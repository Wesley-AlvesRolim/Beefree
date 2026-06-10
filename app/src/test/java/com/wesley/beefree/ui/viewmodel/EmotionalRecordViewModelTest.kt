package com.wesley.beefree.ui.viewmodel

import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.entities.UserAddiction
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import com.wesley.beefree.domain.repository.ports.RiskFeatureSnapshotRepository
import com.wesley.beefree.domain.repository.ports.RiskWeightsRepository
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
import com.wesley.beefree.domain.risk.RiskWeights
import com.wesley.beefree.domain.usecases.emotion.SaveEmotionRecordUseCase
import com.wesley.beefree.domain.usecases.risk.CalculateAndSaveRiskAssessmentUseCase
import com.wesley.beefree.domain.usecases.risk.SaveRiskFeatureSnapshotUseCase
import com.wesley.beefree.infrastructure.logging.TestLogger
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EmotionalRecordViewModelTest {
    companion object {
        const val DEFAULT_USER_ID = 12
    }

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        TestLogger.clear()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `slider changes update the matching field`() =
        runTest {
            val viewModel = createViewModel()
            val sleep = 8f
            val fatigue = 3f
            val initialStress =
                viewModel.uiState.value.emotions
                    .getValue(FeelingType.STRESS)

            viewModel.onSliderChange(FeelingType.SLEEP, sleep)
            viewModel.onSliderChange(FeelingType.FATIGUE, fatigue)

            assertEquals(
                sleep,
                viewModel.uiState.value.emotions
                    .getValue(FeelingType.SLEEP),
                0f,
            )
            assertEquals(
                fatigue,
                viewModel.uiState.value.emotions
                    .getValue(FeelingType.FATIGUE),
                0f,
            )
            assertEquals(
                initialStress,
                viewModel.uiState.value.emotions
                    .getValue(FeelingType.STRESS),
                0f,
            )
        }

    @Test
    fun `on save stores all emotion records and finishes in done step`() =
        runTest {
            val repository = RecordingMetricsRepository()
            val viewModel = createViewModel(repository = repository)
            val sleep = 8.9f
            val craving = 4.2f
            val boredom = 2.7f
            val stress = 6.1f
            val loneliness = 9.4f
            val fatigue = 5.0f

            viewModel.onSliderChange(FeelingType.SLEEP, sleep)
            viewModel.onSliderChange(FeelingType.CRAVING, craving)
            viewModel.onSliderChange(FeelingType.BOREDOM, boredom)
            viewModel.onSliderChange(FeelingType.STRESS, stress)
            viewModel.onSliderChange(FeelingType.LONELINESS, loneliness)
            viewModel.onSliderChange(FeelingType.FATIGUE, fatigue)

            viewModel.onSave()

            advanceUntilIdle()

            assertEquals(EmotionalRecordStep.DONE, viewModel.uiState.value.step)
            assertFalse(viewModel.uiState.value.isSaving)
            assertNull(viewModel.uiState.value.error)
            assertEquals(6, repository.savedRecords.size)
            assertEquals(FeelingType.SLEEP to sleep.toInt(), repository.savedRecords[0].feelingType to repository.savedRecords[0].intensity)
            assertEquals(
                FeelingType.CRAVING to craving.toInt(),
                repository.savedRecords[1].feelingType to repository.savedRecords[1].intensity,
            )
            assertEquals(
                FeelingType.BOREDOM to boredom.toInt(),
                repository.savedRecords[2].feelingType to repository.savedRecords[2].intensity,
            )
            assertEquals(
                FeelingType.STRESS to stress.toInt(),
                repository.savedRecords[3].feelingType to repository.savedRecords[3].intensity,
            )
            assertEquals(
                FeelingType.LONELINESS to loneliness.toInt(),
                repository.savedRecords[4].feelingType to repository.savedRecords[4].intensity,
            )
            assertEquals(
                FeelingType.FATIGUE to fatigue.toInt(),
                repository.savedRecords[5].feelingType to repository.savedRecords[5].intensity,
            )
        }

    @Test
    fun `on save surfaces repository errors`() =
        runTest {
            val errorMessage = "DB error"
            val repository = RecordingMetricsRepository(throwOnInsert = RuntimeException(errorMessage))
            val viewModel = createViewModel(repository = repository)

            viewModel.onNext()
            viewModel.onSave()
            advanceUntilIdle()

            assertEquals(EmotionalRecordStep.CAPTURE, viewModel.uiState.value.step)
            assertFalse(viewModel.uiState.value.isSaving)
            assertEquals(errorMessage, viewModel.uiState.value.error)
            assertEquals(1, repository.insertCalls)
        }

    @Test
    fun `on save rolls back emotion records when risk calculation fails`() =
        runTest {
            val errorMessage = "Risk query error"
            val repository = RecordingMetricsRepository(throwOnRiskQuery = RuntimeException(errorMessage))
            val viewModel = createViewModel(repository = repository)

            viewModel.onNext()
            viewModel.onSave()
            advanceUntilIdle()

            assertEquals(EmotionalRecordStep.CAPTURE, viewModel.uiState.value.step)
            assertFalse(viewModel.uiState.value.isSaving)
            assertEquals(errorMessage, viewModel.uiState.value.error)
            assertTrue(repository.deletedIds.isNotEmpty())
        }

    @Test
    fun `on back from capture step returns to intro step`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.onNext()

            viewModel.onBack()

            assertEquals(EmotionalRecordStep.INTRO, viewModel.uiState.value.step)
        }

    @Test
    fun `on done emits a navigation event`() =
        runTest {
            val viewModel = createViewModel()
            val emittedEvent = async { viewModel.navigationEvents.first() }

            advanceUntilIdle()
            viewModel.onDone()
            advanceUntilIdle()

            assertEquals(EmotionalRecordNavigationDestination.Done, emittedEvent.await())
        }

    private fun createViewModel(
        userId: Int = DEFAULT_USER_ID,
        repository: MetricsRepository = RecordingMetricsRepository(),
        snapshotRepository: RiskFeatureSnapshotRepository = NoOpRiskFeatureSnapshotRepository(),
    ): EmotionalRecordViewModel =
        EmotionalRecordViewModel(
            userProfileRepository = StubUserProfileRepository(userId),
            saveEmotionRecordUseCase = SaveEmotionRecordUseCase(repository),
            saveRiskFeatureSnapshotUseCase = SaveRiskFeatureSnapshotUseCase(snapshotRepository),
            calculateAndSaveRiskAssessmentUseCase = noOpCalculateRiskUseCase(repository),
            logger = TestLogger,
            ioDispatcher = testDispatcher,
        )

    private class StubUserProfileRepository(
        private val userId: Int,
    ) : UserProfileRepository {
        override suspend fun insertProfile(profile: UserProfile): Long = 0

        override suspend fun updateProfile(profile: UserProfile) {}

        override suspend fun getProfileById(id: Int): UserProfile? = null

        override fun getAllProfiles(): Flow<List<UserProfile>> =
            flowOf(listOf(UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)))

        override suspend fun associateAddictionToProfile(userAddiction: UserAddiction): Long = 0

        override suspend fun removeAddictionFromProfile(userAddiction: UserAddiction) {}

        override fun getAddictionsByUserId(userId: Int): Flow<List<UserAddiction>> = emptyFlow()
    }

    private class RecordingMetricsRepository(
        private val throwOnInsert: Throwable? = null,
        private val throwOnRiskQuery: Throwable? = null,
    ) : MetricsRepository {
        val savedRecords: MutableList<EmotionRecord> = mutableListOf()
        val deletedIds: MutableList<Long> = mutableListOf()
        var insertCalls: Int = 0
            private set

        override suspend fun insertEmotionRecord(record: EmotionRecord): Long {
            insertCalls++
            throwOnInsert?.let { throw it }
            savedRecords += record
            return savedRecords.size.toLong()
        }

        override suspend fun deleteEmotionRecordsByIds(ids: List<Long>) {
            deletedIds += ids
        }

        override fun getEmotionRecords(userId: Int): Flow<List<EmotionRecord>> = emptyFlow()

        override fun getEmotionRecordsByType(
            userId: Int,
            feelingType: FeelingType,
        ): Flow<List<EmotionRecord>> = emptyFlow()

        override suspend fun getLatestEmotionRecord(userId: Int): EmotionRecord? = null

        override suspend fun insertRiskFeatureSnapshot(snapshot: RiskFeatureSnapshot): Long = 0

        override fun getRiskFeatureSnapshots(userId: Int): Flow<List<RiskFeatureSnapshot>> = emptyFlow()

        override suspend fun getLatestRiskFeatureSnapshot(userId: Int): RiskFeatureSnapshot? {
            throwOnRiskQuery?.let { throw it }
            return null
        }

        override suspend fun insertRiskAssessment(assessment: RiskAssessment): Long = 0

        override suspend fun deleteAllRiskAssessmentsForUser(userId: Int) = Unit

        override fun getRiskAssessments(userId: Int): Flow<List<RiskAssessment>> = emptyFlow()
    }

    private class NoOpRiskFeatureSnapshotRepository : RiskFeatureSnapshotRepository {
        override suspend fun save(snapshot: RiskFeatureSnapshot): Long = 0L

        override fun getAllByUser(userId: Int): Flow<List<RiskFeatureSnapshot>> = emptyFlow()

        override suspend fun getLatestByUser(userId: Int): RiskFeatureSnapshot? = null
    }

    private fun noOpCalculateRiskUseCase(repository: MetricsRepository): CalculateAndSaveRiskAssessmentUseCase =
        CalculateAndSaveRiskAssessmentUseCase(
            metricsRepository = repository,
            riskWeightsRepository =
                object : RiskWeightsRepository {
                    override fun getWeights(userId: Int) = RiskWeights()

                    override fun saveWeights(
                        userId: Int,
                        weights: RiskWeights,
                    ) = Unit
                },
        )
}
