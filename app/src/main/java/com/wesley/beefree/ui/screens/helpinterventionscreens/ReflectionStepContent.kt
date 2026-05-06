package com.wesley.beefree.ui.screens.helpinterventionscreens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeChip
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun ReflectionStepContent(
    step: HelpInterventionStep.ReflectionStep,
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
            speechText = stringResource(R.string.help_intervention_mascot_speech_reflection),
        )

        BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(BeeSpacing.M)) {
                BeeLabelMedium(stringResource(getResIdOrFallback(context, step.questionKey)))
                Spacer(Modifier.height(BeeSpacing.M))

                BeeChip(
                    label = stringResource(R.string.help_intervention_reflection_yes),
                    selected = selectedValue == "yes",
                    onClick = {
                        onSelectedChange("yes")
                        onAnswerChange(true)
                    },
                )
                BeeChip(
                    label = stringResource(R.string.help_intervention_reflection_no),
                    selected = selectedValue == "no",
                    onClick = {
                        onSelectedChange("no")
                        onAnswerChange(false)
                    },
                )
            }
        }
    }
}

private fun getResIdOrFallback(
    context: Context,
    key: String,
): Int {
    val resId = context.resources.getIdentifier(key, "string", context.packageName)
    return if (resId != 0) resId else R.string.app_name
}
