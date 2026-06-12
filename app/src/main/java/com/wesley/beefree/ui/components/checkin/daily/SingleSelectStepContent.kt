package com.wesley.beefree.ui.components.checkin.daily

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wesley.beefree.domain.checkin.SingleSelectStep
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeSelectableOption
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun SingleSelectStepContent(
    spec: SingleSelectStep,
    selectedId: String?,
    onSelect: (String) -> Unit,
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
        spec.options.forEach { option ->
            BeeSelectableOption(
                text = stringByName(option.labelKey),
                subtitle = option.descriptionKey?.let { stringByName(it) },
                isSelected = selectedId == option.id,
                onClick = { onSelect(option.id) },
            )
            Spacer(modifier = Modifier.height(BeeSpacing.S))
        }
    }
}
