package com.wesley.beefree.ui.screens.checkin.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeChipTag
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeTextArea

@Composable
fun WeeklyCheckinCommittedActionStep(
    text: String,
    onTextChange: (String) -> Unit,
    onNext: () -> Unit,
) {
    BeeHeadlineMedium(stringResource(R.string.check_in_weekly_acao_title))
    Spacer(modifier = Modifier.height(BeeSpacing.S))
    BeeBodyMedium(
        text = stringResource(R.string.check_in_weekly_acao_subtitle),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(BeeSpacing.L))

    BeeTextArea(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { BeeBodyMedium(stringResource(R.string.check_in_weekly_acao_hint)) },
        minLines = 5,
    )
    Spacer(modifier = Modifier.height(BeeSpacing.S))
    BeeChipTag(label = stringResource(R.string.check_in_weekly_acao_tag))

    Spacer(modifier = Modifier.height(BeeSpacing.XL))
    BeeButtonPrimary(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth(),
    ) {
        BeeLabelLarge(stringResource(R.string.check_in_weekly_continue))
    }
}
