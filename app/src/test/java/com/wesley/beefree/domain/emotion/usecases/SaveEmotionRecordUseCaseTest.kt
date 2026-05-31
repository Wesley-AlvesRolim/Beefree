package com.wesley.beefree.domain.emotion.usecases

import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SaveEmotionRecordUseCaseTest {
    private val metricsRepository: MetricsRepository = mock()
    private val useCase = SaveEmotionRecordUseCase(metricsRepository)

    @Test
    fun `save calls insert for each emotion`() =
        runBlocking {
            whenever(metricsRepository.insertEmotionRecord(any())).thenReturn(1L)
            val emotions =
                mapOf(
                    FeelingType.SLEEP to 7f,
                    FeelingType.CRAVING to 4f,
                    FeelingType.BOREDOM to 2f,
                    FeelingType.STRESS to 6f,
                    FeelingType.LONELINESS to 5f,
                    FeelingType.FATIGUE to 3f,
                )

            useCase.execute(userId = 1, emotions = emotions)

            verify(metricsRepository, times(6)).insertEmotionRecord(any())
            Unit
        }

    @Test
    fun `save returns success with inserted ids`() =
        runBlocking {
            whenever(metricsRepository.insertEmotionRecord(any())).thenReturn(42L)
            val emotions = mapOf(FeelingType.STRESS to 5f)

            val result = useCase.execute(userId = 1, emotions = emotions)

            assertTrue(result.isSuccess)
            assertEquals(listOf(42L), result.getOrNull())
        }

    @Test
    fun `delete emotion records delegates to repository`() =
        runBlocking {
            val ids = listOf(1L, 2L, 3L)

            useCase.deleteEmotionRecords(ids)

            verify(metricsRepository).deleteEmotionRecordsByIds(ids)
            Unit
        }

    @Test
    fun `save returns failure on repository exception`() =
        runBlocking {
            whenever(metricsRepository.insertEmotionRecord(any()))
                .thenThrow(RuntimeException("DB error"))
            val emotions = mapOf(FeelingType.STRESS to 5f)

            val result = useCase.execute(userId = 1, emotions = emotions)

            assertTrue(result.isFailure)
        }

    @Test
    fun `save uses correct user id and intensity`() =
        runBlocking {
            whenever(metricsRepository.insertEmotionRecord(any())).thenReturn(1L)
            val userId = 42
            val intensity = 8.7f
            val captor = argumentCaptor<EmotionRecord>()

            useCase.execute(userId = userId, emotions = mapOf(FeelingType.CRAVING to intensity))

            verify(metricsRepository).insertEmotionRecord(captor.capture())
            val saved = captor.firstValue
            assertEquals(userId, saved.userProfileId)
            assertEquals(FeelingType.CRAVING, saved.feelingType)
            assertEquals(intensity.toInt(), saved.intensity)
        }

    @Test
    fun `save uses provided timestamp`() =
        runBlocking {
            whenever(metricsRepository.insertEmotionRecord(any())).thenReturn(1L)
            val timestamp = 1_700_000_000_000L
            val captor = argumentCaptor<EmotionRecord>()

            useCase.execute(userId = 1, emotions = mapOf(FeelingType.FATIGUE to 3f), createdAt = timestamp)

            verify(metricsRepository).insertEmotionRecord(captor.capture())
            assertEquals(timestamp, captor.firstValue.createdAt)
        }
}
