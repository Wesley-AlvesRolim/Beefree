package com.wesley.beefree.ui.components.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BeeScale0to10(
    value: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    lowLabel: String? = null,
    highLabel: String? = null,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    val track = MaterialTheme.colorScheme.outlineVariant
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = BeeSpacing.S),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            for (i in 0..10) {
                val isSelected = i == value
                val isPast = i < value
                val barWidth = if (isSelected) 10.dp else 4.dp
                val barHeight =
                    when {
                        isSelected -> 32.dp
                        isPast -> 24.dp
                        else -> 14.dp
                    }
                val barColor =
                    when {
                        isSelected -> color
                        isPast -> color.copy(alpha = 0.45f)
                        else -> track
                    }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .weight(1f)
                            .clickable { onChange(i) }
                            .padding(horizontal = 2.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .height(barHeight)
                                .width(barWidth)
                                .background(barColor, RoundedCornerShape(4.dp)),
                    )
                    BeeLabelSmall(
                        text = i.toString(),
                        color =
                            if (isSelected) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                        modifier = Modifier.padding(top = BeeSpacing.XS),
                    )
                }
            }
        }
        if (lowLabel != null && highLabel != null) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = BeeSpacing.XS),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BeeLabelSmall(
                    text = lowLabel.uppercase(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                BeeLabelSmall(
                    text = highLabel.uppercase(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
