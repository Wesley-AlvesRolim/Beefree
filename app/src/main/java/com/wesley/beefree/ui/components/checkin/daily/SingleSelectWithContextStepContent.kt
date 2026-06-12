package com.wesley.beefree.ui.components.checkin.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wesley.beefree.domain.checkin.SingleSelectWithContextStep
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeSelectableOption
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeTextArea

@Composable
fun SingleSelectWithContextStepContent(
    spec: SingleSelectWithContextStep,
    selectedId: String?,
    contextValue: String,
    onSelect: (String) -> Unit,
    onContextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    previousObjective: String? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
    ) {
        BeeHeadlineSmall(stringByName(spec.titleKey))
        Spacer(modifier = Modifier.height(BeeSpacing.XS))
        BeeBodyMedium(
            text = stringByName(spec.subtitleKey),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (previousObjective != null) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {
                BeeBodyMedium(
                    text = previousObjective,
                    modifier = Modifier.padding(BeeSpacing.M),
                )
            }
        }
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        spec.options.forEach { option ->
            BeeSelectableOption(
                text = stringByName(option.labelKey),
                isSelected = option.id == selectedId,
                onClick = { onSelect(option.id) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (spec.contextHintKey != null) {
            Spacer(modifier = Modifier.height(BeeSpacing.S))
            BeeTextArea(
                value = contextValue,
                onValueChange = onContextChange,
                placeholder = { BeeBodyMedium(stringByName(spec.contextHintKey)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
