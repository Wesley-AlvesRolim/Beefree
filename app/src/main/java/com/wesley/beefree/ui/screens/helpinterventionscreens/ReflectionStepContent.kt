package com.wesley.beefree.ui.screens.helpinterventionscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineLarge
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeSelectableOption
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.utils.getResIdOrFallback
import com.wesley.beefree.utils.resolveStringOrKey

@Composable
fun ReflectionStepContent(
    step: HelpInterventionStep.ReflectionStep,
    selectedValue: String,
    onSelectedChange: (String) -> Unit,
    initialIntensity: Int = 0,
    postSurfIntensity: Int = 0,
    timerSeconds: Int = 0,
    committedAction: String? = null,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BeeHeadlineLarge(stringResource(getResIdOrFallback(context, step.questionKey)))
            Spacer(Modifier.height(BeeSpacing.M))
            BeeMascotSpeech(
                speechText = stringResource(R.string.help_intervention_mascot_speech_reflection),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = BeeSpacing.M),
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(BeeSpacing.M),
        ) {
            BeeSelectableOption(
                text = stringResource(R.string.help_intervention_reflection_yes),
                subtitle = stringResource(R.string.help_intervention_reflection_subtitle_yes),
                isSelected = selectedValue == "yes",
                onClick = {
                    onSelectedChange("yes")
                },
            )
            Spacer(Modifier.height(BeeSpacing.M))
            BeeSelectableOption(
                text = stringResource(R.string.help_intervention_reflection_no),
                subtitle = stringResource(R.string.help_intervention_reflection_subtitle_no),
                isSelected = selectedValue == "no",
                onClick = {
                    onSelectedChange("no")
                },
            )
        }

        BeeCardElevated(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(BeeSpacing.M),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(BeeSpacing.M),
                verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
            ) {
                BeeHeadlineSmall(
                    text = stringResource(R.string.help_intervention_session_summary_header),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                SessionMetricRow(
                    label = stringResource(R.string.help_intervention_session_initial_intensity),
                    value = "$initialIntensity/10",
                )

                SessionMetricRow(
                    label = stringResource(R.string.help_intervention_session_post_surf),
                    value = "$postSurfIntensity/10",
                )

                SessionMetricRow(
                    label = stringResource(R.string.help_intervention_session_total_time),
                    value = formatSeconds(timerSeconds),
                )

                committedAction?.let {
                    if (it.isNotEmpty()) {
                        SessionMetricRow(
                            label = stringResource(R.string.help_intervention_session_action_taken),
                            value = resolveStringOrKey(context, it),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionMetricRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BeeLabelMedium(text = label)
        BeeBodyMedium(text = value)
    }
}

@Composable
private fun formatSeconds(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    val minUnit = stringResource(R.string.help_intervention_timer_format_min)
    val secUnit = stringResource(R.string.help_intervention_timer_format_sec)
    return when {
        minutes > 0 -> "$minutes $minUnit ${secs}$secUnit"
        else -> "${secs}$secUnit"
    }
}
