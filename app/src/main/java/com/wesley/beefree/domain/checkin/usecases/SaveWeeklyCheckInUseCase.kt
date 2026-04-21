package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.checkin.CheckInDateUtils
import com.wesley.beefree.domain.entities.WeeklyCheckIn
import com.wesley.beefree.infrastructure.storage.ports.CheckInRepository

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
            val weekStartDate = CheckInDateUtils.startOfWeek(now)

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

}
