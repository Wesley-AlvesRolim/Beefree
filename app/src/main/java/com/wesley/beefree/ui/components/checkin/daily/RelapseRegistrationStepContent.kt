package com.wesley.beefree.ui.components.checkin.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.checkin.RelapseRegistrationStep
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeMultiSelectOption
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeTextArea
import com.wesley.beefree.ui.components.designsystem.BeeTimePickerField

@Composable
fun RelapseRegistrationStepContent(
    spec: RelapseRegistrationStep,
    selectedHour: Int?,
    selectedMinute: Int?,
    selectedTriggers: List<String>,
    contextValue: String,
    onTimeSelect: (Int, Int) -> Unit,
    onTriggerToggle: (String) -> Unit,
    onContextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        BeeHeadlineSmall(stringByName(spec.titleKey))
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeBodyMedium(
            text = stringByName(spec.subtitleKey),
        )
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BeeBodySmall(
                text = stringResource(R.string.daily_checkin_relapse_time_label),
            )
            BeeBodySmall(
                text = stringResource(R.string.daily_checkin_relapse_time_hint),
            )
        }
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeTimePickerField(
            selectedHour = selectedHour,
            selectedMinute = selectedMinute,
            onTimeSelect = onTimeSelect,
            modifier = Modifier.fillMaxWidth(),
            emptyText = stringResource(R.string.daily_checkin_relapse_time_empty),
        )
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        BeeBodySmall(
            text = stringResource(R.string.daily_checkin_relapse_triggers_label),
        )
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        spec.triggerOptions.forEach { option ->
            Spacer(modifier = Modifier.height(BeeSpacing.S))
            BeeMultiSelectOption(
                text = stringByName(option.labelKey),
                isSelected = option.id in selectedTriggers,
                onClick = { onTriggerToggle(option.id) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        BeeTextArea(
            value = contextValue,
            onValueChange = onContextChange,
            placeholder = { BeeBodyMedium(stringResource(R.string.daily_checkin_relapse_context_hint)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
