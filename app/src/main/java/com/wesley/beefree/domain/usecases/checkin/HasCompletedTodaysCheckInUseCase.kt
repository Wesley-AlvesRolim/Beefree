package com.wesley.beefree.domain.usecases.checkin

import com.wesley.beefree.domain.repository.ports.CheckInRepository
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HasCompletedTodaysCheckInUseCase(
    private val checkInRepository: CheckInRepository,
) {
    suspend fun execute(
        userId: Int,
        now: Long = System.currentTimeMillis(),
    ): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date(now))
        return checkInRepository
            .getDailyCheckIns(userId)
            .first()
            .any { sdf.format(Date(it.checkedInAt)) == today }
    }
}
