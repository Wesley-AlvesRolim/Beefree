package com.wesley.beefree.ui.screens.emotionalrecord

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeechTone
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun EmotionalRecordDoneStep(
    emotions: Map<FeelingType, Float>,
    onDone: () -> Unit,
) {
    val variables = rememberEmotionalRecordVariables()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = BeeSpacing.L),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
        ) {
            item {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = BeeSpacing.M),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
                ) {
                    BeeHeadlineMedium(text = stringResource(R.string.emotional_record_done_title))
                    BeeMascotSpeech(
                        speechText = stringResource(R.string.emotional_record_done_motivational),
                        tone = BeeMascotSpeechTone.Primary,
                    )
                    BeeBodySmall(
                        text = stringResource(R.string.emotional_record_done_message),
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }

            item {
                BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(BeeSpacing.M),
                        verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
                    ) {
                        BeeHeadlineSmall(text = stringResource(R.string.emotional_record_done_section_title))
                        variables.forEach { variable ->
                            val value = emotions[variable.emotion] ?: 5f
                            EmotionalRecordVariableSummaryRow(
                                variable = variable,
                                value = value.toInt(),
                            )
                        }
                    }
                }
            }
        }

        BeeButtonPrimary(
            onClick = onDone,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(BeeSpacing.L),
        ) {
            BeeLabelLarge(
                text = stringResource(R.string.emotional_record_done_button),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
private fun EmotionalRecordVariableSummaryRow(
    variable: EmotionalRecordVariable,
    value: Int,
) {
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
            BeeBodySmall(text = stringResource(variable.labelRes))
            LinearProgressIndicator(
                progress = { value / 10f },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(BeeSpacing.S),
                color = variable.color,
                trackColor = variable.color.copy(alpha = 0.1f),
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
        }

        BeeBodySmall(
            text = "$value/10",
            color = variable.color,
        )
    }
}
