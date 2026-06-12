package com.wesley.beefree.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wesley.beefree.domain.checkin.ActivityType
import com.wesley.beefree.domain.checkin.BooleanBranchStep
import com.wesley.beefree.domain.checkin.DailyCheckInAnswer
import com.wesley.beefree.domain.checkin.DailyCheckInFlow
import com.wesley.beefree.domain.checkin.DailyCheckInStep
import com.wesley.beefree.domain.checkin.EmotionalRecordStep
import com.wesley.beefree.domain.checkin.SingleSelectWithContextStep
import com.wesley.beefree.domain.checkin.TherapeuticActivityStep
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.repository.ports.OnboardingRepository
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
import com.wesley.beefree.domain.usecases.checkin.DetermineCheckInTypeUseCase
import com.wesley.beefree.domain.usecases.checkin.HasCompletedTodaysCheckInUseCase
import com.wesley.beefree.domain.usecases.checkin.LoadDailyCheckInFlowUseCase
import com.wesley.beefree.domain.usecases.checkin.LoadPreviousObjectiveUseCase
import com.wesley.beefree.domain.usecases.checkin.LoadTodaysEmotionRecordUseCase
import com.wesley.beefree.domain.usecases.checkin.SaveDailyCheckInUseCase
import com.wesley.beefree.domain.usecases.checkin.SelectTherapeuticActivityUseCase
import com.wesley.beefree.domain.usecases.risk.CalculateAndSaveRiskAssessmentUseCase
import com.wesley.beefree.infrastructure.logging.AndroidLogger
import com.wesley.beefree.infrastructure.logging.Logger
import com.wesley.beefree.infrastructure.storage.adapters.KeyValueRiskWeightsRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomCheckInRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomMetricsRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomOnboardingRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class DailyCheckInPhase { INVITE, FLOW, DONE }

sealed class CheckInNavigationDestination {
    object EmotionalRecord : CheckInNavigationDestination()
}

class CheckInViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val onboardingRepository: OnboardingRepository,
    private val calculateAndSaveRiskAssessmentUseCase: CalculateAndSaveRiskAssessmentUseCase,
    private val loadTodaysEmotionRecordUseCase: LoadTodaysEmotionRecordUseCase,
    private val hasCompletedTodaysCheckInUseCase: HasCompletedTodaysCheckInUseCase,
    private val loadPreviousObjectiveUseCase: LoadPreviousObjectiveUseCase,
    private val loadDailyCheckInFlowUseCase: LoadDailyCheckInFlowUseCase,
    private val selectTherapeuticActivityUseCase: SelectTherapeuticActivityUseCase,
    private val saveDailyCheckInUseCase: SaveDailyCheckInUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val logger: Logger = AndroidLogger,
) : ViewModel() {
    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    private val _dailyPhase = MutableStateFlow(DailyCheckInPhase.INVITE)
    val dailyPhase: StateFlow<DailyCheckInPhase> = _dailyPhase.asStateFlow()

    private val _dailyFlow = MutableStateFlow<DailyCheckInFlow?>(null)
    val dailyFlow: StateFlow<DailyCheckInFlow?> = _dailyFlow.asStateFlow()

    private val _dailyAnswers = MutableStateFlow<Map<String, DailyCheckInAnswer>>(emptyMap())
    val dailyAnswers: StateFlow<Map<String, DailyCheckInAnswer>> = _dailyAnswers.asStateFlow()

    private val _dailyVisitedSteps = MutableStateFlow<List<String>>(emptyList())
    val dailyVisitedSteps: StateFlow<List<String>> = _dailyVisitedSteps.asStateFlow()

    private val _profileName = MutableStateFlow<String?>(null)
    val profileName: StateFlow<String?> = _profileName.asStateFlow()

    private val _treatmentProfile = MutableStateFlow(TreatmentProfile.ACT)
    val treatmentProfile: StateFlow<TreatmentProfile> = _treatmentProfile.asStateFlow()

    private val _hasEmotionalRecordToday = MutableStateFlow(false)
    val hasEmotionalRecordToday: StateFlow<Boolean> = _hasEmotionalRecordToday.asStateFlow()

    private val _todaysEmotionRecord = MutableStateFlow<Map<FeelingType, Int>?>(null)
    val todaysEmotionRecord: StateFlow<Map<FeelingType, Int>?> = _todaysEmotionRecord.asStateFlow()

    private val _previousObjective = MutableStateFlow<String?>(null)
    val previousObjective: StateFlow<String?> = _previousObjective.asStateFlow()

    private val _selectedActivity = MutableStateFlow<ActivityType?>(null)
    val selectedActivity: StateFlow<ActivityType?> = _selectedActivity.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<CheckInNavigationDestination>(extraBufferCapacity = 1)
    val navigationEvents: SharedFlow<CheckInNavigationDestination> = _navigationEvents.asSharedFlow()

    private var userId: Int = 0
    private var addictionTypeId: Int? = null

    init {
        viewModelScope.launch {
            try {
                withContext(ioDispatcher) {
                    val profile = userProfileRepository.getAllProfiles().first().firstOrNull()
                    userId = profile?.id ?: 0
                    _profileName.value = profile?.profileName

                    val alreadyDone =
                        hasCompletedTodaysCheckInUseCase.execute(userId, profile?.createdAt ?: 0L)
                    if (alreadyDone) _isCompleted.value = true

                    refreshTodaysEmotionRecord(userId)

                    _previousObjective.value =
                        loadPreviousObjectiveUseCase.execute(userId)

                    val session = onboardingRepository.getOnboardingSession(userId)
                    if (session != null) {
                        val resolvedProfile =
                            runCatching {
                                TreatmentProfile.valueOf(session.clinicalApproach)
                            }.onFailure { e ->
                                logger.e(TAG, "Failed to parse treatment profile: ${session.clinicalApproach}", e)
                            }.getOrDefault(TreatmentProfile.ACT)
                        _treatmentProfile.value = resolvedProfile
                        val flow = loadDailyCheckInFlowUseCase.execute(resolvedProfile)
                        _dailyFlow.value = flow

                        val activityStep = flow.steps.filterIsInstance<TherapeuticActivityStep>().firstOrNull()
                        if (activityStep != null) {
                            _selectedActivity.value =
                                selectTherapeuticActivityUseCase.execute(
                                    userId = userId,
                                    activityOptions = activityStep.activityOptions,
                                )
                        }
                    }

                    val addiction = userProfileRepository.getAddictionsByUserId(userId).first().firstOrNull()
                    addictionTypeId = addiction?.addictionCategoryId
                }
            } catch (e: Exception) {
                logger.e(TAG, "Failed to initialize check-in data", e)
            }
        }
    }

    fun startDailyFlow() {
        _dailyPhase.value = DailyCheckInPhase.FLOW
        val firstStep = flattenedSteps().firstOrNull()
        if (firstStep != null) _dailyVisitedSteps.value = listOf(firstStep.id)
    }

    fun resetDailyFlowState() {
        _dailyPhase.value = DailyCheckInPhase.INVITE
        _dailyAnswers.value = emptyMap()
        _dailyVisitedSteps.value = emptyList()
        _selectedActivity.value = null
    }

    fun startEmotionRecord() {
        _navigationEvents.tryEmit(CheckInNavigationDestination.EmotionalRecord)
    }

    fun onReturnFromEmotionalRecord() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                refreshTodaysEmotionRecord(userId)
            }
        }
    }

    private suspend fun refreshTodaysEmotionRecord(userId: Int) {
        val todaysRecord = loadTodaysEmotionRecordUseCase.execute(userId)
        _todaysEmotionRecord.value = todaysRecord
        _hasEmotionalRecordToday.value = todaysRecord != null
    }

    fun currentDailyStep(): DailyCheckInStep? {
        val steps = flattenedSteps()
        val currentId = _dailyVisitedSteps.value.lastOrNull() ?: return null
        return steps.find { it.id == currentId }
    }

    fun currentStepNumber(): Int = _dailyVisitedSteps.value.size

    fun totalStepsEstimate(): Int = flattenedSteps().size

    fun setDailyAnswer(
        id: String,
        answer: DailyCheckInAnswer,
    ) {
        _dailyAnswers.value += (id to answer)
    }

    fun selectActivity(type: ActivityType) {
        _selectedActivity.value = type
        val stepId =
            flattenedSteps()
                .filterIsInstance<TherapeuticActivityStep>()
                .firstOrNull()
                ?.id ?: return
        _dailyAnswers.value += (stepId to DailyCheckInAnswer.TherapeuticActivity(type))
    }

    fun previousDailyStep() {
        val visited = _dailyVisitedSteps.value
        if (visited.size <= 1) return
        _dailyVisitedSteps.value = visited.dropLast(1)
    }

    fun nextDailyStep() {
        val currentStep = currentDailyStep()

        if (currentStep is EmotionalRecordStep && _hasEmotionalRecordToday.value) {
            advanceToNextStep()
            return
        }

        if (currentStep is EmotionalRecordStep && !_hasEmotionalRecordToday.value) {
            val answered = _dailyAnswers.value[currentStep.id] as? DailyCheckInAnswer.EmotionalRecord
            if (answered == null) {
                _navigationEvents.tryEmit(CheckInNavigationDestination.EmotionalRecord)
                return
            }
        }

        advanceToNextStep()
    }

    private fun advanceToNextStep() {
        val steps = flattenedSteps()
        val currentId = _dailyVisitedSteps.value.lastOrNull()
        val currentIndex = steps.indexOfFirst { it.id == currentId }
        val next = steps.getOrNull(currentIndex + 1)
        if (next != null) {
            _dailyVisitedSteps.value += next.id
        } else {
            submit()
            _dailyPhase.value = DailyCheckInPhase.DONE
        }
    }

    fun flattenedSteps(): List<DailyCheckInStep> {
        val flow = _dailyFlow.value ?: return emptyList()
        val answers = _dailyAnswers.value
        val hasPreviousObjective = _previousObjective.value != null
        val selected = _selectedActivity.value

        return buildList {
            for (step in flow.steps) {
                if (step is SingleSelectWithContextStep && step.id.endsWith("objective_review") && !hasPreviousObjective) {
                    continue
                }
                add(step)
                when (step) {
                    is BooleanBranchStep -> {
                        val answer = answers[step.id] as? DailyCheckInAnswer.Bool
                        when (answer?.value) {
                            true -> add(step.onYes)
                            false -> add(step.onNo)
                            null -> Unit
                        }
                    }
                    is TherapeuticActivityStep -> {
                        if (selected != null) {
                            val option = step.activityOptions.find { it.type == selected }
                            if (option != null) addAll(option.subSteps)
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    fun submit() {
        viewModelScope.launch {
            try {
                withContext(ioDispatcher) {
                    saveDailyCheckInUseCase.execute(
                        userId = userId,
                        treatmentProfile = _treatmentProfile.value,
                        answers = _dailyAnswers.value,
                        addictionTypeId = addictionTypeId,
                    )
                    calculateAndSaveRiskAssessmentUseCase.execute(userId)
                }
                _isCompleted.value = true
            } catch (e: Exception) {
                logger.e(TAG, "Failed to submit check-in", e)
            }
        }
    }

    companion object {
        private const val TAG = "CheckInViewModel"

        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val database = AppDatabase.getDatabase(context)
                    val checkInRepository =
                        RoomCheckInRepository(
                            database.dailyCheckInDao(),
                            database.weeklyCheckInDao(),
                        )
                    val addictionRepository =
                        RoomAddictionRepository(
                            database.addictionCategoryDao(),
                            database.relapseRecordDao(),
                        )
                    val metricsRepository =
                        RoomMetricsRepository(
                            database.emotionRecordDao(),
                            database.riskFeatureSnapshotDao(),
                            database.riskAssessmentDao(),
                        )
                    @Suppress("UNCHECKED_CAST")
                    return CheckInViewModel(
                        userProfileRepository =
                            RoomUserProfileRepository(
                                database.userProfileDao(),
                                database.userAddictionDao(),
                            ),
                        onboardingRepository =
                            RoomOnboardingRepository(
                                database.userOnboardingSessionDao(),
                                database.userCoreValueDao(),
                                database.userHobbyDao(),
                                database.userObjectiveDao(),
                                database.userSymptomDao(),
                            ),
                        calculateAndSaveRiskAssessmentUseCase =
                            CalculateAndSaveRiskAssessmentUseCase(
                                metricsRepository = metricsRepository,
                                riskWeightsRepository =
                                    KeyValueRiskWeightsRepository(
                                        SharedPreferencesKeyValueStorage(context),
                                    ),
                                checkInRepository = checkInRepository,
                                addictionRepository = addictionRepository,
                            ),
                        loadTodaysEmotionRecordUseCase =
                            LoadTodaysEmotionRecordUseCase(metricsRepository),
                        hasCompletedTodaysCheckInUseCase =
                            HasCompletedTodaysCheckInUseCase(
                                checkInRepository = checkInRepository,
                                determineCheckInTypeUseCase = DetermineCheckInTypeUseCase(),
                            ),
                        loadPreviousObjectiveUseCase =
                            LoadPreviousObjectiveUseCase(checkInRepository),
                        loadDailyCheckInFlowUseCase = LoadDailyCheckInFlowUseCase(),
                        selectTherapeuticActivityUseCase =
                            SelectTherapeuticActivityUseCase(checkInRepository),
                        saveDailyCheckInUseCase =
                            SaveDailyCheckInUseCase(
                                checkInRepository = checkInRepository,
                                addictionRepository = addictionRepository,
                            ),
                    ) as T
                }
            }
    }
}
