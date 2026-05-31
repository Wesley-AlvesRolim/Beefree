package com.wesley.beefree.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun EvolutionCalendarCard(
    relapseHistory: List<RelapseRecord>,
    relapseSuccessRate: Float,
    alignedDays: Int,
    setbackDays: Int,
) {
    BeeCardSection(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = BeeSpacing.M),
    ) {
        Column(modifier = Modifier.padding(BeeSpacing.M)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                BeeHeadlineSmall(stringResource(R.string.home_evolution_title))
                Column(horizontalAlignment = Alignment.End) {
                    BeeHeadlineMedium(
                        text = "${(relapseSuccessRate * 100).toInt()}%",
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Right,
                    )
                    BeeLabelSmall(
                        text =
                            stringResource(R.string.relapse_calendar_success_rate),
                        textAlign = TextAlign.Right,
                    )
                }
            }

            Spacer(Modifier.height(BeeSpacing.M))
            ActivityCalendar(relapseHistory)
            Spacer(Modifier.height(BeeSpacing.M))

            Row(horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M)) {
                CalendarLegendItem(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    label = stringResource(R.string.home_legend_aligned, alignedDays),
                )
                CalendarLegendItem(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    label = stringResource(R.string.home_legend_setback, setbackDays),
                )
                CalendarLegendItem(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    label = stringResource(R.string.home_legend_today),
                )
            }
        }
    }
}

@Composable
private fun CalendarLegendItem(
    color: Color,
    label: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier =
                Modifier
                    .size(BeeSpacing.S)
                    .clip(RoundedCornerShape(BeeSpacing.XS))
                    .background(color),
        )
        Spacer(Modifier.width(BeeSpacing.XS))
        BeeBodySmall(label)
    }
}
