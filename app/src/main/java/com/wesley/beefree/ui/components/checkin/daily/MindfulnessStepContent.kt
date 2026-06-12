package com.wesley.beefree.ui.components.checkin.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.domain.checkin.MindfulnessStep
import com.wesley.beefree.domain.entities.BreathingPhaseEnum
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineLarge
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.screens.helpinterventionscreens.BreathingOrb
import com.wesley.beefree.ui.screens.helpinterventionscreens.WaveCard
import kotlinx.coroutines.delay

@Composable
fun MindfulnessStepContent(
    step: MindfulnessStep,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var phase by remember { mutableStateOf(BreathingPhaseEnum.INHALE) }
    var secondsLeft by remember { mutableIntStateOf(BreathingPhaseEnum.INHALE.durationSeconds) }
    var cycleCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        var completed = 0
        outer@ while (completed < step.cycles) {
            for (p in BreathingPhaseEnum.entries) {
                phase = p
                var secs = p.durationSeconds
                secondsLeft = secs
                while (secs > 0) {
                    delay(1000)
                    secs--
                    secondsLeft = secs
                }
                if (p == BreathingPhaseEnum.EXHALE) {
                    completed++
                    cycleCount = completed
                    if (completed >= step.cycles) break@outer
                }
            }
        }
        onDone()
    }

    Column(
        modifier =
            modifier
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

        WaveCard(
            phase = phase,
            secondsLeft = secondsLeft,
            cycleCount = cycleCount,
        )
    }
}
