package com.wesley.beefree.ui.screens.helpinterventionscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wesley.beefree.R
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineLarge
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeechTone
import com.wesley.beefree.ui.components.designsystem.BeeSelectableOption
import com.wesley.beefree.ui.components.designsystem.BeeSelectableOptionDirection
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeTextField
import com.wesley.beefree.utils.getResIdOrFallback

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
        BeeHeadlineLarge(stringResource(getResIdOrFallback(context, step.titleKey)))

        BeeMascotSpeech(
            speechText = stringResource(R.string.help_intervention_mascot_speech_act_values),
            tone = BeeMascotSpeechTone.Tertiary,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 1000.dp),
            contentPadding = PaddingValues(BeeSpacing.M),
            horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
            userScrollEnabled = false,
        ) {
            items(step.predefinedOptions) { option ->
                val selected = isSelected(option.id)
                BeeSelectableOption(
                    text = stringResource(getResIdOrFallback(context, option.labelKey)),
                    isSelected = selected,
                    onClick = {
                        onSelectedChange(option.id)
                        onCustomValueChange("")
                        onAnswerChange(option.id)
                    },
                    indicator = {
                        Icon(
                            imageVector = getValueIcon(option.id),
                            contentDescription = option.id,
                            modifier = Modifier.size(BeeSpacing.M),
                        )
                    },
                    textAlign = TextAlign.Center,
                    direction = BeeSelectableOptionDirection.Column,
                    columnAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

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
