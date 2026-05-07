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
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.RecordVoiceOver
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
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineLarge
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeMultiSelectOption
import com.wesley.beefree.ui.components.designsystem.BeeSelectableOptionDirection
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.utils.getResIdOrFallback

@Composable
fun BodyLocationStepContent(
    step: HelpInterventionStep.BodyLocationStep,
    selected: Set<String>,
    onSelectedChange: (Set<String>) -> Unit,
    onAnswerChange: (Any) -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        BeeHeadlineLarge(stringResource(getResIdOrFallback(context, step.titleKey)))

        BeeMascotSpeech(
            speechText = stringResource(R.string.help_intervention_mascot_speech_body_location),
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
            items(step.options) { option ->
                val isSelected = selected.contains(option.id)

                BeeMultiSelectOption(
                    text = stringResource(getResIdOrFallback(context, option.labelKey)),
                    isSelected = isSelected,
                    onClick = {
                        val newList =
                            selected.toMutableSet().apply {
                                if (contains(option.id)) remove(option.id) else add(option.id)
                            }
                        onSelectedChange(newList)
                        onAnswerChange(newList.toList())
                    },
                    indicator = {
                        Icon(
                            imageVector = getBodyPartIcon(option.id),
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
    }
}

@Composable
private fun getBodyPartIcon(id: String): ImageVector =
    when (id) {
        "chest" -> Icons.Default.Favorite
        "throat" -> Icons.Default.RecordVoiceOver
        "belly" -> Icons.Default.AccessibilityNew
        "hands" -> Icons.Default.PanTool
        "head" -> Icons.Default.Face
        else -> Icons.Default.MoreHoriz
    }
