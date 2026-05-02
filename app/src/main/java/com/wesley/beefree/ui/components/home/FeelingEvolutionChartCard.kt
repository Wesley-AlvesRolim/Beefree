package com.wesley.beefree.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun FeelingEvolutionChartCard(
    anxietySeries: List<Float>,
    satisfactionSeries: List<Float>,
    anxietyDelta: Int,
    satisfactionDelta: Int,
    onClick: () -> Unit,
) {
    BeeCardSection(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = BeeSpacing.L)
                .clickable { onClick() },
    ) {
        Column(modifier = Modifier.padding(BeeSpacing.M)) {
            BeeHeadlineSmall(stringResource(R.string.home_dual_chart_title))
            Spacer(Modifier.height(BeeSpacing.XS))
            BeeBodySmall(
                text = stringResource(R.string.home_dual_chart_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(BeeSpacing.S))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BeeSpacing.M),
        ) {
            DualAxisChart(
                anxietySeries = anxietySeries,
                satisfactionSeries = satisfactionSeries,
            )
        }
        Spacer(Modifier.height(BeeSpacing.S))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(BeeSpacing.M),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BeeLabelMedium(
                        text = formatDelta(anxietyDelta),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.width(BeeSpacing.XS))
                    BeeLabelSmall(
                        text = stringResource(R.string.emotional_chart_anxiety).uppercase(),
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BeeLabelMedium(
                        text = formatDelta(satisfactionDelta),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.width(BeeSpacing.XS))
                    BeeLabelSmall(
                        text = stringResource(R.string.emotional_chart_satisfaction).uppercase(),
                    )
                }
            }
        }
    }
}

private fun formatDelta(delta: Int): String = if (delta > 0) "+$delta%" else "$delta%"
