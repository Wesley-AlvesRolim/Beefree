package com.wesley.beefree.ui.components.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class BeeMascotSpeechTone {
    Primary,
    Secondary,
    Tertiary,
}

@Composable
fun BeeMascotSpeech(
    speechText: String,
    modifier: Modifier = Modifier,
    tone: BeeMascotSpeechTone = BeeMascotSpeechTone.Primary,
) {
    val backgroundColor =
        when (tone) {
            BeeMascotSpeechTone.Primary -> MaterialTheme.colorScheme.primaryContainer
            BeeMascotSpeechTone.Secondary -> MaterialTheme.colorScheme.secondaryContainer
            BeeMascotSpeechTone.Tertiary -> MaterialTheme.colorScheme.tertiaryContainer
        }

    val textColor =
        when (tone) {
            BeeMascotSpeechTone.Primary -> MaterialTheme.colorScheme.onPrimaryContainer
            BeeMascotSpeechTone.Secondary -> MaterialTheme.colorScheme.onSecondaryContainer
            BeeMascotSpeechTone.Tertiary -> MaterialTheme.colorScheme.onTertiaryContainer
        }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
        verticalAlignment = Alignment.Top,
    ) {
        BeeMascot(size = BeeMascotSize.Small)

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .background(backgroundColor, RoundedCornerShape(12.dp))
                    .padding(BeeSpacing.M),
        ) {
            BeeBodyMedium(
                text = speechText,
                color = textColor,
                textAlign = TextAlign.Start,
            )
        }
    }
}
