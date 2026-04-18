package com.wesley.beefree.ui.screens.checkin.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun WeeklyCheckinPresentMomentStep(
    emotionalSatisfaction: Float,
    realConnectionLevel: Float,
    onEmotionalSatisfactionChange: (Float) -> Unit,
    onRealConnectionChange: (Float) -> Unit,
    onNext: () -> Unit,
) {
    BeeHeadlineMedium(stringResource(R.string.check_in_weekly_momento_title))
    Spacer(modifier = Modifier.height(BeeSpacing.S))
    BeeBodyMedium(
        text = stringResource(R.string.check_in_weekly_momento_subtitle),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(BeeSpacing.L))

    BeeCardSection(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(BeeSpacing.M)) {
            BeeBodyMedium(stringResource(R.string.check_in_weekly_momento_q1))
            Slider(
                value = emotionalSatisfaction,
                onValueChange = onEmotionalSatisfactionChange,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                BeeLabelSmall(
                    text = stringResource(R.string.check_in_weekly_momento_q1_low),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                BeeLabelSmall(
                    text = stringResource(R.string.check_in_weekly_momento_q1_high),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(BeeSpacing.M))

    BeeCardSection(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(BeeSpacing.M)) {
            BeeBodyMedium(stringResource(R.string.check_in_weekly_momento_q2))
            Slider(
                value = realConnectionLevel,
                onValueChange = onRealConnectionChange,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                BeeLabelSmall(
                    text = stringResource(R.string.check_in_weekly_momento_q2_low),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                BeeLabelSmall(
                    text = stringResource(R.string.check_in_weekly_momento_q2_high),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(BeeSpacing.XL))
    BeeButtonPrimary(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth(),
    ) {
        BeeLabelLarge(stringResource(R.string.check_in_weekly_continue))
    }
}
