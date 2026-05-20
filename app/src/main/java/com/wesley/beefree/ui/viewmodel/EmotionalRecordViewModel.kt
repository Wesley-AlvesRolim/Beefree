package com.wesley.beefree.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wesley.beefree.domain.emotion.usecases.SaveEmotionRecordUseCase
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.infrastructure.logging.AndroidLogger
import com.wesley.beefree.infrastructure.logging.Logger
import com.wesley.beefree.infrastructure.storage.adapters.RoomMetricsRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.ports.UserProfileRepository
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

private fun defaultEmotionalRecordValues(): Map<FeelingType, Float> = emotionalRecordFeelings.associateWith { 5f }

class EmotionalRecordViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val saveEmotionRecordUseCase: SaveEmotionRecordUseCase,
    private val logger: Logger = AndroidLogger,
) : ViewModel() {
    private var userId: Int = 0

    init {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    userId = userProfileRepository
                        .getAllProfiles()
                        .first()
                        .firstOrNull()
                        ?.id ?: 0
                }
            } catch (e: Exception) {
                logger.e(TAG, "Failed to resolve user ID", e)
            }
        }
    }

    private val _uiState = MutableStateFlow(EmotionalRecordUiState())
    val uiState: StateFlow<EmotionalRecordUiState> = _uiState

    private val _navigationEvents = MutableSharedFlow<EmotionalRecordNavigationDestination>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun onNext() {
        _uiState.value = _uiState.value.copy(step = EmotionalRecordStep.CAPTURE)
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
            val state = _uiState.value
            val result =
                saveEmotionRecordUseCase.execute(
                    userId = userId,
                    emotions = state.emotions,
                )

            result
                .onSuccess {
                    _uiState.value =
                        _uiState.value.copy(
                            step = EmotionalRecordStep.DONE,
                            isSaving = false,
                        )
                }.onFailure { exception ->
                    logger.e(TAG, "Failed to save emotion record", exception)
                    _uiState.value =
                        _uiState.value.copy(
                            error = exception.message,
                            isSaving = false,
                        )
                }
        }
    }

    fun onDone() {
        viewModelScope.launch {
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
                    ) as T
                }
            }
    }
}
