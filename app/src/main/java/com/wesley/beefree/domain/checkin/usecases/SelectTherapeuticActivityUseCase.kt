package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.checkin.ActivityOption
import com.wesley.beefree.domain.checkin.ActivityType
import com.wesley.beefree.domain.entities.DailyCheckInAnswer
import com.wesley.beefree.domain.repository.ports.CheckInRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar

class SelectTherapeuticActivityUseCase(
    private val checkInRepository: CheckInRepository,
) {
    suspend fun execute(
        userId: Int,
        activityOptions: List<ActivityOption>,
        randomSeed: Int = (Math.random() * Int.MAX_VALUE).toInt(),
    ): ActivityType {
        val weekStart = weekStartMillis()
        val checkIns =
            checkInRepository
                .getDailyCheckIns(userId)
                .first()
                .filter { it.checkedInAt >= weekStart }

        fun countType(type: ActivityType): Int =
            checkIns.count { checkIn ->
                checkIn.answers.values
                    .filterIsInstance<DailyCheckInAnswer.TherapeuticActivity>()
                    .any { it.activityType == type.name }
            }

        val videoCount = countType(ActivityType.VIDEO)
        val mindfulnessCount = countType(ActivityType.MINDFULNESS)

        val available =
            activityOptions.filter { option ->
                when (option.type) {
                    ActivityType.VIDEO -> videoCount < VIDEO_WEEKLY_LIMIT
                    ActivityType.MINDFULNESS -> mindfulnessCount < MINDFULNESS_WEEKLY_LIMIT
                    ActivityType.PROFILE_EXERCISE -> true
                }
            }

        val pool = available.ifEmpty { activityOptions }
        return pool[randomSeed.and(Int.MAX_VALUE) % pool.size].type
    }

    private fun weekStartMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    companion object {
        const val VIDEO_WEEKLY_LIMIT = 1
        const val MINDFULNESS_WEEKLY_LIMIT = 3
    }
}
