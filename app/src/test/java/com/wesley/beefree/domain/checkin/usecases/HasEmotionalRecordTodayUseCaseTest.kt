package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class HasEmotionalRecordTodayUseCaseTest {
    private val metricsRepository: MetricsRepository = mock()
    private val useCase = HasEmotionalRecordTodayUseCase(metricsRepository)

    private val userId = 1
    private val now = System.currentTimeMillis()
    private val dayMs = 24L * 60 * 60 * 1000

    private fun record(createdAt: Long) =
        EmotionRecord(
            userProfileId = userId,
            feelingType = FeelingType.CRAVING,
            intensity = 5,
            createdAt = createdAt,
        )

    @Test
    fun `returns false when no records exist`() {
        runBlocking {
            whenever(metricsRepository.getEmotionRecords(userId)).thenReturn(flowOf(emptyList()))
            assertFalse(useCase.execute(userId))
        }
    }

    @Test
    fun `returns false when only records from yesterday exist`() {
        runBlocking {
            val yesterday = now - dayMs
            whenever(metricsRepository.getEmotionRecords(userId)).thenReturn(
                flowOf(listOf(record(yesterday))),
            )
            assertFalse(useCase.execute(userId))
        }
    }

    @Test
    fun `returns true when record exists today`() {
        runBlocking {
            whenever(metricsRepository.getEmotionRecords(userId)).thenReturn(
                flowOf(listOf(record(now))),
            )
            assertTrue(useCase.execute(userId))
        }
    }

    @Test
    fun `returns true when multiple records exist and at least one is today`() {
        runBlocking {
            whenever(metricsRepository.getEmotionRecords(userId)).thenReturn(
                flowOf(listOf(record(now - dayMs), record(now))),
            )
            assertTrue(useCase.execute(userId))
        }
    }
}
