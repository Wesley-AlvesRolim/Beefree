package com.wesley.beefree.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wesley.beefree.domain.checkin.CheckInType
import com.wesley.beefree.domain.checkin.DopamineType
import com.wesley.beefree.domain.checkin.NaturalDopamineSource
import com.wesley.beefree.domain.checkin.usecases.DetermineCheckInTypeUseCase
import com.wesley.beefree.domain.checkin.usecases.HasCompletedTodaysCheckInUseCase
import com.wesley.beefree.domain.checkin.usecases.SaveDailyCheckInUseCase
import com.wesley.beefree.domain.checkin.usecases.SaveWeeklyCheckInUseCase
import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.infrastructure.storage.adapters.RoomActivityRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomCheckInRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomOnboardingRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.ports.ActivityRepository
import com.wesley.beefree.infrastructure.storage.ports.AddictionRepository
import com.wesley.beefree.infrastructure.storage.ports.CheckInRepository
import com.wesley.beefree.infrastructure.storage.ports.OnboardingRepository
import com.wesley.beefree.infrastructure.storage.ports.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckInViewModel(
    private val checkInRepository: CheckInRepository,
    private val addictionRepository: AddictionRepository,
    private val activityRepository: ActivityRepository,
    private val userProfileRepository: UserProfileRepository,
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {
    private val _checkInType = MutableStateFlow(CheckInType.DAILY)
    val checkInType: StateFlow<CheckInType> = _checkInType.asStateFlow()

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    private val _dopamineLevel = MutableStateFlow(3)
    val dopamineLevel: StateFlow<Int> = _dopamineLevel.asStateFlow()

    private val _dopamineType = MutableStateFlow<DopamineType>(DopamineType.Natural(NaturalDopamineSource.HOBBY))
    val dopamineType: StateFlow<DopamineType> = _dopamineType.asStateFlow()

    private val _mood = MutableStateFlow("")
    val mood: StateFlow<String> = _mood.asStateFlow()

    private val _anxietyLevel = MutableStateFlow<Int?>(null)
    val anxietyLevel: StateFlow<Int?> = _anxietyLevel.asStateFlow()

    private val _valuesAlignmentText = MutableStateFlow("")
    val valuesAlignmentText: StateFlow<String> = _valuesAlignmentText.asStateFlow()

    private val _coreValues = MutableStateFlow<List<UserCoreValue>>(emptyList())
    val coreValues: StateFlow<List<UserCoreValue>> = _coreValues.asStateFlow()

    private val _weatherMood = MutableStateFlow<Int?>(null)
    val weatherMood: StateFlow<Int?> = _weatherMood.asStateFlow()

    private val _weeklyStep = MutableStateFlow(1)
    val weeklyStep: StateFlow<Int> = _weeklyStep.asStateFlow()

    private val _valueConnectionLevels = MutableStateFlow<Map<String, Float>>(emptyMap())
    val valueConnectionLevels: StateFlow<Map<String, Float>> = _valueConnectionLevels.asStateFlow()

    private val _emotionalSatisfaction = MutableStateFlow(0.5f)
    val emotionalSatisfaction: StateFlow<Float> = _emotionalSatisfaction.asStateFlow()

    private val _realConnectionLevel = MutableStateFlow(0.5f)
    val realConnectionLevel: StateFlow<Float> = _realConnectionLevel.asStateFlow()

    private val _weeklyAnxiety = MutableStateFlow(0.5f)
    val weeklyAnxiety: StateFlow<Float> = _weeklyAnxiety.asStateFlow()

    private val _defusionChoice = MutableStateFlow<Int?>(null)
    val defusionChoice: StateFlow<Int?> = _defusionChoice.asStateFlow()

    private val _defusionObservation = MutableStateFlow("")
    val defusionObservation: StateFlow<String> = _defusionObservation.asStateFlow()

    private var userId: Int = 0
    private var addictionTypeId: Int? = null

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val profile = userProfileRepository.getAllProfiles().first().firstOrNull()
                userId = profile?.id ?: 0
                _checkInType.value = DetermineCheckInTypeUseCase().execute(profile?.createdAt ?: 0L)
                _coreValues.value = onboardingRepository.getCoreValues(userId).first()

                val addiction = userProfileRepository.getAddictionsByUserId(userId).first().firstOrNull()
                addictionTypeId = addiction?.addictionTypeId

                val alreadyDone =
                    HasCompletedTodaysCheckInUseCase(
                        checkInRepository,
                        DetermineCheckInTypeUseCase(),
                    ).execute(userId, profile?.createdAt ?: 0L)
                if (alreadyDone) _isCompleted.value = true
            }
        }
    }

    fun updateWeatherMood(level: Int) {
        _weatherMood.value = level
    }

    fun updateDopamineLevel(level: Int) {
        _dopamineLevel.value = level
    }

    fun updateDopamineType(type: DopamineType) {
        _dopamineType.value = type
    }

    fun updateMood(value: String) {
        _mood.value = value
    }

    fun updateAnxietyLevel(level: Int?) {
        _anxietyLevel.value = level
    }

    fun updateValuesAlignmentText(text: String) {
        _valuesAlignmentText.value = text
    }

    fun nextWeeklyStep() {
        if (_weeklyStep.value < 5) _weeklyStep.value++
    }

    fun previousWeeklyStep() {
        if (_weeklyStep.value > 1) _weeklyStep.value--
    }

    fun updateValueConnectionLevel(
        valueName: String,
        level: Float,
    ) {
        _valueConnectionLevels.value = _valueConnectionLevels.value + (valueName to level)
    }

    fun updateEmotionalSatisfaction(level: Float) {
        _emotionalSatisfaction.value = level
    }

    fun updateRealConnectionLevel(level: Float) {
        _realConnectionLevel.value = level
    }

    fun updateWeeklyAnxiety(level: Float) {
        _weeklyAnxiety.value = level
    }

    fun updateDefusionChoice(choice: Int?) {
        _defusionChoice.value = choice
    }

    fun updateDefusionObservation(text: String) {
        _defusionObservation.value = text
    }

    fun submit() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (_checkInType.value) {
                    CheckInType.DAILY ->
                        SaveDailyCheckInUseCase(checkInRepository, addictionRepository, activityRepository).execute(
                            userId = userId,
                            dopamineLevel = _dopamineLevel.value,
                            mood = _mood.value,
                            anxietyLevel = _anxietyLevel.value,
                            dopamineType = _dopamineType.value,
                            addictionTypeId = addictionTypeId,
                        )
                    CheckInType.WEEKLY ->
                        SaveWeeklyCheckInUseCase(checkInRepository).execute(
                            userId = userId,
                            valuesAlignmentText = _valuesAlignmentText.value.ifBlank { null },
                            realConnectionEnergy = ((_realConnectionLevel.value * 4).toInt() + 1).coerceIn(1, 5),
                        )
                }
            }
            _isCompleted.value = true
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val database = AppDatabase.getDatabase(context)
                    @Suppress("UNCHECKED_CAST")
                    return CheckInViewModel(
                        checkInRepository =
                            RoomCheckInRepository(
                                database.dailyCheckInDao(),
                                database.weeklyCheckInDao(),
                            ),
                        addictionRepository =
                            RoomAddictionRepository(
                                database.addictionTypeDao(),
                                database.addictionKeywordDao(),
                                database.relapseHistoryDao(),
                            ),
                        activityRepository =
                            RoomActivityRepository(
                                database.microActivityDao(),
                                database.dailyMicroActivityLogDao(),
                            ),
                        userProfileRepository =
                            RoomUserProfileRepository(
                                database.userProfileDao(),
                                database.userProfileAddictionDao(),
                            ),
                        onboardingRepository =
                            RoomOnboardingRepository(
                                database.userProfileOnboardingResultDao(),
                                database.onboardingScaleAnswerDao(),
                                database.userCoreValueDao(),
                                database.userHobbyDao(),
                                database.userObjectiveDao(),
                                database.userSymptomDao(),
                            ),
                    ) as T
                }
            }
    }
}
