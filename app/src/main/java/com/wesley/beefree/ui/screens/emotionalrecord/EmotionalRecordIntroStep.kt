package com.wesley.beefree.ui.screens.emotionalrecord

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeMascot
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun EmotionalRecordIntroStep(
    onNext: () -> Unit,
    onDismiss: () -> Unit,
    onSnooze: (() -> Unit)? = null,
) {
    val variables = rememberEmotionalRecordVariables()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BeeSpacing.L, vertical = BeeSpacing.M),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(modifier = Modifier.weight(1f))
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.btn_close))
            }
        }

        LazyColumn(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = BeeSpacing.L),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    BeeMascot(contentDescription = stringResource(R.string.emotional_record_mascot))
                }
            }

            item {
                BeeHeadlineMedium(text = stringResource(R.string.emotional_record_intro_title))
            }

            item {
                BeeBodyMedium(text = stringResource(R.string.emotional_record_intro_subtitle))
            }

            items(variables.size) { index ->
                EmotionalRecordVariableIntro(variable = variables[index])
            }

            item {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(BeeSpacing.M))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(BeeSpacing.M),
                    horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = stringResource(R.string.emotional_record_info_icon),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(BeeSpacing.M),
                    )
                    BeeBodySmall(
                        text = stringResource(R.string.emotional_record_intro_disclaimer),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        BeeButtonPrimary(
            onClick = onNext,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BeeSpacing.L)
                    .padding(top = BeeSpacing.L),
        ) {
            BeeLabelLarge(text = stringResource(R.string.btn_start), color = MaterialTheme.colorScheme.onPrimary)
        }

        if (onSnooze != null) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onSnooze)
                        .padding(horizontal = BeeSpacing.L, vertical = BeeSpacing.M),
                contentAlignment = Alignment.Center,
            ) {
                BeeLabelMedium(
                    text = stringResource(R.string.emotional_record_snooze),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun EmotionalRecordVariableIntro(variable: EmotionalRecordVariable) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = BeeSpacing.S),
        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(BeeSpacing.L)
                    .clip(RoundedCornerShape(BeeSpacing.S))
                    .background(variable.color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = variable.icon,
                contentDescription = stringResource(variable.labelRes),
                tint = variable.color,
                modifier = Modifier.size(BeeSpacing.M),
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.XS),
        ) {
            BeeBodyMedium(text = stringResource(variable.labelRes))
            BeeBodySmall(text = stringResource(variable.hintRes))
        }

        BeeLabelSmall(
            text = stringResource(R.string.emotional_record_risk_increases_label),
            color = MaterialTheme.colorScheme.error,
        )
    }
}
