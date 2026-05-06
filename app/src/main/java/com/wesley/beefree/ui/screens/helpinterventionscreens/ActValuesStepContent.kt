package com.wesley.beefree.ui.screens.helpinterventionscreens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterCenterFocus
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeButtonOutlined
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeechTone
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeTextField

@Composable
fun ActValuesStepContent(
    step: HelpInterventionStep.ActValuesStep,
    selectedValue: String,
    customValue: String,
    onSelectedChange: (String) -> Unit,
    onCustomValueChange: (String) -> Unit,
    onAnswerChange: (Any) -> Unit,
) {
    val context = LocalContext.current

    fun isSelected(optionId: String): Boolean = selectedValue == optionId

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        BeeMascotSpeech(
            speechText = stringResource(R.string.help_intervention_mascot_speech_act_values),
            tone = BeeMascotSpeechTone.Tertiary,
        )

        BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(BeeSpacing.M)) {
                BeeLabelMedium(stringResource(getResIdOrFallback(context, step.titleKey)))
                Spacer(Modifier.height(BeeSpacing.M))

                step.predefinedOptions.forEach { option ->
                    Spacer(Modifier.height(BeeSpacing.S))
                    BeeButtonOutlined(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onSelectedChange(option.id)
                            onCustomValueChange("")
                            onAnswerChange(option.id)
                        },
                    ) {
                        Icon(
                            imageVector = getValueIcon(option.id),
                            contentDescription = option.id,
                            modifier = Modifier.size(BeeSpacing.M),
                            tint = if (isSelected(option.id)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(Modifier.width(BeeSpacing.S))
                        BeeLabelLarge(
                            text = stringResource(getResIdOrFallback(context, option.labelKey)),
                            color = if (isSelected(option.id)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                Spacer(Modifier.height(BeeSpacing.M))

                BeeTextField(
                    value = customValue,
                    onValueChange = {
                        onCustomValueChange(it)
                        if (it.isNotEmpty()) {
                            onSelectedChange("")
                        }
                        onAnswerChange(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        BeeBodySmall(stringResource(R.string.help_intervention_custom_value_other))
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

private fun getValueIcon(id: String): ImageVector =
    when (id) {
        "disciplined" -> Icons.Default.DoneAll
        "present" -> Icons.Default.FilterCenterFocus
        "honest" -> Icons.Default.Visibility
        "healthy" -> Icons.Default.Favorite
        "responsible" -> Icons.Default.AssignmentInd
        "calm" -> Icons.Default.SelfImprovement
        "focused" -> Icons.Default.AdsClick
        "resilient" -> Icons.Default.FitnessCenter
        "selfcontrolled" -> Icons.Default.LockPerson
        "kind_to_self" -> Icons.Default.VolunteerActivism
        else -> Icons.Default.Star
    }
