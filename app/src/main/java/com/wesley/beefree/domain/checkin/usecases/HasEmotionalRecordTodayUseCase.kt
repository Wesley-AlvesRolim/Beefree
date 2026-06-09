package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.repository.ports.MetricsRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar

class HasEmotionalRecordTodayUseCase(
    private val metricsRepository: MetricsRepository,
) {
    suspend fun execute(userId: Int): Boolean {
        val todayMidnight = todayMidnightMillis()
        val records = metricsRepository.getEmotionRecords(userId).first()
        return records.any { it.createdAt >= todayMidnight }
    }

    private fun todayMidnightMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
