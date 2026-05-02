package com.wesley.beefree.ui.components.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import java.util.Calendar

@Composable
fun BeeCalendar(
    year: Int,
    month: Int,
    modifier: Modifier = Modifier,
    cellColor: @Composable (dayNumber: Int) -> Color = { MaterialTheme.colorScheme.primaryContainer },
    cellContent: @Composable BoxScope.(dayNumber: Int) -> Unit = { day ->
        BeeLabelSmall(text = day.toString())
    },
) {
    val calendar =
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val leadingEmptyDays = if (firstDayOfWeek == Calendar.SUNDAY) 0 else firstDayOfWeek - Calendar.SUNDAY

    val daysInWeek = 7
    val totalCells = leadingEmptyDays + daysInMonth
    val rows = (totalCells + daysInWeek - 1) / daysInWeek

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.XS),
    ) {
        repeat(rows) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(BeeSpacing.XS),
            ) {
                repeat(daysInWeek) { col ->
                    val cellIndex = row * daysInWeek + col
                    val dayNumber = cellIndex - leadingEmptyDays + 1

                    if (dayNumber in 1..daysInMonth) {
                        val color = cellColor(dayNumber)
                        Box(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(BeeSpacing.S))
                                    .background(color),
                            contentAlignment = Alignment.Center,
                        ) {
                            cellContent(dayNumber)
                        }
                    } else {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
