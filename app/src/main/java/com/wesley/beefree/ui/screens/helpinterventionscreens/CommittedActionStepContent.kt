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
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeChip
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeechTone
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeTextField

@Composable
fun CommittedActionStepContent(
    titleKey: String,
    suggestions: List<Any>,
    selectedValue: String,
    customValue: String,
    speechTone: BeeMascotSpeechTone = BeeMascotSpeechTone.Primary,
    speechKey: String? = null,
    onSelectedChange: (String) -> Unit,
    onCustomValueChange: (String) -> Unit,
    onAnswerChange: (Any) -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        speechKey?.let {
            BeeMascotSpeech(
                speechText = stringResource(getResIdOrFallback(context, it)),
                tone = speechTone,
            )
        }

        BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(BeeSpacing.M)) {
                BeeLabelMedium(stringResource(getResIdOrFallback(context, titleKey)))
                Spacer(Modifier.height(BeeSpacing.M))

                suggestions.forEach { suggestion ->
                    val labelKey =
                        when (suggestion) {
                            is HelpInterventionStep.ActCommittedActionStep ->
                                suggestion.titleKey
                            is HelpInterventionStep.TccActionStep ->
                                suggestion.titleKey
                            else -> "unknown"
                        }
                    BeeChip(
                        label = stringResource(getResIdOrFallback(context, labelKey)),
                        selected = selectedValue == labelKey,
                        onClick = {
                            onSelectedChange(labelKey)
                            onCustomValueChange("")
                            onAnswerChange(labelKey)
                        },
                    )
                }

                Spacer(Modifier.height(BeeSpacing.M))
                BeeTextField(
                    value = customValue,
                    onValueChange = {
                        onCustomValueChange(it)
                        onAnswerChange(it)
                    },
                    placeholder = { BeeBodySmall(stringResource(R.string.help_intervention_custom_action_other)) },
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
