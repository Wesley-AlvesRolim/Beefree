package com.wesley.beefree.ui.screens.helpinterventionscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.CoreValueType
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
    userCoreValueNames: Set<String> = emptySet(),
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        BeeHeadlineLarge(stringResource(getResIdOrFallback(context, step.titleKey)))

        BeeMascotSpeech(
            speechText = stringResource(R.string.help_intervention_mascot_speech_act_values),
            tone = BeeMascotSpeechTone.Tertiary,
        )

        ValuesGrid(
            step = step,
            selectedValue = selectedValue,
            userCoreValueNames = userCoreValueNames,
            onOptionClick = { optionId ->
                onSelectedChange(optionId)
                onCustomValueChange("")
                onAnswerChange(optionId)
            },
        )

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

@Composable
private fun ValuesGrid(
    step: HelpInterventionStep.ActValuesStep,
    selectedValue: String,
    userCoreValueNames: Set<String>,
    onOptionClick: (String) -> Unit,
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
    ) {
        step.predefinedOptions.chunked(2).forEach { rowItems ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
            ) {
                rowItems.forEach { option ->

                    val selected = selectedValue == option.id
                    val isAnUserCoreValue = option.id in userCoreValueNames

                    Column(
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        BeeSelectableOption(
                            text =
                                stringResource(
                                    getResIdOrFallback(context, option.labelKey),
                                ),
                            isSelected = selected,
                            onClick = {
                                onOptionClick(option.id)
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
                            columnArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize(),
                        )

                        if (isAnUserCoreValue) {
                            BeeBodySmall(
                                stringResource(
                                    R.string.help_intervention_user_value,
                                ),
                            )
                        }
                    }
                }

                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

private fun getValueIcon(id: String): ImageVector {
    val type = runCatching { CoreValueType.valueOf(id) }.getOrNull() ?: return Icons.Default.Star
    return when (type) {
        CoreValueType.FAMILY -> Icons.Default.FamilyRestroom
        CoreValueType.FAITH -> Icons.Default.AutoAwesome
        CoreValueType.HONESTY -> Icons.Default.Visibility
        CoreValueType.HEALTH -> Icons.Default.Favorite
        CoreValueType.RELATIONSHIPS -> Icons.Default.People
        CoreValueType.GROWTH -> Icons.AutoMirrored.Filled.TrendingUp
        CoreValueType.WORK -> Icons.Default.Work
        CoreValueType.COMMUNITY -> Icons.Default.Groups
        CoreValueType.LOVE -> Icons.Default.VolunteerActivism
        CoreValueType.FREEDOM -> Icons.Default.AirplanemodeActive
    }
}
