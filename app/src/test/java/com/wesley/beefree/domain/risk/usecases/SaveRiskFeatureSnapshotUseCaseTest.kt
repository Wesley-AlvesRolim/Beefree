package com.wesley.beefree.domain.risk.usecases

import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.repository.ports.RiskFeatureSnapshotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class SaveRiskFeatureSnapshotUseCaseTest {
    @Test
    fun `saves snapshot with provided values and derived date fields`() =
        runTest {
            val repository = RecordingRiskFeatureSnapshotRepository()
            val useCase = SaveRiskFeatureSnapshotUseCase(repository)
            val userId = 7
            val sleep = 6
            val craving = 8
            val boredom = 3
            val stress = 5
            val loneliness = 4
            val fatigue = 7

            val result =
                useCase.execute(
                    userId = userId,
                    sleep = sleep,
                    craving = craving,
                    boredom = boredom,
                    stress = stress,
                    loneliness = loneliness,
                    fatigue = fatigue,
                )

            assertTrue(result.isSuccess)
            assertEquals(1, repository.saveCalls)

            val snapshot = repository.savedSnapshot ?: error("Expected snapshot to be saved")
            assertEquals(userId, snapshot.userProfileId)
            assertEquals(sleep, snapshot.sleep)
            assertEquals(craving, snapshot.craving)
            assertEquals(boredom, snapshot.boredom)
            assertEquals(stress, snapshot.stress)
            assertEquals(loneliness, snapshot.loneliness)
            assertEquals(fatigue, snapshot.fatigue)

            val calendar = Calendar.getInstance().apply { timeInMillis = snapshot.createdAt }
            assertEquals(calendar.get(Calendar.HOUR_OF_DAY), snapshot.hourOfDay)
            assertEquals(calendar.get(Calendar.DAY_OF_WEEK), snapshot.dayOfWeek)
        }

    @Test
    fun `returns failure when repository save throws`() =
        runTest {
            val repository = RecordingRiskFeatureSnapshotRepository(throwOnSave = RuntimeException("DB error"))
            val useCase = SaveRiskFeatureSnapshotUseCase(repository)

            val result =
                useCase.execute(
                    userId = 7,
                    sleep = 6,
                    craving = 8,
                    boredom = 3,
                    stress = 5,
                    loneliness = 4,
                    fatigue = 7,
                )

            assertTrue(result.isFailure)
            assertNull(repository.savedSnapshot)
            assertEquals(1, repository.saveCalls)
        }

    private class RecordingRiskFeatureSnapshotRepository(
        private val throwOnSave: Throwable? = null,
    ) : RiskFeatureSnapshotRepository {
        var savedSnapshot: RiskFeatureSnapshot? = null
            private set

        var saveCalls: Int = 0
            private set

        override suspend fun save(snapshot: RiskFeatureSnapshot): Long {
            saveCalls++
            throwOnSave?.let { throw it }
            savedSnapshot = snapshot
            return 1L
        }

        override fun getAllByUser(userId: Int): Flow<List<RiskFeatureSnapshot>> = emptyFlow()

        override suspend fun getLatestByUser(userId: Int): RiskFeatureSnapshot? = null
    }
}
