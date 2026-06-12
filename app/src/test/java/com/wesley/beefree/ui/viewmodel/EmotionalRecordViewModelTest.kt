package com.wesley.beefree.ui.viewmodel

import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.mocks.MetricsRepositoryMock
import com.wesley.beefree.domain.mocks.RiskFeatureSnapshotRepositoryMock
import com.wesley.beefree.domain.mocks.RiskWeightsRepositoryMock
import com.wesley.beefree.domain.mocks.UserProfileRepositoryMock
import com.wesley.beefree.domain.usecases.emotion.SaveEmotionRecordUseCase
import com.wesley.beefree.domain.usecases.risk.CalculateAndSaveRiskAssessmentUseCase
import com.wesley.beefree.domain.usecases.risk.SaveRiskFeatureSnapshotUseCase
import com.wesley.beefree.infrastructure.logging.TestLogger
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
            val repository = MetricsRepositoryMock()
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
            assertEquals(6, repository.insertEmotionRecordArgs.size)
            assertEquals(
                FeelingType.SLEEP to sleep.toInt(),
                repository.insertEmotionRecordArgs[0].feelingType to repository.insertEmotionRecordArgs[0].intensity,
            )
            assertEquals(
                FeelingType.CRAVING to craving.toInt(),
                repository.insertEmotionRecordArgs[1].feelingType to repository.insertEmotionRecordArgs[1].intensity,
            )
            assertEquals(
                FeelingType.BOREDOM to boredom.toInt(),
                repository.insertEmotionRecordArgs[2].feelingType to repository.insertEmotionRecordArgs[2].intensity,
            )
            assertEquals(
                FeelingType.STRESS to stress.toInt(),
                repository.insertEmotionRecordArgs[3].feelingType to repository.insertEmotionRecordArgs[3].intensity,
            )
            assertEquals(
                FeelingType.LONELINESS to loneliness.toInt(),
                repository.insertEmotionRecordArgs[4].feelingType to repository.insertEmotionRecordArgs[4].intensity,
            )
            assertEquals(
                FeelingType.FATIGUE to fatigue.toInt(),
                repository.insertEmotionRecordArgs[5].feelingType to repository.insertEmotionRecordArgs[5].intensity,
            )
        }

    @Test
    fun `on save surfaces repository errors`() =
        runTest {
            val errorMessage = "DB error"
            val repository = MetricsRepositoryMock().apply { throwOnInsertEmotionRecord = RuntimeException(errorMessage) }
            val viewModel = createViewModel(repository = repository)

            viewModel.onNext()
            viewModel.onSave()
            advanceUntilIdle()

            assertEquals(EmotionalRecordStep.CAPTURE, viewModel.uiState.value.step)
            assertFalse(viewModel.uiState.value.isSaving)
            assertEquals(errorMessage, viewModel.uiState.value.error)
            assertEquals(1, repository.insertEmotionRecordCount)
        }

    @Test
    fun `on save rolls back emotion records when risk calculation fails`() =
        runTest {
            val errorMessage = "Risk query error"
            val repository = MetricsRepositoryMock().apply { throwOnGetLatestRiskFeatureSnapshot = RuntimeException(errorMessage) }
            val viewModel = createViewModel(repository = repository)

            viewModel.onNext()
            viewModel.onSave()
            advanceUntilIdle()

            assertEquals(EmotionalRecordStep.CAPTURE, viewModel.uiState.value.step)
            assertFalse(viewModel.uiState.value.isSaving)
            assertEquals(errorMessage, viewModel.uiState.value.error)
            assertTrue(repository.deleteEmotionRecordsByIdsArgs.isNotEmpty())
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
        repository: MetricsRepositoryMock = MetricsRepositoryMock(),
        snapshotRepository: RiskFeatureSnapshotRepositoryMock = RiskFeatureSnapshotRepositoryMock(),
    ): EmotionalRecordViewModel =
        EmotionalRecordViewModel(
            userProfileRepository =
                UserProfileRepositoryMock().apply {
                    profiles = listOf(UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L))
                },
            saveEmotionRecordUseCase = SaveEmotionRecordUseCase(repository),
            saveRiskFeatureSnapshotUseCase = SaveRiskFeatureSnapshotUseCase(snapshotRepository),
            calculateAndSaveRiskAssessmentUseCase = noOpCalculateRiskUseCase(repository),
            logger = TestLogger,
            ioDispatcher = testDispatcher,
        )

    private fun noOpCalculateRiskUseCase(repository: MetricsRepositoryMock): CalculateAndSaveRiskAssessmentUseCase =
        CalculateAndSaveRiskAssessmentUseCase(
            metricsRepository = repository,
            riskWeightsRepository = RiskWeightsRepositoryMock(),
        )
}
