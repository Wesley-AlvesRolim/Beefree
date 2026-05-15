package com.wesley.beefree.ui.components.checkin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun WeeklyCheckinValuesStep(
    coreValues: List<UserCoreValue>,
    valueConnectionLevels: Map<String, Float>,
    onUpdateLevel: (String, Float) -> Unit,
    onNext: () -> Unit,
) {
    BeeHeadlineMedium(stringResource(R.string.check_in_weekly_valores_title))
    Spacer(modifier = Modifier.height(BeeSpacing.S))
    BeeBodyMedium(
        text = stringResource(R.string.check_in_weekly_valores_subtitle),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(BeeSpacing.L))

    if (coreValues.isEmpty()) {
        BeeBodySmall(
            text = stringResource(R.string.check_in_your_values),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    } else {
        coreValues.forEach { value ->
            val level = valueConnectionLevels[value.value.name] ?: 0.5f
            BeeCardSection(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(BeeSpacing.M)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        BeeLabelLarge(
                            text = value.value.name,
                            modifier = Modifier.weight(1f),
                        )
                        BeeLabelSmall(
                            text = "${(level * 100).toInt()}%",
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Slider(
                        value = level,
                        onValueChange = { onUpdateLevel(value.value.name, it) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            Spacer(modifier = Modifier.height(BeeSpacing.S))
        }
    }

    Spacer(modifier = Modifier.height(BeeSpacing.XL))
    BeeButtonPrimary(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth(),
    ) {
        BeeLabelLarge(
            stringResource(R.string.check_in_weekly_continue),
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}
