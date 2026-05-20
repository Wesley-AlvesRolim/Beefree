package com.wesley.beefree.infrastructure.events.workers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker.Result
import androidx.work.testing.TestListenableWorkerBuilder
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.EmotionRecordDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.RiskAssessmentDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.RiskFeatureSnapshotDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.UserAddictionDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.UserProfileDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.EmotionRecordEntity
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.UserProfileEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
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
class EmotionalRecordReminderWorkerTest {
    private lateinit var context: Context
    private lateinit var mockDatabase: AppDatabase
    private lateinit var mockUserProfileDao: UserProfileDAO
    private lateinit var mockUserAddictionDao: UserAddictionDAO
    private lateinit var mockEmotionRecordDao: EmotionRecordDAO
    private lateinit var mockRiskFeatureSnapshotDao: RiskFeatureSnapshotDAO
    private lateinit var mockRiskAssessmentDao: RiskAssessmentDAO
    private lateinit var worker: EmotionalRecordReminderWorker

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        mockDatabase = mock()
        mockUserProfileDao = mock()
        mockUserAddictionDao = mock()
        mockEmotionRecordDao = mock()
        mockRiskFeatureSnapshotDao = mock()
        mockRiskAssessmentDao = mock()
        setDatabaseInstance(mockDatabase)
        whenever(mockDatabase.userProfileDao()).thenReturn(mockUserProfileDao)
        whenever(mockDatabase.userAddictionDao()).thenReturn(mockUserAddictionDao)
        whenever(mockDatabase.emotionRecordDao()).thenReturn(mockEmotionRecordDao)
        whenever(mockDatabase.riskFeatureSnapshotDao()).thenReturn(mockRiskFeatureSnapshotDao)
        whenever(mockDatabase.riskAssessmentDao()).thenReturn(mockRiskAssessmentDao)
        worker = TestListenableWorkerBuilder<EmotionalRecordReminderWorker>(context).build()
    }

    @After
    fun tearDown() {
        setDatabaseInstance(null)
    }

    @Test
    fun `onTriggered returns false when no user profiles`() =
        runTest {
            whenever(mockUserProfileDao.getAll()).thenReturn(flowOf(emptyList()))
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns false when user id is null`() =
        runTest {
            val user = UserProfileEntity(id = null, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            whenever(mockUserProfileDao.getAll()).thenReturn(flowOf(listOf(user)))
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns true when no emotion record and user created before interval`() =
        runTest {
            val userId = 1
            val user = UserProfileEntity(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            whenever(mockUserProfileDao.getAll()).thenReturn(flowOf(listOf(user)))
            whenever(mockEmotionRecordDao.getLatestByUser(userId)).thenReturn(null)
            assertTrue(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns false when no emotion record and user created within interval`() =
        runTest {
            val now = System.currentTimeMillis()
            val userId = 1
            val user = UserProfileEntity(id = userId, profileName = "Test", createdAt = now, updatedAt = now)
            whenever(mockUserProfileDao.getAll()).thenReturn(flowOf(listOf(user)))
            whenever(mockEmotionRecordDao.getLatestByUser(userId)).thenReturn(null)
            assertFalse(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns true when emotion record older than interval`() =
        runTest {
            val userId = 1
            val user = UserProfileEntity(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            val oldRecord = EmotionRecordEntity(userProfileId = userId, feelingType = "CRAVING", intensity = 5, createdAt = 0L)
            whenever(mockUserProfileDao.getAll()).thenReturn(flowOf(listOf(user)))
            whenever(mockEmotionRecordDao.getLatestByUser(userId)).thenReturn(oldRecord)
            assertTrue(worker.onTriggered())
        }

    @Test
    fun `onTriggered returns false when emotion record within interval`() =
        runTest {
            val now = System.currentTimeMillis()
            val userId = 1
            val user = UserProfileEntity(id = userId, profileName = "Test", createdAt = now, updatedAt = now)
            val recentRecord = EmotionRecordEntity(userProfileId = userId, feelingType = "CRAVING", intensity = 5, createdAt = now)
            whenever(mockUserProfileDao.getAll()).thenReturn(flowOf(listOf(user)))
            whenever(mockEmotionRecordDao.getLatestByUser(userId)).thenReturn(recentRecord)
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
            val user = UserProfileEntity(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)
            whenever(mockUserProfileDao.getAll()).thenReturn(flowOf(listOf(user)))
            whenever(mockEmotionRecordDao.getLatestByUser(userId)).thenReturn(null)
            assertEquals(Result.success(), worker.doWork())
        }

    @Test
    fun `doWork returns success when notification is not triggered`() =
        runTest {
            whenever(mockUserProfileDao.getAll()).thenReturn(flowOf(emptyList()))
            assertEquals(Result.success(), worker.doWork())
        }

    private fun setDatabaseInstance(value: AppDatabase?) {
        AppDatabase::class.java.getDeclaredField("databaseInstance").apply {
            isAccessible = true
            set(null, value)
        }
    }
}
