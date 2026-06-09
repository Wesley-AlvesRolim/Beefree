package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.entities.DailyCheckInAnswer
import com.wesley.beefree.domain.repository.ports.CheckInRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar

class LoadPreviousObjectiveUseCase(
    private val checkInRepository: CheckInRepository,
) {
    suspend fun execute(userId: Int): String? {
        val yesterdayStart = yesterdayMidnightMillis()
        val todayStart = yesterdayStart + DAY_MS
        val checkIns = checkInRepository.getDailyCheckIns(userId).first()
        val yesterday =
            checkIns
                .filter { it.checkedInAt in yesterdayStart until todayStart }
                .maxByOrNull { it.checkedInAt }
        val answer =
            yesterday
                ?.answers
                ?.values
                ?.filterIsInstance<DailyCheckInAnswer.TextWithSuggestions>()
                ?.firstOrNull()
        return answer?.value
    }

    private fun yesterdayMidnightMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return cal.timeInMillis
    }

    companion object {
        private const val DAY_MS = 24 * 60 * 60 * 1000L
    }
}
