package com.wesley.beefree.ui.screens.checkin.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge

@Composable
fun DailyCheckInSubmitButton(
    enabled: Boolean,
    onSubmit: () -> Unit,
) {
    BeeButtonPrimary(
        onClick = { onSubmit() },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
    ) {
        BeeLabelLarge(stringResource(R.string.check_in_log_button))
    }
}
