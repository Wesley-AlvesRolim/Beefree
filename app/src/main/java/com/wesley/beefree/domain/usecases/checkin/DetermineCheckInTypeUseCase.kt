package com.wesley.beefree.domain.usecases.checkin

import com.wesley.beefree.domain.checkin.CheckInType

class DetermineCheckInTypeUseCase {
    fun execute(
        userCreatedAt: Long,
        now: Long = System.currentTimeMillis(),
    ): CheckInType = CheckInType.DAILY
}
