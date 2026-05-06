package com.wesley.beefree.ui.components.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BeeStepper(
    step: Int,
    total: Int,
    modifier: Modifier = Modifier,
    chipLabel: String? = null,
    chipContainerColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    chipContentColor: Color = MaterialTheme.colorScheme.onTertiaryContainer,
    subLabel: String? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            if (!chipLabel.isNullOrBlank()) {
                BeeStepperChip(
                    label = chipLabel,
                    containerColor = chipContainerColor,
                    contentColor = chipContentColor,
                )
            }
            BeeStepperProgressBar(
                step = step,
                total = total,
                modifier = Modifier.weight(1f),
            )
            BeeLabelSmall(
                text = "$step/$total",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (!subLabel.isNullOrBlank()) {
            BeeLabelSmall(
                text = subLabel.uppercase(),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = BeeSpacing.S),
            )
        }
    }
}

@Composable
private fun BeeStepperChip(
    label: String,
    containerColor: Color,
    contentColor: Color,
) {
    Box(
        modifier =
            Modifier
                .background(containerColor, CircleShape)
                .padding(horizontal = BeeSpacing.S, vertical = BeeSpacing.XS),
    ) {
        BeeLabelSmall(text = label, color = contentColor)
    }
}

@Composable
private fun BeeStepperProgressBar(
    step: Int,
    total: Int,
    modifier: Modifier = Modifier,
) {
    val safeTotal = total.coerceAtLeast(1)
    val ratio = (step.toFloat() / safeTotal).coerceIn(0f, 1f)
    val track = MaterialTheme.colorScheme.surfaceContainerHigh
    val fill = MaterialTheme.colorScheme.primary
    BoxWithConstraints(
        modifier =
            modifier
                .height(6.dp)
                .background(track, CircleShape),
    ) {
        val filledWidth = maxWidth * ratio
        Box(
            modifier =
                Modifier
                    .width(filledWidth)
                    .height(6.dp)
                    .background(fill, CircleShape),
        )
    }
}
