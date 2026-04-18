package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.entities.WeeklyCheckIn
import com.wesley.beefree.storage.ports.CheckInRepository
import java.util.Calendar

class SaveWeeklyCheckInUseCase(
    private val checkInRepository: CheckInRepository,
) {
    suspend fun execute(
        userId: Int,
        valuesAlignmentText: String?,
        realConnectionEnergy: Int?,
    ): Result<Unit> =
        runCatching {
            val now = System.currentTimeMillis()
            val weekStartDate = startOfWeek(now)

            checkInRepository.insertWeeklyCheckIn(
                WeeklyCheckIn(
                    userProfileId = userId,
                    weekStartDate = weekStartDate,
                    valuesAlignmentText = valuesAlignmentText,
                    realConnectionEnergy = realConnectionEnergy,
                    createdAt = now,
                ),
            )
        }

    private fun startOfWeek(now: Long): Long {
        val cal =
            Calendar.getInstance().apply {
                timeInMillis = now
                set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        return cal.timeInMillis
    }
}
