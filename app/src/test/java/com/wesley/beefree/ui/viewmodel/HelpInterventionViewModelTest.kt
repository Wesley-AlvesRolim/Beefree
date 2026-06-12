package com.wesley.beefree.ui.viewmodel

import com.wesley.beefree.domain.entities.BreathingPhaseEnum
import com.wesley.beefree.domain.entities.UserOnboardingSession
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.domain.mocks.EMIRepositoryMock
import com.wesley.beefree.domain.mocks.MetricsRepositoryMock
import com.wesley.beefree.domain.mocks.OnboardingRepositoryMock
import com.wesley.beefree.domain.mocks.RiskWeightsRepositoryMock
import com.wesley.beefree.domain.mocks.TickerMock
import com.wesley.beefree.domain.mocks.UserProfileRepositoryMock
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.usecases.intervention.SaveInterventionSessionUseCase
import com.wesley.beefree.domain.usecases.risk.CalculateAndSaveRiskAssessmentUseCase
import com.wesley.beefree.infrastructure.logging.TestLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.OptIn

@OptIn(ExperimentalCoroutinesApi::class)
class HelpInterventionViewModelTest {
    private lateinit var onboardingRepository: OnboardingRepositoryMock
    private lateinit var userProfileRepository: UserProfileRepositoryMock
    private lateinit var emiRepository: EMIRepositoryMock
    private lateinit var saveInterventionSessionUseCase: SaveInterventionSessionUseCase
    private lateinit var ticker: TickerMock
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        onboardingRepository = OnboardingRepositoryMock()
        userProfileRepository = UserProfileRepositoryMock()
        emiRepository = EMIRepositoryMock()
        saveInterventionSessionUseCase =
            SaveInterventionSessionUseCase(
                emiRepository = emiRepository,
                onboardingRepository = onboardingRepository,
            )
        ticker = TickerMock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `update answer stores value in map`() {
        val viewModel = createEmptyViewModel()
        val key = "test_key"
        val value = "test_value"
        viewModel.updateAnswer(key, value)
        assertEquals("Answer should be stored", value, viewModel.getAnswer(key))
    }

    @Test
    fun `get answer as int returns zero when missing`() {
        val viewModel = createEmptyViewModel()
        val result = viewModel.getAnswerAsInt("nonexistent_key")
        assertEquals("Should return 0 for missing key", 0, result)
    }

    @Test
    fun `get answer as int returns actual value`() {
        val viewModel = createEmptyViewModel()
        viewModel.updateAnswer("test_int", 42)
        val result = viewModel.getAnswerAsInt("test_int")
        assertEquals("Should return actual int value", 42, result)
    }

    @Test
    fun `get answer as string returns empty when missing`() {
        val viewModel = createEmptyViewModel()
        val result = viewModel.getAnswerAsString("nonexistent_key")
        assertEquals("Should return empty string for missing key", "", result)
    }

    @Test
    fun `get answer as set returns empty when missing`() {
        val viewModel = createEmptyViewModel()
        val result = viewModel.getAnswerAsSet("nonexistent_key")
        assertEquals("Should return empty set for missing key", emptySet<String>(), result)
    }

    @Test
    fun `breathing phase starts with inhale`() {
        val viewModel = createEmptyViewModel()
        assertEquals("Should start with INHALE phase", BreathingPhaseEnum.INHALE, viewModel.uiState.value.breathingPhaseEnum)
    }

    @Test
    fun `initial state has empty answers`() {
        val viewModel = createEmptyViewModel()
        assertEquals("Initial answers should be empty", emptyMap<String, Any>(), viewModel.uiState.value.answers)
    }

    @Test
    fun `initial state has zero cycle count`() {
        val viewModel = createEmptyViewModel()
        assertEquals("Initial cycle count should be 0", 0, viewModel.uiState.value.breathingCycleCount)
    }

    @Test
    fun `get answer as string returns actual value`() {
        val viewModel = createEmptyViewModel()
        viewModel.updateAnswer("test_string", "hello")
        val result = viewModel.getAnswerAsString("test_string")
        assertEquals("Should return actual string value", "hello", result)
    }

