package com.wesley.beefree.ui.screens.checkin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeChipMood
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.screens.checkin.components.weatherMoodOptions

@Composable
fun DailyCheckInWeatherMoodSection(
    weatherMood: Int?,
    onWeatherMoodChange: (Int) -> Unit,
) {
    BeeLabelSmall(
        text = stringResource(R.string.check_in_weather_section),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(BeeSpacing.S))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
    ) {
        weatherMoodOptions.forEach { option ->
            BeeChipMood(
                emoji = option.emoji,
                label = stringResource(option.labelRes),
                selected = weatherMood == option.level,
                onClick = { onWeatherMoodChange(option.level) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}
