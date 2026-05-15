package com.wesley.beefree.ui.screens.helpinterventionscreens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.BreathingPhaseEnum
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineLarge
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.utils.getResIdOrFallback

@Composable
fun UrgeSurfingStepContent(
    step: HelpInterventionStep.UrgeSurfingStep,
    meditationTextIndex: Int,
    phase: BreathingPhaseEnum,
    secondsLeft: Int,
    cycleCount: Int,
    onAdvanceMeditation: () -> Unit,
    onStartBreathing: () -> Unit,
    onAnswerChange: (Any) -> Unit,
) {
    LaunchedEffect(Unit) {
        onStartBreathing()
    }

    val context = LocalContext.current

    LaunchedEffect(phase) {
        if (meditationTextIndex < step.meditationStepKeys.size - 1) {
            onAdvanceMeditation()
        } else {
            onAnswerChange(true)
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = BeeSpacing.L),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BeeHeadlineLarge(
                stringResource(R.string.urge_surfing_headline),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(BeeSpacing.S))
            BeeBodyMedium(
                stringResource(R.string.urge_surfing_body),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        BreathingOrb(phase = phase, secondsLeft = secondsLeft)

        BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier =
                    Modifier
                        .height(BeeSpacing.XXL * 2f)
                        .padding(BeeSpacing.S)
                        .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AnimatedContent(
                    targetState = meditationTextIndex,
                    label = "meditation_step",
                    modifier = Modifier.fillMaxWidth(),
                ) { index ->
                    if (index < step.meditationStepKeys.size) {
                        BeeBodyMedium(
                            text =
                                stringResource(
                                    getResIdOrFallback(context, step.meditationStepKeys[index]),
                                ),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }

        WaveCard(
            phase = phase,
            secondsLeft = secondsLeft,
            cycleCount = cycleCount,
        )
    }
}
