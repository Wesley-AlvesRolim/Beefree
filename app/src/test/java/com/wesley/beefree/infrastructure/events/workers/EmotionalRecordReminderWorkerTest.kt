package com.wesley.beefree.infrastructure.events.workers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.ListenableWorker.Result
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.wesley.beefree.TestApplication
import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.repository.ports.MetricsRepository
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
class EmotionalRecordReminderWorkerTest {
    private lateinit var context: Context
    private lateinit var mockUserRepository: UserProfileRepository
    private lateinit var mockMetricsRepository: MetricsRepository
    private lateinit var worker: EmotionalRecordReminderWorker

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        mockUserRepository = mock()
        mockMetricsRepository = mock()
        worker =
            TestListenableWorkerBuilder<EmotionalRecordReminderWorker>(context)
                .setWorkerFactory(
                    object : WorkerFactory() {
                        override fun createWorker(
                            appContext: Context,
                            workerClassName: String,
                            workerParameters: WorkerParameters,
                        ): ListenableWorker =
                            EmotionalRecordReminderWorker(
                                context = appContext,
                                workerParams = workerParameters,
                                userRepository = mockUserRepository,
                                metricsRepository = mockMetricsRepository,
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
    fun `onTriggered returns true when no emotion record and user created before interval`() =
        runTest {
            val userId = 1
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockMetricsRepository.getLatestEmotionRecord(userId)).thenReturn(null)
            assertTrue(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns false when no emotion record and user created within interval`() =
        runTest {
            val now = System.currentTimeMillis()
            val userId = 1
            val user = UserProfile(id = userId, profileName = "Test", createdAt = now, updatedAt = now)
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockMetricsRepository.getLatestEmotionRecord(userId)).thenReturn(null)
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns true when emotion record older than interval`() =
        runTest {
            val userId = 1
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            val oldRecord = EmotionRecord(userProfileId = userId, feelingType = FeelingType.CRAVING, intensity = 5, createdAt = 0L)
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockMetricsRepository.getLatestEmotionRecord(userId)).thenReturn(oldRecord)
            assertTrue(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns false when emotion record within interval`() =
        runTest {
            val now = System.currentTimeMillis()
            val userId = 1
            val user = UserProfile(id = userId, profileName = "Test", createdAt = now, updatedAt = now)
            val recentRecord = EmotionRecord(userProfileId = userId, feelingType = FeelingType.CRAVING, intensity = 5, createdAt = now)
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockMetricsRepository.getLatestEmotionRecord(userId)).thenReturn(recentRecord)
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `buildNotification intent contains open emotional record extra`() {
        val notification = worker.buildNotification()
        assertTrue(
            notification.intent.getBooleanExtra(
                EmotionalRecordReminderWorker.EXTRA_OPEN_EMOTIONAL_RECORD,
                false,
            ),
        )
    }

    @Test
    fun `doWork returns success when notification is triggered`() =
        runTest {
            val userId = 1
            val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(listOf(user)))
            whenever(mockMetricsRepository.getLatestEmotionRecord(userId)).thenReturn(null)
            assertEquals(Result.success(), worker.doWork())
        }

    @Test
    fun `doWork returns success when notification is not triggered`() =
        runTest {
            whenever(mockUserRepository.getAllProfiles()).thenReturn(flowOf(emptyList()))
            assertEquals(Result.success(), worker.doWork())
        }
}
