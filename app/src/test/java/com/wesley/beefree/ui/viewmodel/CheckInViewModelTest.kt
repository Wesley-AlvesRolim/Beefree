package com.wesley.beefree.ui.viewmodel

import com.wesley.beefree.domain.checkin.ActivityType
import com.wesley.beefree.domain.checkin.DailyCheckInAnswer
import com.wesley.beefree.domain.entities.AddictionCategory
import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.entities.UserAddiction
import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.domain.entities.UserHobby
import com.wesley.beefree.domain.entities.UserObjective
import com.wesley.beefree.domain.entities.UserOnboardingSession
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.entities.UserSymptom
import com.wesley.beefree.domain.entities.WeeklyCheckIn
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.repository.ports.AddictionRepository
import com.wesley.beefree.domain.repository.ports.CheckInRepository
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import com.wesley.beefree.domain.repository.ports.OnboardingRepository
import com.wesley.beefree.domain.repository.ports.RiskWeightsRepository
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
import com.wesley.beefree.domain.treatments.checkin.ActDailyCheckInFlow
import com.wesley.beefree.domain.usecases.checkin.DetermineCheckInTypeUseCase
import com.wesley.beefree.domain.usecases.checkin.HasCompletedTodaysCheckInUseCase
import com.wesley.beefree.domain.usecases.checkin.LoadDailyCheckInFlowUseCase
import com.wesley.beefree.domain.usecases.checkin.LoadPreviousObjectiveUseCase
import com.wesley.beefree.domain.usecases.checkin.LoadTodaysEmotionRecordUseCase
import com.wesley.beefree.domain.usecases.checkin.SaveDailyCheckInUseCase
import com.wesley.beefree.domain.usecases.checkin.SelectTherapeuticActivityUseCase
import com.wesley.beefree.domain.usecases.risk.CalculateAndSaveRiskAssessmentUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
            val repository = MutableMetricsRepository()
            val viewModel = createViewModel(metricsRepository = repository, ioDispatcher = dispatcher)

            advanceUntilIdle()
            assertEquals(false, viewModel.hasEmotionalRecordToday.value)

            repository.records =
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
                        StubOnboardingRepository(
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
                                ),
                        ),
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
    fun `saves check-in when all steps are completed`() =
        runTest {
            val dispatcher = StandardTestDispatcher(testScheduler)
            Dispatchers.setMain(dispatcher)
            val spyRepo = SpyCheckInRepository()
            val viewModel =
                createViewModel(
                    ioDispatcher = dispatcher,
                    onboardingRepository =
                        StubOnboardingRepository(
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
                                ),
                        ),
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
            assertTrue(spyRepo.insertCalled)
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
        metricsRepository: MetricsRepository = StubMetricsRepository(),
        onboardingRepository: OnboardingRepository = StubOnboardingRepository(),
        checkInRepository: CheckInRepository = StubCheckInRepository(),
    ): CheckInViewModel =
        CheckInViewModel(
            userProfileRepository = StubUserProfileRepository(),
            onboardingRepository = onboardingRepository,
            calculateAndSaveRiskAssessmentUseCase = noOpCalculateRiskUseCase(),
            loadTodaysEmotionRecordUseCase = LoadTodaysEmotionRecordUseCase(metricsRepository),
            hasCompletedTodaysCheckInUseCase =
                HasCompletedTodaysCheckInUseCase(
                    checkInRepository = checkInRepository,
                    determineCheckInTypeUseCase = DetermineCheckInTypeUseCase(),
                ),
            loadPreviousObjectiveUseCase = LoadPreviousObjectiveUseCase(checkInRepository),
            loadDailyCheckInFlowUseCase = LoadDailyCheckInFlowUseCase(),
            selectTherapeuticActivityUseCase = SelectTherapeuticActivityUseCase(checkInRepository),
            saveDailyCheckInUseCase =
                SaveDailyCheckInUseCase(
                    checkInRepository = checkInRepository,
                    addictionRepository = StubAddictionRepository(),
                ),
            ioDispatcher = ioDispatcher,
            logger = noOpLogger(),
        )

    private fun noOpLogger() =
        object : com.wesley.beefree.infrastructure.logging.Logger {
            override fun d(
                tag: String,
                message: String,
            ) = Unit

            override fun info(
                tag: String,
                message: String,
            ) = Unit

            override fun e(
                tag: String,
                message: String,
                throwable: Throwable?,
            ) = Unit
        }

    private fun noOpCalculateRiskUseCase(): CalculateAndSaveRiskAssessmentUseCase {
        val stubMetrics =
            object : com.wesley.beefree.domain.repository.ports.MetricsRepository {
                override suspend fun insertEmotionRecord(record: com.wesley.beefree.domain.entities.EmotionRecord): Long = 0L

                override suspend fun deleteEmotionRecordsByIds(ids: List<Long>) = Unit

                override fun getEmotionRecords(
                    userId: Int,
                ): kotlinx.coroutines.flow.Flow<List<com.wesley.beefree.domain.entities.EmotionRecord>> =
                    kotlinx.coroutines.flow.emptyFlow()

                override fun getEmotionRecordsByType(
                    userId: Int,
                    feelingType: com.wesley.beefree.domain.entities.FeelingType,
                ): kotlinx.coroutines.flow.Flow<List<com.wesley.beefree.domain.entities.EmotionRecord>> =
                    kotlinx.coroutines.flow.emptyFlow()

                override suspend fun getLatestEmotionRecord(userId: Int): com.wesley.beefree.domain.entities.EmotionRecord? = null

                override suspend fun insertRiskFeatureSnapshot(snapshot: com.wesley.beefree.domain.entities.RiskFeatureSnapshot): Long = 0L

                override fun getRiskFeatureSnapshots(
                    userId: Int,
                ): kotlinx.coroutines.flow.Flow<List<com.wesley.beefree.domain.entities.RiskFeatureSnapshot>> =
                    kotlinx.coroutines.flow.emptyFlow()

                override suspend fun getLatestRiskFeatureSnapshot(userId: Int): com.wesley.beefree.domain.entities.RiskFeatureSnapshot? =
                    null

                override suspend fun insertRiskAssessment(assessment: com.wesley.beefree.domain.entities.RiskAssessment): Long = 0L

                override suspend fun deleteAllRiskAssessmentsForUser(userId: Int) = Unit

                override fun getRiskAssessments(
                    userId: Int,
                ): kotlinx.coroutines.flow.Flow<List<com.wesley.beefree.domain.entities.RiskAssessment>> =
                    kotlinx.coroutines.flow.emptyFlow()
            }
        val stubWeights =
            object : RiskWeightsRepository {
                override fun getWeights(userId: Int) =
                    com.wesley.beefree.domain.risk
                        .RiskWeights()

                override fun saveWeights(
                    userId: Int,
                    weights: com.wesley.beefree.domain.risk.RiskWeights,
                ) = Unit
            }
        return CalculateAndSaveRiskAssessmentUseCase(stubMetrics, stubWeights)
    }

    private class StubCheckInRepository : CheckInRepository {
        override suspend fun insertDailyCheckIn(checkIn: DailyCheckIn): Long = 0

        override fun getDailyCheckIns(userId: Int): Flow<List<DailyCheckIn>> = emptyFlow()

        override suspend fun insertWeeklyCheckIn(checkIn: WeeklyCheckIn): Long = 0

        override fun getWeeklyCheckIns(userId: Int): Flow<List<WeeklyCheckIn>> = emptyFlow()
    }

    private class SpyCheckInRepository : CheckInRepository {
        var insertCalled = false

        override suspend fun insertDailyCheckIn(checkIn: DailyCheckIn): Long {
            insertCalled = true
            return 0L
        }

        override fun getDailyCheckIns(userId: Int): Flow<List<DailyCheckIn>> = emptyFlow()

        override suspend fun insertWeeklyCheckIn(checkIn: WeeklyCheckIn): Long = 0

        override fun getWeeklyCheckIns(userId: Int): Flow<List<WeeklyCheckIn>> = emptyFlow()
    }

    private class StubAddictionRepository : AddictionRepository {
        override suspend fun insertAddictionCategory(category: AddictionCategory): Long = 0

        override suspend fun updateAddictionCategory(category: AddictionCategory) {}

        override suspend fun deleteAddictionCategory(category: AddictionCategory) {}

        override suspend fun getAddictionCategoryById(id: Int): AddictionCategory? = null

        override fun getAllAddictionCategories() = emptyFlow<List<AddictionCategory>>()

        override suspend fun insertRelapse(relapse: RelapseRecord): Long = 0

        override fun getRelapseHistory() = emptyFlow<List<RelapseRecord>>()
    }

    private class StubUserProfileRepository : UserProfileRepository {
        override suspend fun insertProfile(profile: UserProfile): Long = 0

        override suspend fun updateProfile(profile: UserProfile) {}

        override suspend fun getProfileById(id: Int): UserProfile? = null

        override fun getAllProfiles(): Flow<List<UserProfile>> =
            flowOf(listOf(UserProfile(id = 1, profileName = "Test", createdAt = 0L, updatedAt = 0L)))

        override suspend fun associateAddictionToProfile(userAddiction: UserAddiction): Long = 0

        override suspend fun removeAddictionFromProfile(userAddiction: UserAddiction) {}

        override fun getAddictionsByUserId(userId: Int): Flow<List<UserAddiction>> = emptyFlow()
    }

    private class StubMetricsRepository : MetricsRepository {
        var records: List<EmotionRecord> = emptyList()

        override suspend fun insertEmotionRecord(record: EmotionRecord): Long = 0L

        override fun getEmotionRecords(userId: Int): Flow<List<EmotionRecord>> = flowOf(records)

        override fun getEmotionRecordsByType(
            userId: Int,
            feelingType: FeelingType,
        ): Flow<List<EmotionRecord>> = flowOf(records.filter { it.feelingType == feelingType })

        override suspend fun getLatestEmotionRecord(userId: Int): EmotionRecord? = null

        override suspend fun deleteEmotionRecordsByIds(ids: List<Long>) = Unit

        override suspend fun insertRiskFeatureSnapshot(snapshot: RiskFeatureSnapshot): Long = 0L

        override fun getRiskFeatureSnapshots(userId: Int): Flow<List<RiskFeatureSnapshot>> = emptyFlow()

        override suspend fun getLatestRiskFeatureSnapshot(userId: Int): RiskFeatureSnapshot? = null

        override suspend fun insertRiskAssessment(assessment: RiskAssessment): Long = 0L

        override suspend fun deleteAllRiskAssessmentsForUser(userId: Int) = Unit

        override fun getRiskAssessments(userId: Int): Flow<List<RiskAssessment>> = emptyFlow()
    }

    private class MutableMetricsRepository : MetricsRepository {
        var records: List<EmotionRecord> = emptyList()

        override suspend fun insertEmotionRecord(record: EmotionRecord): Long = 0L

        override fun getEmotionRecords(userId: Int): Flow<List<EmotionRecord>> = flowOf(records)

        override fun getEmotionRecordsByType(
            userId: Int,
            feelingType: FeelingType,
        ): Flow<List<EmotionRecord>> = flowOf(records.filter { it.feelingType == feelingType })

        override suspend fun getLatestEmotionRecord(userId: Int): EmotionRecord? = null

        override suspend fun deleteEmotionRecordsByIds(ids: List<Long>) = Unit

        override suspend fun insertRiskFeatureSnapshot(snapshot: RiskFeatureSnapshot): Long = 0L

        override fun getRiskFeatureSnapshots(userId: Int): Flow<List<RiskFeatureSnapshot>> = emptyFlow()

        override suspend fun getLatestRiskFeatureSnapshot(userId: Int): RiskFeatureSnapshot? = null

        override suspend fun insertRiskAssessment(assessment: RiskAssessment): Long = 0L

        override suspend fun deleteAllRiskAssessmentsForUser(userId: Int) = Unit

        override fun getRiskAssessments(userId: Int): Flow<List<RiskAssessment>> = emptyFlow()
    }

    private class StubOnboardingRepository(
        private val session: UserOnboardingSession? = null,
    ) : OnboardingRepository {
        override suspend fun insertOnboardingSession(session: UserOnboardingSession): Long = 0

        override suspend fun getOnboardingSession(userId: Int): UserOnboardingSession? = session

        override suspend fun insertCoreValue(value: UserCoreValue): Long = 0

        override suspend fun deleteCoreValue(value: UserCoreValue) {}

        override fun getCoreValues(userId: Int): Flow<List<UserCoreValue>> = emptyFlow()

        override suspend fun insertHobby(hobby: UserHobby): Long = 0

        override suspend fun deleteHobby(hobby: UserHobby) {}

        override fun getHobbies(userId: Int) = emptyFlow<List<UserHobby>>()

        override suspend fun insertObjective(objective: UserObjective): Long = 0

        override suspend fun deleteObjective(objective: UserObjective) {}

        override fun getObjectives(userId: Int) = emptyFlow<List<UserObjective>>()

        override suspend fun insertSymptom(symptom: UserSymptom): Long = 0

        override suspend fun deleteSymptom(symptom: UserSymptom) {}

        override fun getSymptoms(userId: Int) = emptyFlow<List<UserSymptom>>()
    }
}
