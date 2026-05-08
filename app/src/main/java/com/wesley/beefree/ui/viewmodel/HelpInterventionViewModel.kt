package com.wesley.beefree.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wesley.beefree.domain.entities.BreathingPhaseEnum
import com.wesley.beefree.domain.entities.BreathingPhaseEnum.EXHALE
import com.wesley.beefree.domain.entities.BreathingPhaseEnum.HOLD
import com.wesley.beefree.domain.entities.BreathingPhaseEnum.INHALE
import com.wesley.beefree.domain.entities.CognitiveThoughtRecord
import com.wesley.beefree.domain.entities.CoreValueType
import com.wesley.beefree.domain.entities.InterventionRecord
import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.domain.intervention.ClinicalProfileStrategyFactory
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.domain.intervention.RealTicker
import com.wesley.beefree.domain.intervention.ports.Ticker
import com.wesley.beefree.domain.intervention.usecases.SaveInterventionSessionUseCase
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.infrastructure.storage.adapters.RoomEMIRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomOnboardingRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.ports.OnboardingRepository
import com.wesley.beefree.infrastructure.storage.ports.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class HelpInterventionSource {
    FAB,
    WIDGET,
}

enum class AnswerKey(
    val value: String,
) {
    INITIAL_INTENSITY("initial_intensity"),
    BODY_LOCATIONS("body_locations"),
    URGE_SURFING("urge_surfing"),
    POST_SURF_INTENSITY("post_surf_intensity"),
    ACT_VALUE("act_value"),
    ACT_VALUE_CUSTOM("act_value_custom"),
    ACT_DIRECTION("act_direction"),
    ACT_ACTION("act_action"),
    ACT_ACTION_CUSTOM("act_action_custom"),
    TCC_AUTO_THOUGHT("tcc_auto_thought"),
    TCC_EVIDENCE_FOR("tcc_evidence_for"),
    TCC_EVIDENCE_AGAINST("tcc_evidence_against"),
    TCC_ALTERNATIVE_THOUGHT("tcc_alternative_thought"),
    TCC_ACTION("tcc_action"),
    TCC_ACTION_CUSTOM("tcc_action_custom"),
    TIMER("timer"),
    TIMER_SECONDS("timer_seconds"),
    TIMER_STARTED("timer_started"),
    REFLECTION("reflection"),
}

data class HelpInterventionUiState(
    val allSteps: List<HelpInterventionStep> = emptyList(),
    val currentStepIndex: Int = 0,
    val answers: Map<String, Any> = emptyMap(),
    val breathingPhaseEnum: BreathingPhaseEnum = INHALE,
    val breathingSecondsLeft: Int = INHALE.durationSeconds,
    val breathingCycleCount: Int = 0,
    val meditationTextIndex: Int = 0,
    val loopCount: Int = 0,
    val isLoading: Boolean = true,
    val isComplete: Boolean = false,
    val userId: Int? = null,
    val userProfileId: Int? = null,
    val clinicalBranch: TreatmentProfile? = null,
    val openedFrom: HelpInterventionSource = HelpInterventionSource.FAB,
    val userCoreValues: List<UserCoreValue> = emptyList(),
)

class HelpInterventionViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val userProfileRepository: UserProfileRepository,
    private val saveInterventionSessionUseCase: SaveInterventionSessionUseCase,
    private val ticker: Ticker,
    private val source: HelpInterventionSource = HelpInterventionSource.FAB,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HelpInterventionUiState(openedFrom = source))
    val uiState: StateFlow<HelpInterventionUiState> = _uiState.asStateFlow()
    private var breathingTimerStarted = false

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val user = userProfileRepository.getAllProfiles().first().firstOrNull()
            val userId = user?.id
            if (userId == null) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            val session = onboardingRepository.getOnboardingSession(userId)
            val profile =
                session?.let {
                    runCatching {
                        TreatmentProfile.valueOf(it.clinicalApproach)
                    }.getOrNull()
                }

            val strategy = profile?.let { ClinicalProfileStrategyFactory.from(it) }
            val flow =
                strategy?.helpInterventionFlow
                    ?: return@launch

            val userCoreValues = onboardingRepository.getCoreValues(userId).first()
            val userValueNames = userCoreValues.map { it.value.name }.toSet()

            val sortedSteps =
                flow.allSteps.map { step ->
                    if (step is HelpInterventionStep.ActValuesStep) {
                        step.copy(
                            predefinedOptions =
                                step.predefinedOptions
                                    .sortedWith(compareByDescending { it.id in userValueNames }),
                        )
                    } else {
                        step
                    }
                }

            _uiState.update {
                HelpInterventionUiState(
                    allSteps = sortedSteps,
                    userId = userId,
                    userProfileId = userId,
                    clinicalBranch = profile,
                    isLoading = false,
                    userCoreValues = userCoreValues,
                )
            }
        }
    }

    fun onNext(answer: Any) {
        val currentState = _uiState.value
        val currentStep = currentState.allSteps.getOrNull(currentState.currentStepIndex) ?: return

        val newAnswers = currentState.answers.toMutableMap()
        val stepId = stepAnswerKey(currentStep)

        if (currentStep is HelpInterventionStep.PostSurfIntensityStep && answer is Int) {
            newAnswers[stepId] = answer
            if (answer >= currentStep.loopThreshold) {
                val urgeSurfingIndex =
                    currentState.allSteps.indexOfFirst { it is HelpInterventionStep.UrgeSurfingStep }
                if (urgeSurfingIndex >= 0) {
                    _uiState.update {
                        it.copy(
                            answers = newAnswers,
                            currentStepIndex = urgeSurfingIndex,
                            loopCount = it.loopCount + 1,
                            meditationTextIndex = 0,
                        )
                    }
                    return
                }
            }
        } else if (currentStep !is HelpInterventionStep.IntensityStep && answer !is Boolean) {
            newAnswers[stepId] = answer
        }

        val nextIndex = currentState.currentStepIndex + 1
        val isComplete = nextIndex >= currentState.allSteps.size - 1
        _uiState.update {
            it.copy(
                answers = newAnswers,
                currentStepIndex = nextIndex,
                isComplete = isComplete,
            )
        }

        if (isComplete) {
            saveSession()
        }
    }

    fun onBack() {
        val currentState = _uiState.value
        if (currentState.currentStepIndex > 0) {
            _uiState.update { it.copy(currentStepIndex = it.currentStepIndex - 1) }
        }
    }

    fun updateAnswer(
        key: String,
        value: Any,
    ) {
        _uiState.update { state ->
            state.copy(
                answers =
                    state.answers
                        .toMutableMap()
                        .apply {
                            put(key, value)
                        }.toMap(),
            )
        }
    }

    fun getAnswer(key: String): Any? = _uiState.value.answers[key]

    fun getAnswerAsInt(key: String): Int = (getAnswer(key) as? Int) ?: 0

    fun getAnswerAsString(key: String): String = (getAnswer(key) as? String) ?: ""

    fun getAnswerAsSet(key: String): Set<String> = (getAnswer(key) as? Set<String>) ?: emptySet()

    fun advanceMeditationStep() {
        _uiState.update {
            val currentStep = it.allSteps.getOrNull(it.currentStepIndex)
            val maxIndex =
                when (currentStep) {
                    is HelpInterventionStep.UrgeSurfingStep -> currentStep.meditationStepKeys.size - 1
                    else -> 0
                }
            it.copy(meditationTextIndex = (it.meditationTextIndex + 1).coerceAtMost(maxIndex))
        }
    }

    fun startBreathingTimer() {
        if (breathingTimerStarted) return
        breathingTimerStarted = true

        viewModelScope.launch {
            ticker.ticks().collect {
                updateBreathingPhase()
            }
        }
    }

    private fun updateBreathingPhase() {
        _uiState.update { state ->
            var newPhase = state.breathingPhaseEnum
            var newSecondsLeft = state.breathingSecondsLeft - 1
            var newCycleCount = state.breathingCycleCount

            if (newSecondsLeft <= 0) {
                newPhase =
                    when (state.breathingPhaseEnum) {
                        INHALE -> HOLD
                        HOLD -> EXHALE
                        EXHALE -> {
                            newCycleCount++
                            INHALE
                        }
                    }
                newSecondsLeft = newPhase.durationSeconds
            }

            state.copy(
                breathingPhaseEnum = newPhase,
                breathingSecondsLeft = newSecondsLeft,
                breathingCycleCount = newCycleCount,
            )
        }
    }

    private fun saveSession() {
        val state = _uiState.value
        if (state.userProfileId == null || state.clinicalBranch == null) return

        viewModelScope.launch {
            val answers = state.answers
            val createdAt = System.currentTimeMillis()
            val intensityBefore = answers[AnswerKey.INITIAL_INTENSITY.value] as? Int ?: 0
            val intensityAfter = answers[AnswerKey.POST_SURF_INTENSITY.value] as? Int ?: 0
            val bodyLocations =
                (answers[AnswerKey.BODY_LOCATIONS.value] as? Set<*>)
                    ?.filterIsInstance<String>() ?: emptyList()
            val committedAction =
                (answers[AnswerKey.ACT_ACTION.value] as? String)
                    ?: (answers[AnswerKey.TCC_ACTION.value] as? String)
            val automaticThought = answers[AnswerKey.TCC_AUTO_THOUGHT.value] as? String
            val alternativeThought = answers[AnswerKey.TCC_ALTERNATIVE_THOUGHT.value] as? String

            val interventionRecord =
                InterventionRecord(
                    userProfileId = state.userProfileId,
                    interventionType = "EMI_${state.clinicalBranch.name}",
                    triggerType = source.name,
                    wasCompleted = (answers[AnswerKey.REFLECTION.value] as? Boolean ?: false).not(),
                    createdAt = createdAt,
                )

            val thoughtRecord =
                if (automaticThought.isNullOrEmpty().not() ||
                    alternativeThought
                        .isNullOrEmpty()
                        .not()
                ) {
                    CognitiveThoughtRecord(
                        userProfileId = state.userProfileId,
                        situation =
                            "$intensityBefore <> $intensityAfter|" +
                                bodyLocations.joinToString(),
                        automaticThought = automaticThought ?: "",
                        feeling = bodyLocations.joinToString("|"),
                        consequence = committedAction ?: "N/A",
                        alternativeThought = alternativeThought ?: "",
                        cognitiveDistortions = emptyList(),
                        createdAt = createdAt,
                    )
                } else {
                    null
                }

            val selectedValueName = answers[AnswerKey.ACT_VALUE.value] as? String
            val selectedCoreValue =
                selectedValueName
                    ?.let { runCatching { CoreValueType.valueOf(it) }.getOrNull() }

            saveInterventionSessionUseCase.execute(
                interventionRecord = interventionRecord,
                thoughtRecord = thoughtRecord,
                selectedCoreValue = selectedCoreValue,
                userProfileId = state.userProfileId,
            )
        }
    }

    companion object {
        fun factory(
            context: Context,
            source: HelpInterventionSource = HelpInterventionSource.FAB,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val db = AppDatabase.getDatabase(context)
                    HelpInterventionViewModel(
                        onboardingRepository =
                            RoomOnboardingRepository(
                                onboardingSessionDao = db.userOnboardingSessionDao(),
                                coreValueDao = db.userCoreValueDao(),
                                hobbyDao = db.userHobbyDao(),
                                objectiveDao = db.userObjectiveDao(),
                                symptomDao = db.userSymptomDao(),
                            ),
                        userProfileRepository =
                            RoomUserProfileRepository(
                                userProfileDao = db.userProfileDao(),
                                userAddictionDao = db.userAddictionDao(),
                            ),
                        saveInterventionSessionUseCase =
                            SaveInterventionSessionUseCase(
                                emiRepository =
                                    RoomEMIRepository(
                                        interventionRecordDao = db.interventionRecordDao(),
                                        thoughtRecordDao = db.cognitiveThoughtRecordDao(),
                                        interventionValueLinkDao = db.interventionValueLinkDao(),
                                    ),
                                onboardingRepository =
                                    RoomOnboardingRepository(
                                        onboardingSessionDao = db.userOnboardingSessionDao(),
                                        coreValueDao = db.userCoreValueDao(),
                                        hobbyDao = db.userHobbyDao(),
                                        objectiveDao = db.userObjectiveDao(),
                                        symptomDao = db.userSymptomDao(),
                                    ),
                            ),
                        ticker = RealTicker(),
                        source = source,
                    )
                }
            }
    }
}

