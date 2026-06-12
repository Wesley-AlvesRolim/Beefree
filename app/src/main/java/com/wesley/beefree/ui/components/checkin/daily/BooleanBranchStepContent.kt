package com.wesley.beefree.ui.components.checkin.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wesley.beefree.domain.checkin.BooleanBranchStep
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeSelectableOption
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun BooleanBranchStepContent(
    spec: BooleanBranchStep,
    value: Boolean?,
    onChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
    ) {
        BeeHeadlineSmall(stringByName(spec.titleKey))
        BeeBodyMedium(
            text = stringByName(spec.subtitleKey),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
        ) {
            BeeSelectableOption(
                text = stringByName(spec.yesLabelKey),
                isSelected = value == true,
                onClick = { onChange(true) },
                modifier = Modifier.fillMaxWidth(),
            )
            BeeSelectableOption(
                text = stringByName(spec.noLabelKey),
                isSelected = value == false,
                onClick = { onChange(false) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
