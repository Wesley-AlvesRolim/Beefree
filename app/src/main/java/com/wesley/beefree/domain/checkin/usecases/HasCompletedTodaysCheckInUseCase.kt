package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.checkin.CheckInType
import com.wesley.beefree.storage.ports.CheckInRepository
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HasCompletedTodaysCheckInUseCase(
    private val checkInRepository: CheckInRepository,
    private val determineCheckInTypeUseCase: DetermineCheckInTypeUseCase,
) {
    suspend fun execute(
        userId: Int,
        userCreatedAt: Long,
    ): Boolean =
        when (determineCheckInTypeUseCase.execute(userCreatedAt)) {
            CheckInType.DAILY -> hasDailyCheckInToday(userId)
            CheckInType.WEEKLY -> hasWeeklyCheckInThisWeek(userId)
        }

    private suspend fun hasDailyCheckInToday(userId: Int): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())
        return checkInRepository
            .getDailyCheckIns(userId)
            .first()
            .any { sdf.format(Date(it.checkedInAt)) == today }
    }

    private suspend fun hasWeeklyCheckInThisWeek(userId: Int): Boolean {
        val weekStart = currentWeekStart()
        return checkInRepository
            .getWeeklyCheckIns(userId)
            .first()
            .any { it.weekStartDate == weekStart }
    }

    companion object {
        fun currentWeekStart(): Long {
            val cal =
                Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            return cal.timeInMillis
        }
    }
}
