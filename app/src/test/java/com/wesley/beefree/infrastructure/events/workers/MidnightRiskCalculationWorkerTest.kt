package com.wesley.beefree.infrastructure.events.workers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.ListenableWorker.Result
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
import com.wesley.beefree.domain.risk.RiskLevel
import com.wesley.beefree.domain.risk.ports.RiskEngine
import com.wesley.beefree.domain.risk.usecases.CalculateAndSaveRiskAssessmentUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MidnightRiskCalculationWorkerTest {
    private lateinit var context: Context
    private lateinit var mockUserProfileRepository: UserProfileRepository
    private lateinit var mockCalculateAndSaveRiskAssessmentUseCase: CalculateAndSaveRiskAssessmentUseCase
    private lateinit var mockRiskEngine: RiskEngine
    private lateinit var worker: MidnightRiskCalculationWorker

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        mockUserProfileRepository = mock()
        mockCalculateAndSaveRiskAssessmentUseCase = mock()
        mockRiskEngine = mock()
        worker =
            TestListenableWorkerBuilder<MidnightRiskCalculationWorker>(context)
                .setWorkerFactory(
                    object : WorkerFactory() {
                        override fun createWorker(
                            appContext: Context,
                            workerClassName: String,
                            workerParameters: WorkerParameters,
                        ): ListenableWorker =
                            MidnightRiskCalculationWorker(
                                context = appContext,
                                workerParams = workerParameters,
                                userProfileRepository = mockUserProfileRepository,
                                calculateAndSaveRiskAssessmentUseCase = mockCalculateAndSaveRiskAssessmentUseCase,
                                riskEngine = mockRiskEngine,
                            )
                    },
                ).build()
    }

    @Test
    fun `onTriggered returns false when no user profiles`() =
        runTest {
            whenever(mockUserProfileRepository.getAllProfiles()).thenReturn(flowOf(emptyList()))
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns false when user id is null`() =
        runTest {
            val user = UserProfile(id = null, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            whenever(mockUserProfileRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns false when use case fails`() =
        runTest {
            val userId = 1
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            whenever(mockUserProfileRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockCalculateAndSaveRiskAssessmentUseCase.execute(userId))
                .thenReturn(kotlin.Result.failure(RuntimeException("error")))
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns false when scores list is empty`() =
        runTest {
            val userId = 1
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            whenever(mockUserProfileRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockCalculateAndSaveRiskAssessmentUseCase.execute(userId))
                .thenReturn(kotlin.Result.success(emptyList()))
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns false when all scores are low`() =
        runTest {
            val userId = 1
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            val scores = listOf(10, 20, 30)
            whenever(mockUserProfileRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockCalculateAndSaveRiskAssessmentUseCase.execute(userId))
                .thenReturn(kotlin.Result.success(scores))
            scores.forEach { score ->
                whenever(mockRiskEngine.classify(score / 100.0)).thenReturn(RiskLevel.LOW)
            }
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns true when at least one score is medium`() =
        runTest {
            val userId = 1
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            val scores = listOf(10, 55, 30)
            whenever(mockUserProfileRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockCalculateAndSaveRiskAssessmentUseCase.execute(userId))
                .thenReturn(kotlin.Result.success(scores))
            whenever(mockRiskEngine.classify(0.10)).thenReturn(RiskLevel.LOW)
            whenever(mockRiskEngine.classify(0.55)).thenReturn(RiskLevel.MEDIUM)
            whenever(mockRiskEngine.classify(0.30)).thenReturn(RiskLevel.LOW)
            assertTrue(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns true when at least one score is high`() =
        runTest {
            val userId = 1
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            val scores = listOf(10, 85)
            whenever(mockUserProfileRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockCalculateAndSaveRiskAssessmentUseCase.execute(userId))
                .thenReturn(kotlin.Result.success(scores))
            whenever(mockRiskEngine.classify(0.10)).thenReturn(RiskLevel.LOW)
            whenever(mockRiskEngine.classify(0.85)).thenReturn(RiskLevel.HIGH)
            assertTrue(worker.onTriggered())
        }

    @Test
    fun `buildNotification returns content with correct channel id`() {
        val notification = worker.buildNotification()
        assertEquals("midnight_risk", notification.channelId)
    }

    @Test
    fun `buildNotification returns content with correct notification id`() {
        val notification = worker.buildNotification()
        assertEquals(2004, notification.id)
    }

    @Test
    fun `doWork returns success when notification is triggered`() =
        runTest {
            val userId = 1
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            val scores = listOf(80)
            whenever(mockUserProfileRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockCalculateAndSaveRiskAssessmentUseCase.execute(userId))
                .thenReturn(kotlin.Result.success(scores))
            whenever(mockRiskEngine.classify(0.80)).thenReturn(RiskLevel.HIGH)
            assertEquals(Result.success(), worker.doWork())
        }

    @Test
    fun `doWork returns success when notification is not triggered`() =
        runTest {
            whenever(mockUserProfileRepository.getAllProfiles()).thenReturn(flowOf(emptyList()))
            assertEquals(Result.success(), worker.doWork())
        }
}
