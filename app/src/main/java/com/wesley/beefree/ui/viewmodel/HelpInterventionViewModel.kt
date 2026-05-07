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
import com.wesley.beefree.domain.entities.HelpInterventionSession
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
)

class HelpInterventionViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val userProfileRepository: UserProfileRepository,
    private val saveInterventionSessionUseCase: SaveInterventionSessionUseCase,
    private val ticker: Ticker,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HelpInterventionUiState())
    val uiState: StateFlow<HelpInterventionUiState> = _uiState.asStateFlow()

    init {
        loadWizard()
    }

    private fun loadWizard() {
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

            _uiState.update {
                HelpInterventionUiState(
                    allSteps = flow.allSteps,
                    userId = userId,
                    userProfileId = userId,
                    clinicalBranch = profile,
                    isLoading = false,
                )
            }

            startBreathingTimer()
        }
    }

    fun onNext(answer: Any) {
        val currentState = _uiState.value
        val currentStep = currentState.allSteps.getOrNull(currentState.currentStepIndex) ?: return

        val newAnswers = currentState.answers.toMutableMap()
        val stepId = getStepId(currentStep)
        newAnswers[stepId] = answer

        val nextIndex = currentState.currentStepIndex + 1

        if (currentStep is HelpInterventionStep.PostSurfIntensityStep && answer is Int) {
            if (answer >= currentStep.loopThreshold) {
                _uiState.update {
                    it.copy(
                        answers = newAnswers,
                        currentStepIndex = nextIndex,
                        loopCount = it.loopCount + 1,
                    )
                }
                reinsertLoopSteps()
                return
            }
        }

        val isComplete = nextIndex >= currentState.allSteps.size
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

    private fun startBreathingTimer() {
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

    private fun reinsertLoopSteps() {
        _uiState.update { state ->
            val currentIndex = state.currentStepIndex
            val allSteps = state.allSteps.toMutableList()

            val urgeSurfingStep = allSteps.find { it is HelpInterventionStep.UrgeSurfingStep }
            val postSurfStep = allSteps.find { it is HelpInterventionStep.PostSurfIntensityStep }

            if (urgeSurfingStep != null && postSurfStep != null) {
                allSteps.add(currentIndex, postSurfStep)
                allSteps.add(currentIndex, urgeSurfingStep)

                state.copy(
                    allSteps = allSteps,
                    meditationTextIndex = 0,
                )
            } else {
                state
            }
        }
    }

    private fun saveSession() {
        val state = _uiState.value
        if (state.userProfileId == null || state.clinicalBranch == null) return

        viewModelScope.launch {
            val answers = state.answers
            val session =
                HelpInterventionSession(
                    userProfileId = state.userProfileId,
                    intensityBefore = answers["initial_intensity"] as? Int ?: 0,
                    intensityAfter = answers["post_surf_intensity"] as? Int ?: 0,
                    bodyLocations = answers["body_locations"] as? List<String> ?: emptyList(),
                    clinicalBranch = state.clinicalBranch,
                    selectedValue = answers["act_value"] as? String,
                    directionAnswer = answers["act_direction"] as? String,
                    committedAction = answers["act_action"] as? String,
                    automaticThought = answers["tcc_auto_thought"] as? String,
                    alternativeThought = answers["tcc_alternative_thought"] as? String,
                    wasImpulseStillStrong = answers["reflection"] as? Boolean ?: false,
                    surgeSurfLoopCount = state.loopCount,
                    createdAt = System.currentTimeMillis(),
                )

            saveInterventionSessionUseCase.execute(session)
        }
    }

    private fun getStepId(step: HelpInterventionStep): String =
        when (step) {
            is HelpInterventionStep.IntensityStep -> step.id
            is HelpInterventionStep.BodyLocationStep -> "body_locations"
            is HelpInterventionStep.UrgeSurfingStep -> "urge_surfing"
            is HelpInterventionStep.PostSurfIntensityStep -> "post_surf_intensity"
            is HelpInterventionStep.ActValuesStep -> "act_value"
            is HelpInterventionStep.ActDirectionStep -> "act_direction"
            is HelpInterventionStep.ActCommittedActionStep -> "act_action"
            is HelpInterventionStep.TccAutomaticThoughtStep -> "tcc_auto_thought"
            is HelpInterventionStep.TccEvidenceForStep -> "tcc_evidence_for"
            is HelpInterventionStep.TccEvidenceAgainstStep -> "tcc_evidence_against"
            is HelpInterventionStep.TccAlternativeThoughtStep -> "tcc_alternative_thought"
            is HelpInterventionStep.TccActionStep -> "tcc_action"
            is HelpInterventionStep.TimerStep -> "timer"
            is HelpInterventionStep.ReflectionStep -> "reflection"
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
                            ),
                        ticker = RealTicker(),
                        source = source,
                    )
                }
            }
    }
}
