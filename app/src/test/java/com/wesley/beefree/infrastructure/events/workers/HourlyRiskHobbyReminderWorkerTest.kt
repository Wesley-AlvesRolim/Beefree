package com.wesley.beefree.infrastructure.events.workers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.ListenableWorker.Result
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.wesley.beefree.TestApplication
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.UserHobby
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import com.wesley.beefree.domain.repository.ports.OnboardingRepository
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
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
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class HourlyRiskHobbyReminderWorkerTest {
    private lateinit var context: Context
    private lateinit var mockUserRepository: UserProfileRepository
    private lateinit var mockMetricsRepository: MetricsRepository
    private lateinit var mockOnboardingRepository: OnboardingRepository
    private lateinit var worker: HourlyRiskHobbyReminderWorker

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        mockUserRepository = mock()
        mockMetricsRepository = mock()
        mockOnboardingRepository = mock()
        worker =
            TestListenableWorkerBuilder<HourlyRiskHobbyReminderWorker>(context)
                .setWorkerFactory(
                    object : WorkerFactory() {
                        override fun createWorker(
                            appContext: Context,
                            workerClassName: String,
                            workerParameters: WorkerParameters,
                        ): ListenableWorker =
                            HourlyRiskHobbyReminderWorker(
                                context = appContext,
                                workerParams = workerParameters,
                                userProfileRepository = mockUserRepository,
                                metricsRepository = mockMetricsRepository,
                                onboardingRepository = mockOnboardingRepository,
                            )
                    },
                ).build()
    }

    @Test
    fun `onTriggered returns false when no user profiles`() =
        runTest {
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(emptyList()))
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns false when user id is null`() =
        runTest {
            val user = UserProfile(id = null, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns false when no matching risk assessment exists`() =
        runTest {
            val userId = 1
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            val assessment = RiskAssessment(userProfileId = userId, riskScore = 90, timeWindowStart = 0L, createdAt = 0L)
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockMetricsRepository.getRiskAssessments(userId)).thenReturn(flowOf(listOf(assessment)))
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns false when matching risk assessment is not high risk`() =
        runTest {
            val userId = 1
            val now = System.currentTimeMillis()
            val hourInMillis = 60 * 60 * 1000L
            val targetTimeWindow = now + hourInMillis
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            val assessment =
                RiskAssessment(userProfileId = userId, riskScore = 40, timeWindowStart = targetTimeWindow, createdAt = 0L)
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockMetricsRepository.getRiskAssessments(userId)).thenReturn(flowOf(listOf(assessment)))
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns true when matching risk assessment is high risk`() =
        runTest {
            val userId = 1
            val now = System.currentTimeMillis()
            val hourInMillis = 60 * 60 * 1000L
            val targetTimeWindow = now + hourInMillis
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            val assessment =
                RiskAssessment(userProfileId = userId, riskScore = 80, timeWindowStart = targetTimeWindow, createdAt = 0L)
            val hobby = UserHobby(userProfileId = userId, hobbyName = "Caminhar", createdAt = 0L)
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockMetricsRepository.getRiskAssessments(userId)).thenReturn(flowOf(listOf(assessment)))
            whenever(mockOnboardingRepository.getHobbies(userId)).thenReturn(flowOf(listOf(hobby)))

            assertTrue(worker.onTriggered())
        }

    @Test
    fun `buildNotification uses hobby name when available`() =
        runTest {
            val userId = 1
            val now = System.currentTimeMillis()
            val hourInMillis = 60 * 60 * 1000L
            val targetTimeWindow = now + hourInMillis
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            val assessment =
                RiskAssessment(userProfileId = userId, riskScore = 80, timeWindowStart = targetTimeWindow, createdAt = 0L)
            val hobbies =
                listOf(
                    UserHobby(userProfileId = userId, hobbyName = "Ler", createdAt = 0L),
                    UserHobby(userProfileId = userId, hobbyName = "Caminhar", createdAt = 0L),
                )
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockMetricsRepository.getRiskAssessments(userId)).thenReturn(flowOf(listOf(assessment)))
            whenever(mockOnboardingRepository.getHobbies(userId)).thenReturn(flowOf(hobbies))

            assertTrue(worker.onTriggered())

            val notification = worker.buildNotification()
            assertTrue(hobbies.any { hobby -> notification.text.contains(hobby.hobbyName) })
        }

    @Test
    fun `doWork returns success when notification is triggered`() =
        runTest {
            val userId = 1
            val now = System.currentTimeMillis()
            val hourInMillis = 60 * 60 * 1000L
            val targetTimeWindow = now + hourInMillis
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            val assessment =
                RiskAssessment(userProfileId = userId, riskScore = 80, timeWindowStart = targetTimeWindow, createdAt = 0L)
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockMetricsRepository.getRiskAssessments(userId)).thenReturn(flowOf(listOf(assessment)))
            whenever(mockOnboardingRepository.getHobbies(userId)).thenReturn(flowOf(emptyList()))

            assertEquals(Result.success(), worker.doWork())
        }

    @Test
    fun `doWork returns success when notification is not triggered`() =
        runTest {
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(emptyList()))
            assertEquals(Result.success(), worker.doWork())
        }
}
