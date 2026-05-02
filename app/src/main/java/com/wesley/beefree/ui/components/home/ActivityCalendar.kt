package com.wesley.beefree.ui.components.home

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.ui.components.designsystem.BeeCalendar
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import java.util.Calendar

@Composable
fun ActivityCalendar(
    relapseHistory: List<RelapseRecord>,
    modifier: Modifier = Modifier,
) {
    val now = Calendar.getInstance()
    val year = now.get(Calendar.YEAR)
    val month = now.get(Calendar.MONTH)

    BeeCalendar(
        year = year,
        month = month,
        modifier = modifier,
        cellColor = { dayNumber ->
            when (tileTypeForDay(calendarForDay(year, month, dayNumber), relapseHistory)) {
                CalendarDayType.RELAPSE -> MaterialTheme.colorScheme.tertiaryContainer
                CalendarDayType.ALIGN -> MaterialTheme.colorScheme.primaryContainer
                CalendarDayType.TODAY -> MaterialTheme.colorScheme.surfaceContainerHigh
            }
        },
        cellContent = { dayNumber ->
            if (tileTypeForDay(calendarForDay(year, month, dayNumber), relapseHistory) == CalendarDayType.ALIGN) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(BeeSpacing.M),
                )
            }
        },
    )
}

private fun calendarForDay(
    year: Int,
    month: Int,
    day: Int,
): Calendar =
    Calendar.getInstance().apply {
        set(year, month, day)
    }

private fun tileTypeForDay(
    dateAsCalendar: Calendar,
    relapses: List<RelapseRecord>,
): CalendarDayType {
    val dateClone = dateAsCalendar.clone() as Calendar
    val dayStart =
        dateClone
            .apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
    val dayEnd =
        (dateClone.clone() as Calendar)
            .apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }.timeInMillis

    return when {
        relapses.any { it.createdAt in dayStart..dayEnd } -> CalendarDayType.RELAPSE
        dayEnd < System.currentTimeMillis() -> CalendarDayType.ALIGN
        else -> CalendarDayType.TODAY
    }
}

enum class CalendarDayType {
    ALIGN,
    RELAPSE,
    TODAY,
}
