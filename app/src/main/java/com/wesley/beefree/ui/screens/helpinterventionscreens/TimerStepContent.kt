package com.wesley.beefree.ui.screens.helpinterventionscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeech
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import kotlinx.coroutines.delay

@Composable
fun TimerStepContent(
    step: HelpInterventionStep.TimerStep,
    secondsLeft: Int,
    timerStarted: Boolean,
    onTimerTick: (Int) -> Unit,
    onTimerStart: () -> Unit,
    onAnswerChange: (Any) -> Unit,
) {
    LaunchedEffect(Unit) {
        onTimerStart()
    }

    LaunchedEffect(secondsLeft, timerStarted) {
        if (timerStarted && secondsLeft < step.durationSeconds) {
            delay(1000)
            onTimerTick(secondsLeft + 1)
        } else if (timerStarted && secondsLeft >= step.durationSeconds) {
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

        BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(BeeSpacing.M),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
            ) {
                BeeBodyMedium(stringResource(R.string.help_intervention_timer_title))
                BeeLabelMedium(stringResource(R.string.help_intervention_timer_remaining))
                BeeHeadlineSmall("${secondsLeft / 60}:${"%02d".format(secondsLeft % 60)}")
            }
        }
    }
}
