package com.wesley.beefree.ui.screens.checkin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeChip
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeTextArea

@Composable
fun WeeklyCheckinCognitiveDefusionAndSelfAsContextStep(
    defusionChoice: Int?,
    defusionObservation: String,
    onChoiceChange: (Int?) -> Unit,
    onObservationChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    BeeHeadlineMedium(stringResource(R.string.check_in_weekly_defusao_title))
    Spacer(modifier = Modifier.height(BeeSpacing.L))

    BeeBodyMedium(
        text = stringResource(R.string.check_in_weekly_defusao_q),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(BeeSpacing.M))

    val choices =
        listOf(
            0 to R.string.check_in_weekly_defusao_sim,
            1 to R.string.check_in_weekly_defusao_parcial,
            2 to R.string.check_in_weekly_defusao_nao,
        )
    Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
        choices.forEach { (index, labelRes) ->
            BeeChip(
                label = stringResource(labelRes),
                selected = defusionChoice == index,
                onClick = { onChoiceChange(index) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    Spacer(modifier = Modifier.height(BeeSpacing.L))
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    Spacer(modifier = Modifier.height(BeeSpacing.M))

    BeeLabelMedium(
        text = stringResource(R.string.check_in_weekly_defusao_obs_label),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(BeeSpacing.S))
    BeeTextArea(
        value = defusionObservation,
        onValueChange = onObservationChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { BeeBodyMedium(stringResource(R.string.check_in_weekly_defusao_obs_hint)) },
        minLines = 4,
    )

    Spacer(modifier = Modifier.height(BeeSpacing.XL))
    BeeButtonPrimary(
        onClick = onSubmit,
        modifier = Modifier.fillMaxWidth(),
    ) {
        BeeLabelLarge(stringResource(R.string.check_in_weekly_conclude))
    }

    Spacer(modifier = Modifier.height(BeeSpacing.L))

    BeeBodySmall(
        text = stringResource(R.string.check_in_weekly_defusao_quote),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}
