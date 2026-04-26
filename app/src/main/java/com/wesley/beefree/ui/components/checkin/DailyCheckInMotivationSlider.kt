package com.wesley.beefree.ui.components.checkin

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import kotlin.math.roundToInt

@Composable
fun DailyCheckInMotivationSlider(
    dopamineLevel: Int,
    onDopamineLevelChange: (Int) -> Unit,
) {
    val motivationSlider = (dopamineLevel - 1) / 4.0f
    val motivationPercent = (motivationSlider * 100).roundToInt()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BeeBodyMedium(
            text = stringResource(R.string.check_in_motivation_label),
            modifier = Modifier.weight(1f),
        )
        BeeLabelLarge(
            text = "$motivationPercent%",
            color = MaterialTheme.colorScheme.primary,
        )
    }
    Slider(
        value = motivationSlider,
        onValueChange = { onDopamineLevelChange((it * 4).roundToInt() + 1) },
        modifier = Modifier.fillMaxWidth(),
        steps = 3,
    )
    Row(modifier = Modifier.fillMaxWidth()) {
        BeeLabelSmall(
            text = stringResource(R.string.check_in_motivation_low),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        BeeLabelSmall(
            text = stringResource(R.string.check_in_motivation_high),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
    }
}
