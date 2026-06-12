package com.wesley.beefree.ui.screens.helpinterventionscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wesley.beefree.R
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeChipTag
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.utils.getResIdOrFallback
import kotlinx.coroutines.delay

@Composable
fun TimerStepContent(
    step: HelpInterventionStep.TimerStep,
    selectedAction: String?,
    secondsLeft: Int,
    timerStarted: Boolean,
    timerCompleted: Boolean,
    onTimerTick: (Int) -> Unit,
    onTimerStart: () -> Unit,
    onAnswerChange: (Any) -> Unit,
) {
    val context = LocalContext.current
    val cycledSeconds = secondsLeft % step.durationSeconds
    val progress = cycledSeconds.toFloat() / step.durationSeconds.toFloat()

    LaunchedEffect(Unit) {
        onTimerStart()
    }

    LaunchedEffect(secondsLeft, timerStarted) {
        if (timerStarted) {
            delay(1000)
            onTimerTick(secondsLeft + 1)
        }
    }

    LaunchedEffect(secondsLeft, timerStarted, timerCompleted) {
        if (shouldAutoCompleteTimer(secondsLeft, timerStarted, timerCompleted)) {
            onAnswerChange(true)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        BeeMascotSpeech(
            speechText = stringResource(R.string.help_intervention_mascot_speech_timer),
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
        ) {
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = BeeSpacing.XS,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(BeeSpacing.XS),
                ) {
                    BeeHeadlineSmall(formatElapsed(secondsLeft))
                    BeeLabelMedium(stringResource(R.string.help_intervention_timer_remaining))
                }
            }

            BeeChipTag(
                label = "EM CURSO",
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            BeeCardSection(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(BeeSpacing.M),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
                ) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Schedule,
                                contentDescription = stringResource(R.string.help_intervention_timer_icon),
                            )
                        }
                    }

                    selectedAction?.let {
                        val resId = getResIdOrFallback(context, it)
                        if (resId == R.string.app_name) {
                            BeeBodyMedium(it)
                        } else {
                            BeeBodyMedium(stringResource(resId))
                        }
                    }
                }
            }
        }
    }
}

internal fun shouldAutoCompleteTimer(
    secondsElapsed: Int,
    timerStarted: Boolean,
    timerCompleted: Boolean,
): Boolean = timerStarted && !timerCompleted && secondsElapsed >= 10

private fun formatElapsed(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%d:%02d".format(minutes, remainingSeconds)
}
