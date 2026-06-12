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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.wesley.beefree.domain.checkin.ScaleStep
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeScale0to10
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun ScaleStepContent(
    spec: ScaleStep,
    value: Int,
    onChange: (Int) -> Unit,
    color: Color,
    icon: ImageVector,
    label: String,
    hint: String,
    modifier: Modifier = Modifier,
    decoration: @Composable (Int) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        BeeHeadlineSmall(text = stringByName(spec.titleKey))
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeBodyMedium(
            text = stringByName(spec.subtitleKey),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        decoration(value)
        Slider(
            value = value,
            onChange = onChange,
            color = color,
            icon = icon,
            label = label,
            hint = hint,
            lowLabel = stringByName(spec.lowLabelKey),
            highLabel = stringByName(spec.highLabelKey),
        )
    }
}

@Composable
private fun Slider(
    value: Int,
    onChange: (Int) -> Unit,
    color: Color,
    icon: ImageVector,
    label: String,
    hint: String,
    lowLabel: String = "",
    highLabel: String = "",
) {
    BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(BeeSpacing.M),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = BeeSpacing.XS),
                horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(BeeSpacing.L)
                            .clip(RoundedCornerShape(BeeSpacing.S))
                            .background(color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = color,
                        modifier = Modifier.size(BeeSpacing.M),
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(BeeSpacing.XS),
                ) {
                    BeeBodyMedium(text = label)
                    BeeLabelSmall(text = hint)
                }

                BeeBodyMedium(
                    text = "$value/10",
                    color = color,
                )
            }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = BeeSpacing.S),
                horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BeeScale0to10(
                    value = value,
                    onChange = onChange,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BeeLabelMedium(text = lowLabel)
                BeeLabelMedium(text = highLabel)
            }
        }
    }
}
