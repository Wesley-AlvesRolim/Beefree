package com.wesley.beefree.ui.screens.helpinterventionscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineLarge
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeechTone
import com.wesley.beefree.ui.components.designsystem.BeeSelectableOption
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.utils.getResIdOrFallback

@Composable
fun ActDirectionStepContent(
    step: HelpInterventionStep.ActDirectionStep,
    selectedValue: String,
    onSelectedChange: (String) -> Unit,
    onAnswerChange: (Any) -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        BeeMascotSpeech(
            speechText = stringResource(R.string.help_intervention_mascot_speech_act_direction),
            tone = BeeMascotSpeechTone.Tertiary,
        )

        BeeHeadlineLarge(stringResource(getResIdOrFallback(context, step.titleKey)))

        Column(
            Modifier.padding(BeeSpacing.M),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            step.options.forEach { option ->
                val selected = selectedValue == option.id
                val optionLabel = stringResource(getResIdOrFallback(context, option.labelKey))

                val icon =
                    when (option.id) {
                        "approximates" -> Icons.AutoMirrored.Filled.Login
                        "distances" -> Icons.AutoMirrored.Filled.Logout
                        else -> Icons.AutoMirrored.Filled.HelpOutline
                    }

                BeeSelectableOption(
                    text = optionLabel,
                    isSelected = selected,
                    onClick = {
                        onSelectedChange(option.id)
                        onAnswerChange(option.id)
                    },
                    indicator = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(BeeSpacing.L),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
