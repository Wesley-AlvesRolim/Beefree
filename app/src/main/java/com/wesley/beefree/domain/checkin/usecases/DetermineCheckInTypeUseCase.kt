package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.checkin.CheckInType
import com.wesley.beefree.domain.intervention.ports.ClinicalProfileStrategy

class DetermineCheckInTypeUseCase {
    fun execute(
        userCreatedAt: Long,
        now: Long = System.currentTimeMillis(),
        profileStrategy: ClinicalProfileStrategy? = null,
    ): CheckInType {
        if (profileStrategy != null && !profileStrategy.showsWeeklyCheckIn) {
            return CheckInType.DAILY
        }
        val daysSinceStart = ((now - userCreatedAt) / DAY_MS).toInt()
        return if ((daysSinceStart + 1) % 7 == 0) CheckInType.WEEKLY else CheckInType.DAILY
    }

    companion object {
        private const val DAY_MS = 24L * 60 * 60 * 1000
    }
}