internal fun stepAnswerKey(step: HelpInterventionStep): String =
    when (step) {
        is HelpInterventionStep.IntensityStep -> step.id
        is HelpInterventionStep.BodyLocationStep -> AnswerKey.BODY_LOCATIONS.value
        is HelpInterventionStep.UrgeSurfingStep -> AnswerKey.URGE_SURFING.value
        is HelpInterventionStep.PostSurfIntensityStep -> AnswerKey.POST_SURF_INTENSITY.value
        is HelpInterventionStep.ActValuesStep -> AnswerKey.ACT_VALUE.value
        is HelpInterventionStep.ActDirectionStep -> AnswerKey.ACT_DIRECTION.value
        is HelpInterventionStep.ActCommittedActionStep -> AnswerKey.ACT_ACTION.value
        is HelpInterventionStep.TccAutomaticThoughtStep -> AnswerKey.TCC_AUTO_THOUGHT.value
        is HelpInterventionStep.TccEvidenceForStep -> AnswerKey.TCC_EVIDENCE_FOR.value
        is HelpInterventionStep.TccEvidenceAgainstStep -> AnswerKey.TCC_EVIDENCE_AGAINST.value
        is HelpInterventionStep.TccAlternativeThoughtStep -> AnswerKey.TCC_ALTERNATIVE_THOUGHT.value
        is HelpInterventionStep.TccActionStep -> AnswerKey.TCC_ACTION.value
        is HelpInterventionStep.TimerStep -> AnswerKey.TIMER.value
        is HelpInterventionStep.ReflectionStep -> AnswerKey.REFLECTION.value
    }
