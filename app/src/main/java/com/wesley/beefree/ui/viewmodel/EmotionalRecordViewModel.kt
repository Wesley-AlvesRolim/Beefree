package com.wesley.beefree.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
import com.wesley.beefree.domain.usecases.emotion.SaveEmotionRecordUseCase
import com.wesley.beefree.domain.usecases.risk.CalculateAndSaveRiskAssessmentUseCase
import com.wesley.beefree.domain.usecases.risk.SaveRiskFeatureSnapshotUseCase
import com.wesley.beefree.infrastructure.logging.AndroidLogger
import com.wesley.beefree.infrastructure.logging.Logger
import com.wesley.beefree.infrastructure.storage.adapters.KeyValueRiskWeightsRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomCheckInRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomMetricsRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomRiskFeatureSnapshotRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class EmotionalRecordStep {
    INTRO,
    CAPTURE,
    DONE,
}

enum class EmotionalRecordNavigationDestination {
    Done,
}

data class EmotionalRecordUiState(
    val step: EmotionalRecordStep = EmotionalRecordStep.INTRO,
    val emotions: Map<FeelingType, Float> = defaultEmotionalRecordValues(),
    val isSaving: Boolean = false,
    val error: String? = null,
)

private val emotionalRecordFeelings =
    listOf(
        FeelingType.SLEEP,
        FeelingType.CRAVING,
        FeelingType.BOREDOM,
        FeelingType.STRESS,
        FeelingType.LONELINESS,
        FeelingType.FATIGUE,
    )

private const val DEFAULT_EMOTION_INTENSITY = 5

private fun defaultEmotionalRecordValues(): Map<FeelingType, Float> =
    emotionalRecordFeelings.associateWith { DEFAULT_EMOTION_INTENSITY.toFloat() }

class EmotionalRecordViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val saveEmotionRecordUseCase: SaveEmotionRecordUseCase,
    private val saveRiskFeatureSnapshotUseCase: SaveRiskFeatureSnapshotUseCase,
    private val calculateAndSaveRiskAssessmentUseCase: CalculateAndSaveRiskAssessmentUseCase,
    private val logger: Logger = AndroidLogger,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EmotionalRecordUiState())
    val uiState: StateFlow<EmotionalRecordUiState> = _uiState

    private val _navigationEvents = MutableSharedFlow<EmotionalRecordNavigationDestination>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    init {
        clearData()
    }

    fun clearData() {
        _uiState.value = EmotionalRecordUiState()
    }

    fun onNext() {
        _uiState.value = _uiState.value.copy(step = EmotionalRecordStep.CAPTURE)
    }

    fun onBack() {
        _uiState.value = _uiState.value.copy(step = EmotionalRecordStep.INTRO)
    }

    fun onSliderChange(
        emotion: FeelingType,
        value: Float,
    ) {
        val currentState = _uiState.value
        _uiState.value =
            currentState.copy(
                emotions = currentState.emotions + (emotion to value),
            )
    }

    fun onSave() {
        _uiState.value = _uiState.value.copy(isSaving = true)
        viewModelScope.launch {
            try {
                val userId =
                    withContext(ioDispatcher) {
                        userProfileRepository
                            .getAllProfiles()
                            .first()
                            .firstOrNull()
                            ?.id ?: 0
                    }
                val state = _uiState.value
                var insertedIds: List<Long> = emptyList()
                saveEmotionRecordUseCase
                    .execute(userId = userId, emotions = state.emotions)
                    .onSuccess { ids -> insertedIds = ids }
                    .onFailure { exception ->
                        logger.e(TAG, "Failed to save emotion record", exception)
                        _uiState.value =
                            _uiState.value.copy(
                                error = exception.message,
                                isSaving = false,
                            )
                        return@launch
                    }
                saveRiskFeatureSnapshotUseCase
                    .execute(
                        userId = userId,
                        sleep =
                            state.emotions[FeelingType.SLEEP]?.toInt()
                                ?: DEFAULT_EMOTION_INTENSITY,
                        craving =
                            state.emotions[FeelingType.CRAVING]?.toInt()
                                ?: DEFAULT_EMOTION_INTENSITY,
                        boredom =
                            state.emotions[FeelingType.BOREDOM]?.toInt()
                                ?: DEFAULT_EMOTION_INTENSITY,
                        stress =
                            state.emotions[FeelingType.STRESS]?.toInt()
                                ?: DEFAULT_EMOTION_INTENSITY,
                        loneliness =
                            state.emotions[FeelingType.LONELINESS]?.toInt()
                                ?: DEFAULT_EMOTION_INTENSITY,
                        fatigue =
                            state.emotions[FeelingType.FATIGUE]?.toInt()
                                ?: DEFAULT_EMOTION_INTENSITY,
                    ).onFailure { exception ->
                        saveEmotionRecordUseCase.deleteEmotionRecords(insertedIds)
                        _uiState.value =
                            _uiState.value.copy(
                                error = exception.message,
                                isSaving = false,
                            )
                        return@launch
                    }
                calculateAndSaveRiskAssessmentUseCase
                    .execute(userId)
                    .onFailure { exception ->
                        logger.e(TAG, "Failed to calculate risk assessment", exception)
                        saveEmotionRecordUseCase.deleteEmotionRecords(insertedIds)
                        _uiState.value =
                            _uiState.value.copy(
                                error = exception.message,
                                isSaving = false,
                            )
                        return@launch
                    }
                _uiState.value =
                    _uiState.value.copy(
                        step = EmotionalRecordStep.DONE,
                        isSaving = false,
                    )
            } catch (e: Exception) {
                logger.e(TAG, "Failed to save emotion record", e)
                _uiState.value =
                    _uiState.value.copy(
                        error = e.message,
                        isSaving = false,
                    )
            }
        }
    }

    fun onDone() {
        viewModelScope.launch {
            clearData()
            _navigationEvents.emit(EmotionalRecordNavigationDestination.Done)
        }
    }

    fun resetError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    companion object {
        private const val TAG = "EmotionalRecordViewModel"

        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val database = AppDatabase.getDatabase(context)
                    @Suppress("UNCHECKED_CAST")
                    return EmotionalRecordViewModel(
                        userProfileRepository =
                            RoomUserProfileRepository(
                                database.userProfileDao(),
                                database.userAddictionDao(),
                            ),
                        saveEmotionRecordUseCase =
                            SaveEmotionRecordUseCase(
                                RoomMetricsRepository(
                                    database.emotionRecordDao(),
                                    database.riskFeatureSnapshotDao(),
                                    database.riskAssessmentDao(),
                                ),
                            ),
                        saveRiskFeatureSnapshotUseCase =
                            SaveRiskFeatureSnapshotUseCase(
                                riskFeatureSnapshotRepository =
                                    RoomRiskFeatureSnapshotRepository(
                                        database.riskFeatureSnapshotDao(),
                                    ),
                            ),
                        calculateAndSaveRiskAssessmentUseCase =
                            CalculateAndSaveRiskAssessmentUseCase(
                                metricsRepository =
                                    RoomMetricsRepository(
                                        database.emotionRecordDao(),
                                        database.riskFeatureSnapshotDao(),
                                        database.riskAssessmentDao(),
                                    ),
                                riskWeightsRepository =
                                    KeyValueRiskWeightsRepository(
                                        SharedPreferencesKeyValueStorage(context),
                                    ),
                                checkInRepository =
                                    RoomCheckInRepository(
                                        database.dailyCheckInDao(),
                                        database.weeklyCheckInDao(),
                                    ),
                                addictionRepository =
                                    RoomAddictionRepository(
                                        database.addictionCategoryDao(),
                                        database.relapseRecordDao(),
                                    ),
                            ),
                    ) as T
                }
            }
    }
}
