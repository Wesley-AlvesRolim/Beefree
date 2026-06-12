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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.RiskTrigger
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import java.util.Locale

@Composable
fun TriggersThisWeekCard(topTriggers: List<Pair<RiskTrigger, Double>>) {
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
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BeeHeadlineSmall(stringResource(R.string.home_triggers_week_title))
            }

            if (topTriggers.isEmpty()) {
                Spacer(Modifier.height(BeeSpacing.S))
                BeeBodySmall(
                    text = stringResource(R.string.triggers_empty),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Spacer(Modifier.height(BeeSpacing.S))
                Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
                    val biggestWeight = topTriggers[0]
                    topTriggers.forEach { (trigger, count) ->
                        TriggerRow(
                            trigger = trigger,
                            weight = count,
                            biggestWeight = biggestWeight.second,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TriggerRow(
    trigger: RiskTrigger,
    weight: Double,
    biggestWeight: Double,
) {
    val icon =
        when (trigger) {
            RiskTrigger.SLEEP -> Icons.Default.Bedtime
            RiskTrigger.CRAVING -> Icons.Default.Whatshot
            RiskTrigger.BOREDOM -> Icons.Default.AccessTime
            RiskTrigger.STRESS -> Icons.Default.Psychology
            RiskTrigger.LONELINESS -> Icons.Default.FavoriteBorder
            RiskTrigger.FATIGUE -> Icons.Default.BatteryAlert
            RiskTrigger.HOURS_SINCE_LAST_RELAPSE -> Icons.Default.Timer
            RiskTrigger.HOUR_OF_DAY -> Icons.Default.Schedule
            RiskTrigger.DAY_OF_WEEK -> Icons.Default.CalendarToday
            RiskTrigger.TIME_SINCE_LAST_APP_OPEN -> Icons.Default.PhoneAndroid
            RiskTrigger.MISSING_CHECKINS -> Icons.Default.EventBusy
        }

    val text =
        when (trigger) {
            RiskTrigger.SLEEP -> stringResource(R.string.feeling_type_sleep)
            RiskTrigger.CRAVING -> stringResource(R.string.feeling_type_craving)
            RiskTrigger.BOREDOM -> stringResource(R.string.feeling_type_boredom)
            RiskTrigger.STRESS -> stringResource(R.string.feeling_type_stress)
            RiskTrigger.LONELINESS -> stringResource(R.string.feeling_type_loneliness)
            RiskTrigger.FATIGUE -> stringResource(R.string.feeling_type_fatigue)
            RiskTrigger.HOURS_SINCE_LAST_RELAPSE -> stringResource(R.string.trigger_hours_since_last_relapse)
            RiskTrigger.HOUR_OF_DAY -> stringResource(R.string.trigger_hour_of_day)
            RiskTrigger.DAY_OF_WEEK -> stringResource(R.string.trigger_day_of_week)
            RiskTrigger.TIME_SINCE_LAST_APP_OPEN -> stringResource(R.string.trigger_time_since_last_app_open)
            RiskTrigger.MISSING_CHECKINS -> stringResource(R.string.trigger_missing_checkins)
        }

    val triggerDescription = trigger.name.lowercase()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
    ) {
        Surface(
            modifier = Modifier.size(30.dp),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.tertiaryContainer,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = triggerDescription,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(14.dp),
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            val rounded = String.format(Locale.US, "%.1f", weight).toDouble()
            val fraction = (weight / biggestWeight).toFloat().coerceIn(0f, 1f)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                BeeLabelMedium(text.lowercase().replaceFirstChar { it.uppercase() })

                BeeBodySmall(
                    text = "${rounded}X",
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(Modifier.height(BeeSpacing.XS))

            BeeBodySmall(
                text =
                    stringResource(
                        R.string.trigger_chance_description,
                        rounded.toString(),
                    ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(BeeSpacing.XS))

            val barColor =
                when {
                    fraction > 0.7f -> MaterialTheme.colorScheme.primary
                    fraction > 0.4f -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.tertiaryContainer
                }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(BeeSpacing.XS)
                        .clip(RoundedCornerShape(999.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(fraction)
                            .height(BeeSpacing.XS)
                            .clip(RoundedCornerShape(999.dp))
                            .background(barColor),
                )
            }
        }
    }
}
