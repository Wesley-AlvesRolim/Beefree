package com.wesley.beefree.ui.components.checkin.daily

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.domain.checkin.EmotionalRecordStep
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.screens.emotionalrecord.EmotionalRecordSummaryCard
import com.wesley.beefree.ui.screens.emotionalrecord.rememberEmotionalRecordVariables

@Composable
fun EmotionalRecordStepContent(
    spec: EmotionalRecordStep,
    emotionRecordValues: Map<FeelingType, Float>?,
    onGoRecord: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        BeeHeadlineSmall(stringByName(spec.titleKey))
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeBodyMedium(
            text = stringByName(spec.subtitleKey),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        if (emotionRecordValues != null) {
            EmotionalRecordSummaryCard(emotions = emotionRecordValues)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
                rememberEmotionalRecordVariables().forEach { variable ->
                    BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(BeeSpacing.M),
                            horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(BeeSpacing.L)
                                        .clip(CircleShape)
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
                            Column(modifier = Modifier.weight(1f)) {
                                BeeBodyMedium(text = stringResource(variable.labelRes))
                                BeeLabelSmall(
                                    text = stringResource(variable.hintRes),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
