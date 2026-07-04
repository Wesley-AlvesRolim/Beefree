package com.wesley.beefree.ui.viewmodel

import com.wesley.beefree.domain.checkin.ActivityType
import com.wesley.beefree.domain.checkin.DailyCheckInAnswer
import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.UserOnboardingSession
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.mocks.AddictionRepositoryMock
import com.wesley.beefree.domain.mocks.CheckInRepositoryMock
import com.wesley.beefree.domain.mocks.MetricsRepositoryMock
import com.wesley.beefree.domain.mocks.OnboardingRepositoryMock
import com.wesley.beefree.domain.mocks.RiskWeightsRepositoryMock
import com.wesley.beefree.domain.mocks.UserProfileRepositoryMock
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.treatments.checkin.ActDailyCheckInFlow
import com.wesley.beefree.domain.usecases.checkin.HasCompletedTodaysCheckInUseCase
import com.wesley.beefree.domain.usecases.checkin.LoadDailyCheckInFlowUseCase
import com.wesley.beefree.domain.usecases.checkin.LoadPreviousObjectiveUseCase
import com.wesley.beefree.domain.usecases.checkin.LoadTodaysEmotionRecordUseCase
import com.wesley.beefree.domain.usecases.checkin.SaveDailyCheckInUseCase
import com.wesley.beefree.domain.usecases.checkin.SelectTherapeuticActivityUseCase
import com.wesley.beefree.domain.usecases.risk.CalculateAndSaveRiskAssessmentUseCase
import com.wesley.beefree.infrastructure.logging.TestLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CheckInViewModelTest {
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `start emotion record emits a navigation event`() =
        runTest {
            val dispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(dispatcher)
            val viewModel = createViewModel(ioDispatcher = dispatcher)
            val emittedEvent = async { viewModel.navigationEvents.first() }

            advanceUntilIdle()
            viewModel.startEmotionRecord()
            advanceUntilIdle()

            assertEquals(CheckInNavigationDestination.EmotionalRecord, emittedEvent.await())
        }

    @Test
    fun `on return from emotional record refreshes today's summary`() =
        runTest {
            val dispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(dispatcher)
            val repository = MetricsRepositoryMock()
            val viewModel = createViewModel(metricsRepository = repository, ioDispatcher = dispatcher)

            advanceUntilIdle()
            assertEquals(false, viewModel.hasEmotionalRecordToday.value)

            repository.emotionRecords =
                listOf(
                    record(FeelingType.SLEEP, 8, System.currentTimeMillis()),
                    record(FeelingType.CRAVING, 4, System.currentTimeMillis()),
                    record(FeelingType.BOREDOM, 2, System.currentTimeMillis()),
                    record(FeelingType.STRESS, 6, System.currentTimeMillis()),
                    record(FeelingType.LONELINESS, 7, System.currentTimeMillis()),
                    record(FeelingType.FATIGUE, 5, System.currentTimeMillis()),
                )

            viewModel.onReturnFromEmotionalRecord()
            advanceUntilIdle()

            assertEquals(true, viewModel.hasEmotionalRecordToday.value)
            assertEquals(8, viewModel.todaysEmotionRecord.value?.getValue(FeelingType.SLEEP))
            assertEquals(4, viewModel.todaysEmotionRecord.value?.getValue(FeelingType.CRAVING))
        }

    @Test
    fun `reset daily flow state clears transient check in progress`() =
        runTest {
            val dispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(dispatcher)
            val viewModel =
                createViewModel(
                    ioDispatcher = dispatcher,
                    onboardingRepository =
                        OnboardingRepositoryMock().apply {
                            session =
                                UserOnboardingSession(
                                    id = 1,
                                    userProfileId = 1,
                                    clinicalApproach = TreatmentProfile.ACT.name,
                                    ppcsScore = null,
                                    pgsiScore = null,
                                    moralIncongruenceScore = null,
                                    frequencyScore = null,
                                    moralDisapprovalScore = null,
                                    hasNeurodivergence = false,
                                    createdAt = 0L,
                                )
                        },
                )

            advanceUntilIdle()
            viewModel.startDailyFlow()
            viewModel.setDailyAnswer("step-1", DailyCheckInAnswer.Text("value"))
            viewModel.selectActivity(ActivityType.VIDEO)

            viewModel.resetDailyFlowState()

            assertEquals(DailyCheckInPhase.INVITE, viewModel.dailyPhase.value)
            assertEquals(emptyMap<String, DailyCheckInAnswer>(), viewModel.dailyAnswers.value)
            assertEquals(emptyList<String>(), viewModel.dailyVisitedSteps.value)
            assertNull(viewModel.selectedActivity.value)
        }

    @Test
    fun `isCompleted is false when check-in was done on a different day`() =
        runTest {
            val dispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(dispatcher)
            val yesterday = System.currentTimeMillis() - 86_400_000L
            val repo =
                CheckInRepositoryMock().apply {
                    dailyCheckIns =
                        listOf(
                            DailyCheckIn(
                                userProfileId = 1,
                                treatmentProfile = TreatmentProfile.ACT,
                                answers = emptyMap(),
                                checkedInAt = yesterday,
                            ),
                        )
                }
            val viewModel = createViewModel(ioDispatcher = dispatcher, checkInRepository = repo)

            advanceUntilIdle()

            assertEquals(false, viewModel.isCompleted.value)
        }

    @Test
    fun `pre-selected activity is added to answers on init so next button is enabled`() =
        runTest {
            val dispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(dispatcher)
            val viewModel =
                createViewModel(
                    ioDispatcher = dispatcher,
                    onboardingRepository =
                        OnboardingRepositoryMock().apply {
                            session =
                                UserOnboardingSession(
                                    id = 1,
                                    userProfileId = 1,
                                    clinicalApproach = TreatmentProfile.ACT.name,
                                    ppcsScore = null,
                                    pgsiScore = null,
                                    moralIncongruenceScore = null,
                                    frequencyScore = null,
                                    moralDisapprovalScore = null,
                                    hasNeurodivergence = false,
                                    createdAt = 0L,
                                )
                        },
                )

            advanceUntilIdle()

            val answer = viewModel.dailyAnswers.value[ActDailyCheckInFlow.STEP_THERAPEUTIC_ACTIVITY]
            assertTrue(answer is DailyCheckInAnswer.TherapeuticActivity)
        }

    @Test
    fun `isCompleted is true when check-in was done today`() =
        runTest {
            val dispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(dispatcher)
            val repo =
                CheckInRepositoryMock().apply {
                    dailyCheckIns =
                        listOf(
                            DailyCheckIn(
                                userProfileId = 1,
                                treatmentProfile = TreatmentProfile.ACT,
                                answers = emptyMap(),
                                checkedInAt = System.currentTimeMillis(),
                            ),
                        )
                }
            val viewModel = createViewModel(ioDispatcher = dispatcher, checkInRepository = repo)

            advanceUntilIdle()

            assertEquals(true, viewModel.isCompleted.value)
        }

    @Test
    fun `saves check-in when all steps are completed`() =
        runTest {
            val dispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(dispatcher)
            val spyRepo = CheckInRepositoryMock()
            val viewModel =
                createViewModel(
                    ioDispatcher = dispatcher,
                    onboardingRepository =
                        OnboardingRepositoryMock().apply {
                            session =
                                UserOnboardingSession(
                                    id = 1,
                                    userProfileId = 1,
                                    clinicalApproach = TreatmentProfile.ACT.name,
                                    ppcsScore = null,
                                    pgsiScore = null,
                                    moralIncongruenceScore = null,
                                    frequencyScore = null,
                                    moralDisapprovalScore = null,
                                    hasNeurodivergence = false,
                                    createdAt = 0L,
                                )
                        },
                    checkInRepository = spyRepo,
                )
            advanceUntilIdle()

            viewModel.startDailyFlow()

            viewModel.setDailyAnswer(ActDailyCheckInFlow.STEP_EMOTIONAL_RECORD, DailyCheckInAnswer.EmotionalRecord(alreadyDone = false))
            viewModel.nextDailyStep()

            viewModel.selectActivity(ActivityType.MINDFULNESS)
            viewModel.nextDailyStep()

            viewModel.setDailyAnswer(ActDailyCheckInFlow.STEP_MINDFULNESS, DailyCheckInAnswer.Mindfulness)
            viewModel.nextDailyStep()

            viewModel.setDailyAnswer(ActDailyCheckInFlow.STEP_GOAL_DEFINITION, DailyCheckInAnswer.TextWithSuggestions("meta"))
            viewModel.nextDailyStep()

            viewModel.setDailyAnswer(ActDailyCheckInFlow.STEP_RELAPSE_CHECK, DailyCheckInAnswer.Bool(false))
            viewModel.nextDailyStep()

            viewModel.setDailyAnswer(ActDailyCheckInFlow.STEP_DAY_EVAL, DailyCheckInAnswer.SingleSelectWithContext("calm"))
            viewModel.nextDailyStep()

            advanceUntilIdle()

            assertEquals(DailyCheckInPhase.DONE, viewModel.dailyPhase.value)
            assertTrue(spyRepo.insertDailyCheckInArgs.isNotEmpty())
        }

    private fun record(
        feelingType: FeelingType,
        intensity: Int,
        createdAt: Long,
        userProfileId: Int = 1,
    ) = EmotionRecord(
        userProfileId = userProfileId,
        feelingType = feelingType,
        intensity = intensity,
        createdAt = createdAt,
    )

    private fun createViewModel(
        ioDispatcher: CoroutineDispatcher,
        metricsRepository: MetricsRepositoryMock = MetricsRepositoryMock(),
        onboardingRepository: OnboardingRepositoryMock = OnboardingRepositoryMock(),
        checkInRepository: CheckInRepositoryMock = CheckInRepositoryMock(),
    ): CheckInViewModel =
        CheckInViewModel(
            userProfileRepository =
                UserProfileRepositoryMock().apply {
                    profiles = listOf(UserProfile(id = 1, profileName = "Test", createdAt = 0L, updatedAt = 0L))
                },
            onboardingRepository = onboardingRepository,
            calculateAndSaveRiskAssessmentUseCase = noOpCalculateRiskUseCase(),
            loadTodaysEmotionRecordUseCase = LoadTodaysEmotionRecordUseCase(metricsRepository),
            hasCompletedTodaysCheckInUseCase =
                HasCompletedTodaysCheckInUseCase(
                    checkInRepository = checkInRepository,
                ),
            loadPreviousObjectiveUseCase = LoadPreviousObjectiveUseCase(checkInRepository),
            loadDailyCheckInFlowUseCase = LoadDailyCheckInFlowUseCase(),
            selectTherapeuticActivityUseCase = SelectTherapeuticActivityUseCase(checkInRepository),
            saveDailyCheckInUseCase =
                SaveDailyCheckInUseCase(
                    checkInRepository = checkInRepository,
                    addictionRepository = AddictionRepositoryMock(),
                ),
            ioDispatcher = ioDispatcher,
            logger = TestLogger,
        )

    private fun noOpCalculateRiskUseCase(): CalculateAndSaveRiskAssessmentUseCase =
        CalculateAndSaveRiskAssessmentUseCase(
            metricsRepository = MetricsRepositoryMock(),
            riskWeightsRepository = RiskWeightsRepositoryMock(),
        )
}
