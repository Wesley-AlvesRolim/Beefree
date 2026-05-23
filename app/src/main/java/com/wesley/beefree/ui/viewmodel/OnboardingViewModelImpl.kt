package com.wesley.beefree.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wesley.beefree.domain.onboarding.ClinicalProfile
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.OnboardingStep
import com.wesley.beefree.domain.onboarding.ScaleResult
import com.wesley.beefree.domain.onboarding.StepType
import com.wesley.beefree.domain.onboarding.engine.CompositeOnboardingFlowEngine
import com.wesley.beefree.domain.onboarding.engine.OnboardingFlowFactory
import com.wesley.beefree.domain.onboarding.ports.OnboardingFlowEngine
import com.wesley.beefree.domain.onboarding.usecases.ComputeClinicalProfileUseCase
import com.wesley.beefree.domain.onboarding.usecases.ComputeScoreUseCase
import com.wesley.beefree.domain.onboarding.usecases.SaveOnboardingDataUseCase
import com.wesley.beefree.infrastructure.logging.AndroidLogger
import com.wesley.beefree.infrastructure.logging.Logger
import com.wesley.beefree.infrastructure.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomLessonRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomOnboardingRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.repositories.KeyValueStorageRepository
import com.wesley.beefree.ui.viewmodel.ports.OnboardingViewModelPort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class OnboardingViewModelImpl(
    private val engine: OnboardingFlowEngine,
    private val saveOnboardingDataUseCase: SaveOnboardingDataUseCase,
    private val computeScoreUseCase: ComputeScoreUseCase,
    private val computeClinicalProfileUseCase: ComputeClinicalProfileUseCase,
    private val logger: Logger = AndroidLogger,
) : ViewModel(),
    OnboardingViewModelPort {
    private val _currentStep = MutableStateFlow(engine.currentStep)
    override val currentStep: StateFlow<OnboardingStep> = _currentStep.asStateFlow()

    private val _answers = MutableStateFlow(OnboardingAnswers())
    override val answers: StateFlow<OnboardingAnswers> = _answers.asStateFlow()

    private val _scaleResult = MutableStateFlow<ScaleResult?>(null)
    override val scaleResult: StateFlow<ScaleResult?> = _scaleResult.asStateFlow()

    private val _clinicalProfile = MutableStateFlow<ClinicalProfile?>(null)
    override val clinicalProfile: StateFlow<ClinicalProfile?> = _clinicalProfile.asStateFlow()

    override fun updateAnswer(update: OnboardingAnswers.() -> OnboardingAnswers) {
        _answers.value = _answers.value.update()
    }

    override fun next() {
        engine.next(_answers.value)
        _currentStep.value = engine.currentStep
        if (engine.currentStep.type == StepType.SCORE_RESULT) {
            computeScore()
            computeClinicalProfile()
        }
    }

    override fun previous() {
        engine.previous()
        _currentStep.value = engine.currentStep
    }

    override fun finishOnboarding(
        onFinish: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        viewModelScope.launch {
            val profile = _clinicalProfile.value
            logger.d(
                TAG,
                "Clinical profile on finish: treatment=${profile?.treatmentProfile}, incongruence=${profile?.incongruenceLevel}",
            )
            saveOnboardingDataUseCase
                .execute(_answers.value)
                .onSuccess { onFinish() }
                .onFailure {
                    logger.e(TAG, "Failed to save onboarding data: ${it.message}", it)
                    onError(it)
                }
        }
    }

    private fun computeScore() {
        _scaleResult.value = computeScoreUseCase.execute(_answers.value)
    }

    private fun computeClinicalProfile() {
        _clinicalProfile.value = computeClinicalProfileUseCase.execute(_answers.value)
    }

    companion object {
        private const val TAG = "OnboardingViewModelImpl"

        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val database = AppDatabase.getDatabase(context)
                    val addictionRepository =
                        RoomAddictionRepository(
                            database.addictionCategoryDao(),
                            database.relapseRecordDao(),
                        )
                    val userProfileRepository =
                        RoomUserProfileRepository(
                            database.userProfileDao(),
                            database.userAddictionDao(),
                        )
                    val onboardingRepository =
                        RoomOnboardingRepository(
                            database.userOnboardingSessionDao(),
                            database.userCoreValueDao(),
                            database.userHobbyDao(),
                            database.userObjectiveDao(),
                            database.userSymptomDao(),
                        )
                    val lessonRepository = RoomLessonRepository(database.psychoeducationContentDao())
                    val keyValueStorageRepository =
                        KeyValueStorageRepository(SharedPreferencesKeyValueStorage(context))
                    val computeScoreUseCase = ComputeScoreUseCase()
                    val computeClinicalProfileUseCase = ComputeClinicalProfileUseCase()
                    @Suppress("UNCHECKED_CAST")
                    return OnboardingViewModelImpl(
                        engine =
                            CompositeOnboardingFlowEngine(
                                OnboardingFlowFactory.factory(),
                            ),
                        saveOnboardingDataUseCase =
                            SaveOnboardingDataUseCase(
                                addictionRepository,
                                userProfileRepository,
                                onboardingRepository,
                                lessonRepository,
                                keyValueStorageRepository,
                                computeScoreUseCase = computeScoreUseCase,
                                computeClinicalProfileUseCase = computeClinicalProfileUseCase,
                            ),
                        computeScoreUseCase = computeScoreUseCase,
                        computeClinicalProfileUseCase = computeClinicalProfileUseCase,
                    ) as T
                }
            }
    }
}
