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
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.ui.components.designsystem.BeeButtonOutlined
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun BodyLocationStepContent(
    step: HelpInterventionStep.BodyLocationStep,
    selected: Set<String>,
    onSelectedChange: (Set<String>) -> Unit,
    onAnswerChange: (Any) -> Unit,
) {
    val context = LocalContext.current

    Column {
        BeeMascotSpeech(
            speechText = stringResource(R.string.help_intervention_mascot_speech_body_location),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BeeSpacing.M),
        )

        Spacer(Modifier.height(BeeSpacing.M))

        BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(BeeSpacing.M)) {
                BeeLabelMedium(stringResource(getResIdOrFallback(context, step.titleKey)))
                Spacer(Modifier.height(BeeSpacing.M))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    step.options.forEach { option ->
                        val isSelected = selected.contains(option.id)

                        Spacer(Modifier.height(BeeSpacing.S))
                        BeeButtonOutlined(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                val newList =
                                    selected.toMutableSet().apply {
                                        if (contains(option.id)) remove(option.id) else add(option.id)
                                    }
                                onSelectedChange(newList)
                                onAnswerChange(newList.toList())
                            },
                        ) {
                            Icon(
                                imageVector = getBodyPartIcon(option.id),
                                contentDescription = option.id,
                                modifier = Modifier.size(BeeSpacing.M),
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            )
                            Spacer(Modifier.width(BeeSpacing.S))
                            BeeLabelLarge(
                                text = stringResource(getResIdOrFallback(context, option.labelKey)),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
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

private fun getResIdOrFallback(
    context: Context,
    key: String,
): Int {
    val resId = context.resources.getIdentifier(key, "string", context.packageName)
    return if (resId != 0) resId else R.string.app_name
}
