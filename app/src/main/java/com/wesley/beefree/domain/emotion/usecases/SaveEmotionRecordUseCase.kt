package com.wesley.beefree.domain.emotion.usecases

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
    ): Result<Unit> =
        runCatching {
            emotions.forEach { (feelingType, intensity) ->
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
}
