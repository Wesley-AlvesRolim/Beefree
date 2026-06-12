package com.wesley.beefree.domain.usecases.checkin

import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.mocks.MetricsRepositoryMock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LoadTodaysEmotionRecordUseCaseTest {
    private val userId = 1
    private val now = System.currentTimeMillis()
    private val dayMs = 24L * 60 * 60 * 1000

    @Test
    fun `returns null when there is no emotion record today`() {
        runBlocking {
            val useCase = LoadTodaysEmotionRecordUseCase(MetricsRepositoryMock())

            assertNull(useCase.execute(userId))
        }
    }

    @Test
    fun `returns today's latest emotion record values for all feelings`() {
        runBlocking {
            val today = now
            val records =
                listOf(
                    record(FeelingType.SLEEP, 3, today - 3_000),
                    record(FeelingType.SLEEP, 8, today - 1_000),
                    record(FeelingType.CRAVING, 4, today - 2_000),
                    record(FeelingType.BOREDOM, 2, today - 2_500),
                    record(FeelingType.STRESS, 6, today - 4_000),
                    record(FeelingType.LONELINESS, 7, today - 5_000),
                    record(FeelingType.FATIGUE, 5, today - 6_000),
                    record(FeelingType.FATIGUE, 9, today - 500),
                    record(FeelingType.CRAVING, 1, today - dayMs),
                )
            val useCase = LoadTodaysEmotionRecordUseCase(MetricsRepositoryMock().apply { emotionRecords = records })

            val summary = useCase.execute(userId)

            assertEquals(8, summary?.getValue(FeelingType.SLEEP))
            assertEquals(4, summary?.getValue(FeelingType.CRAVING))
            assertEquals(2, summary?.getValue(FeelingType.BOREDOM))
            assertEquals(6, summary?.getValue(FeelingType.STRESS))
            assertEquals(7, summary?.getValue(FeelingType.LONELINESS))
            assertEquals(9, summary?.getValue(FeelingType.FATIGUE))
        }
    }

    private fun record(
        feelingType: FeelingType,
        intensity: Int,
        createdAt: Long,
    ) = EmotionRecord(
        userProfileId = userId,
        feelingType = feelingType,
        intensity = intensity,
        createdAt = createdAt,
    )
}
