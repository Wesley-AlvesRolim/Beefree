package com.wesley.beefree.ui.screens.helpinterventionscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineLarge
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeScale0to10
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.utils.getResIdOrFallback

@Composable
fun IntensityStepContent(
    step: Any,
    value: Int = 0,
    onValueChange: (Int) -> Unit = {},
    onAnswerChange: (Any) -> Unit,
) {
    val context = LocalContext.current
    val titleKey =
        when (step) {
            is HelpInterventionStep.IntensityStep -> step.titleKey
            is HelpInterventionStep.PostSurfIntensityStep -> step.titleKey
            else -> "unknown"
        }
    val subtitleKey =
        (step as? HelpInterventionStep.IntensityStep)?.subtitleKey
            ?: "help_intervention.intensity_subtitle"

    val speechKey =
        if (step is HelpInterventionStep.PostSurfIntensityStep) {
            R.string.help_intervention_mascot_speech_post_intensity
        } else {
            R.string.help_intervention_mascot_speech_intensity
        }

    Column {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BeeHeadlineLarge(stringResource(getResIdOrFallback(context, titleKey)))

            Spacer(Modifier.height(BeeSpacing.M))

            BeeMascotSpeech(
                speechText = stringResource(speechKey),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = BeeSpacing.M),
            )
        }

        Spacer(Modifier.height(BeeSpacing.M))

        BeeCardSection(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(BeeSpacing.M),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BeeHeadlineLarge(
                    text = value.toString(),
                    color = MaterialTheme.colorScheme.primary,
                )
                BeeBodySmall(
                    text = stringResource(getResIdOrFallback(context, subtitleKey)),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(BeeSpacing.M))

                BeeScale0to10(
                    value = value,
                    onChange = {
                        onValueChange(it)
                        onAnswerChange(it)
                    },
                )
            }
        }
    }
}
