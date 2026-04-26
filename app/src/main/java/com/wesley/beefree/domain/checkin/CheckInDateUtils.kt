package com.wesley.beefree.domain.checkin

import java.util.Calendar

object CheckInDateUtils {
    fun startOfWeek(now: Long): Long {
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
