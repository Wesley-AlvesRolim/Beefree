package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar

class LoadTodaysEmotionRecordUseCase(
    private val metricsRepository: MetricsRepository,
) {
    suspend fun execute(userId: Int): Map<FeelingType, Int>? {
        val todayMidnight = todayMidnightMillis()
        val records = metricsRepository.getEmotionRecords(userId).first().filter { it.createdAt >= todayMidnight }
        if (records.isEmpty()) return null

        val latestByFeeling =
            records
                .groupBy { it.feelingType }
                .mapValues { (_, values) -> values.maxByOrNull { it.createdAt }?.intensity ?: DEFAULT_INTENSITY }

        return FeelingType.entries.associateWith { latestByFeeling[it] ?: DEFAULT_INTENSITY }
    }

    private fun todayMidnightMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private companion object {
        const val DEFAULT_INTENSITY = 5
    }
}
