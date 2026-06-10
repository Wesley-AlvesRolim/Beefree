package com.wesley.beefree.domain.usecases.emotion

import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.repository.ports.MetricsRepository

class SaveEmotionRecordUseCase(
    private val metricsRepository: MetricsRepository,
) {
    suspend fun execute(
        userId: Int,
        emotions: Map<FeelingType, Float>,
        createdAt: Long = System.currentTimeMillis(),
    ): Result<List<Long>> =
        runCatching {
            emotions.map { (feelingType, intensity) ->
                metricsRepository.insertEmotionRecord(
                    EmotionRecord(
                        userProfileId = userId,
                        feelingType = feelingType,
                        intensity = intensity.toInt(),
                        createdAt = createdAt,
                    ),
                )
            }
        }

    suspend fun deleteEmotionRecords(ids: List<Long>) {
        metricsRepository.deleteEmotionRecordsByIds(ids)
    }
}