    @Test
    fun `get answer as set returns actual value`() {
        val viewModel = createEmptyViewModel()
        val testSet = setOf("a", "b", "c")
        viewModel.updateAnswer("test_set", testSet)
        val result = viewModel.getAnswerAsSet("test_set")
        assertEquals("Should return actual set value", testSet, result)
    }

    @Test
    fun `on next advances step index`() {
        val viewModel = createViewModelWithProfile(TreatmentProfile.TCC)
        assertEquals("Should start at step 0", 0, viewModel.uiState.value.currentStepIndex)
        viewModel.onNext(5)
        assertEquals("Should advance to step 1", 1, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `on next with intensity step does not save answer`() {
        val viewModel = createViewModelWithProfile(TreatmentProfile.TCC)
        viewModel.onNext(5)
        assertEquals("IntensityStep answer should not be saved", emptyMap<String, Any>(), viewModel.uiState.value.answers)
    }

    @Test
    fun `on next saves answer for non intensity step`() {
        val viewModel = createViewModelWithProfile(TreatmentProfile.TCC)
        viewModel.onNext(5)
        val bodyLocations = setOf("chest", "neck")
        viewModel.onNext(bodyLocations)
        val savedAnswer = viewModel.getAnswer(AnswerKey.BODY_LOCATIONS.value)
        assertEquals("BodyLocationStep answer should be saved", bodyLocations, savedAnswer)
    }

    @Test
    fun `on back does nothing at step zero`() {
        val viewModel = createEmptyViewModel()
        viewModel.onBack()
        assertEquals("Should remain at step 0", 0, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `on back decreases step index`() {
        val viewModel = createViewModelWithProfile(TreatmentProfile.TCC)
        viewModel.onNext(5)
        assertEquals("Should be at step 1", 1, viewModel.uiState.value.currentStepIndex)
        viewModel.onBack()
        assertEquals("Should return to step 0", 0, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun `on next with post surf intensity at threshold loops back`() {
        val viewModel = createViewModelWithProfile(TreatmentProfile.TCC)
        val loopCountBefore = viewModel.uiState.value.loopCount
        val urgeSurfIndex =
            viewModel.uiState.value.allSteps
                .indexOfFirst { it is HelpInterventionStep.UrgeSurfingStep }

        viewModel.onNext(5)
        viewModel.onNext(setOf("chest"))
        viewModel.onNext(true)
        viewModel.onNext(8)

        assertEquals("Should loop back to UrgeSurfingStep", urgeSurfIndex, viewModel.uiState.value.currentStepIndex)
        assertEquals("Loop count should increment", loopCountBefore + 1, viewModel.uiState.value.loopCount)
        assertEquals("Meditation index should reset", 0, viewModel.uiState.value.meditationTextIndex)
    }

    @Test
    fun `on next with post surf intensity below threshold advances`() {
        val viewModel = createViewModelWithProfile(TreatmentProfile.TCC)
        val loopCountBefore = viewModel.uiState.value.loopCount

        viewModel.onNext(5)
        viewModel.onNext(setOf("chest"))
        viewModel.onNext(true)
        viewModel.onNext(4)

        assertEquals("Should advance normally", 4, viewModel.uiState.value.currentStepIndex)
        assertEquals("Loop count should not change", loopCountBefore, viewModel.uiState.value.loopCount)
    }

    @Test
    fun `final reflection only saves after answer is provided`() {
        val viewModel = createViewModelWithProfile(TreatmentProfile.TCC)

        advanceToTimerStep(viewModel)
        assertEquals(0, emiRepository.insertInterventionRecordArgs.size)

        viewModel.onNext(true)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isComplete)
        assertEquals(viewModel.uiState.value.allSteps.lastIndex, viewModel.uiState.value.currentStepIndex)
        assertEquals(0, emiRepository.insertInterventionRecordArgs.size)

        viewModel.onNext("no")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isComplete)
        assertEquals(viewModel.uiState.value.allSteps.lastIndex, viewModel.uiState.value.currentStepIndex)
        assertEquals(1, emiRepository.insertInterventionRecordArgs.size)
        assertTrue(emiRepository.insertInterventionRecordArgs.first().wasCompleted)
    }

    @Test
    fun `advance meditation step on non urge surfing step stays at zero`() {
        val viewModel = createViewModelWithProfile(TreatmentProfile.TCC)
        viewModel.advanceMeditationStep()
        assertEquals("Meditation index should stay 0 for non-UrgeSurfing step", 0, viewModel.uiState.value.meditationTextIndex)
    }

    @Test
    fun `start breathing timer is idempotent`() {
        val viewModel = createEmptyViewModel()
        val secondsBeforeFirstCall = viewModel.uiState.value.breathingSecondsLeft
        viewModel.startBreathingTimer()
        viewModel.startBreathingTimer()

        testDispatcher.scheduler.advanceUntilIdle()
        ticker.flow.tryEmit(Unit)
        testDispatcher.scheduler.advanceUntilIdle()

        val secondsAfter = viewModel.uiState.value.breathingSecondsLeft
        assertEquals("Seconds should decrease by 1, not 2 (idempotent)", secondsBeforeFirstCall - 1, secondsAfter)
    }

    @Test
    fun `breathing phase decreases seconds on tick`() {
        val viewModel = createEmptyViewModel()
        val secondsBeforeStart = BreathingPhaseEnum.INHALE.durationSeconds
        viewModel.startBreathingTimer()

        testDispatcher.scheduler.advanceUntilIdle()
        ticker.flow.tryEmit(Unit)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Seconds should decrease by 1", secondsBeforeStart - 1, viewModel.uiState.value.breathingSecondsLeft)
    }

    @Test
    fun `breathing phase transitions inhale to hold`() {
        val viewModel = createEmptyViewModel()
        viewModel.startBreathingTimer()
        testDispatcher.scheduler.advanceUntilIdle()

        repeat(BreathingPhaseEnum.INHALE.durationSeconds) {
            ticker.flow.tryEmit(Unit)
            testDispatcher.scheduler.advanceUntilIdle()
        }

        assertEquals("Should transition to HOLD", BreathingPhaseEnum.HOLD, viewModel.uiState.value.breathingPhaseEnum)
        assertEquals(
            "HOLD should have correct duration",
            BreathingPhaseEnum.HOLD.durationSeconds,
            viewModel.uiState.value.breathingSecondsLeft,
        )
    }

    @Test
    fun `breathing cycle count increments on full cycle`() {
        val viewModel = createEmptyViewModel()
        viewModel.startBreathingTimer()
        testDispatcher.scheduler.advanceUntilIdle()

        val totalTicks =
            BreathingPhaseEnum.INHALE.durationSeconds + BreathingPhaseEnum.HOLD.durationSeconds + BreathingPhaseEnum.EXHALE.durationSeconds
        repeat(totalTicks) {
            ticker.flow.tryEmit(Unit)
            testDispatcher.scheduler.advanceUntilIdle()
        }

        assertEquals("Cycle count should be 1", 1, viewModel.uiState.value.breathingCycleCount)
        assertEquals("Should return to INHALE", BreathingPhaseEnum.INHALE, viewModel.uiState.value.breathingPhaseEnum)
    }

    @Test
    fun `load data with valid profile populates steps`() {
        val viewModel = createViewModelWithProfile(TreatmentProfile.TCC)
        assertEquals("Should complete loading", false, viewModel.uiState.value.isLoading)
        assertEquals(
            "Should populate steps",
            true,
            viewModel.uiState.value.allSteps
                .isNotEmpty(),
        )
    }

    @Test
    fun `step answer key body location step returns body locations key`() {
        val step = HelpInterventionStep.BodyLocationStep(titleKey = "test", options = emptyList())
        assertEquals("Should map to BODY_LOCATIONS", AnswerKey.BODY_LOCATIONS.value, stepAnswerKey(step))
    }

    @Test
    fun `step answer key urge surfing step returns urge surfing key`() {
        val step = HelpInterventionStep.UrgeSurfingStep(meditationStepKeys = emptyList())
        assertEquals("Should map to URGE_SURFING", AnswerKey.URGE_SURFING.value, stepAnswerKey(step))
    }

    private fun createEmptyViewModel(): HelpInterventionViewModel =
        HelpInterventionViewModel(
            onboardingRepository = onboardingRepository,
            userProfileRepository = userProfileRepository,
            saveInterventionSessionUseCase = saveInterventionSessionUseCase,
            calculateAndSaveRiskAssessmentUseCase = noOpCalculateRiskUseCase(),
            ticker = ticker,
            logger = TestLogger,
        )

    private fun createViewModelWithProfile(profile: TreatmentProfile): HelpInterventionViewModel {
        val vm =
            HelpInterventionViewModel(
                onboardingRepository = OnboardingRepositoryMock().apply { session = onboardingSession(profile) },
                userProfileRepository =
                    UserProfileRepositoryMock().apply {
                        profiles = listOf(UserProfile(id = 1, profileName = "Test", createdAt = 0L, updatedAt = 0L))
                    },
                saveInterventionSessionUseCase = saveInterventionSessionUseCase,
                calculateAndSaveRiskAssessmentUseCase = noOpCalculateRiskUseCase(),
                ticker = ticker,
                logger = TestLogger,
            )
        testDispatcher.scheduler.advanceUntilIdle()
        return vm
    }

    private fun onboardingSession(profile: TreatmentProfile) =
        UserOnboardingSession(
            userProfileId = 1,
            clinicalApproach = profile.name,
            ppcsScore = null,
            pgsiScore = null,
            moralIncongruenceScore = null,
            frequencyScore = null,
            moralDisapprovalScore = null,
            hasNeurodivergence = false,
            createdAt = 0L,
        )

    private fun noOpCalculateRiskUseCase(): CalculateAndSaveRiskAssessmentUseCase =
        CalculateAndSaveRiskAssessmentUseCase(
            metricsRepository = MetricsRepositoryMock(),
            riskWeightsRepository = RiskWeightsRepositoryMock(),
        )

    private fun advanceToTimerStep(viewModel: HelpInterventionViewModel) {
        while (viewModel.uiState.value.allSteps
                .getOrNull(viewModel.uiState.value.currentStepIndex) !is HelpInterventionStep.TimerStep
        ) {
            val currentStep = viewModel.uiState.value.allSteps[viewModel.uiState.value.currentStepIndex]
            viewModel.onNext(answerForStep(currentStep))
            testDispatcher.scheduler.advanceUntilIdle()
        }
    }

    private fun answerForStep(step: HelpInterventionStep): Any =
        when (step) {
            is HelpInterventionStep.IntensityStep -> 5
            is HelpInterventionStep.BodyLocationStep -> setOf("chest")
            is HelpInterventionStep.UrgeSurfingStep -> true
            is HelpInterventionStep.PostSurfIntensityStep -> 4
            is HelpInterventionStep.ActValuesStep -> step.predefinedOptions.firstOrNull()?.id ?: "faith"
            is HelpInterventionStep.ActDirectionStep -> step.options.firstOrNull()?.id ?: "toward"
            is HelpInterventionStep.ActCommittedActionStep -> step.suggestions.firstOrNull()?.labelKey ?: "action"
            is HelpInterventionStep.TccAutomaticThoughtStep -> "thought"
            is HelpInterventionStep.TccEvidenceForStep -> "for"
            is HelpInterventionStep.TccEvidenceAgainstStep -> "against"
            is HelpInterventionStep.TccAlternativeThoughtStep -> "alternative"
            is HelpInterventionStep.TccActionStep -> step.suggestions.firstOrNull()?.labelKey ?: "action"
            is HelpInterventionStep.TimerStep -> true
            is HelpInterventionStep.ReflectionStep -> "yes"
        }
}
