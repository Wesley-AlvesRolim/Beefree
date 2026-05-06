package com.wesley.beefree.ui.screens.helpinterventionscreens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.BreathingPhaseEnum
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeCardElevated
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineLarge
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import kotlin.math.PI
import kotlin.math.cos

internal val phaseRangesForChart =
    mapOf(
        BreathingPhaseEnum.INHALE to (0f to 0.4f),
        BreathingPhaseEnum.HOLD to (0.4f to 0.7f),
        BreathingPhaseEnum.EXHALE to (0.7f to 1f),
    )

@Composable
fun BreathingOrb(
    phase: BreathingPhaseEnum,
    secondsLeft: Int,
    modifier: Modifier = Modifier,
) {
    val orbScale by animateFloatAsState(
        targetValue = if (phase == BreathingPhaseEnum.EXHALE) 0.6f else 1.0f,
        animationSpec =
            tween(
                durationMillis =
                    when (phase) {
                        BreathingPhaseEnum.INHALE -> 4000
                        BreathingPhaseEnum.HOLD -> 200
                        BreathingPhaseEnum.EXHALE -> 8000
                    },
            ),
        label = "orbScale",
    )
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val surfaceContainer = MaterialTheme.colorScheme.surfaceContainer
    val surfaceLow = MaterialTheme.colorScheme.surfaceContainerLow
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer

    Box(
        modifier =
            modifier
                .size(220.dp)
                .aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        Surface(shape = CircleShape, color = surfaceLow, modifier = Modifier.fillMaxSize()) {}
        Surface(
            shape = CircleShape,
            color = surfaceContainer,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(22.dp),
        ) {}
        Surface(
            shape = CircleShape,
            color = primaryContainer,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(44.dp)
                    .scale(orbScale),
        ) {}
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BeeLabelMedium(stringResource(phase.labelRes), color = onPrimaryContainer)
            BeeHeadlineLarge(text = "%02d".format(secondsLeft), color = onPrimaryContainer)
            BeeLabelSmall(stringResource(R.string.urge_surfing_seconds), color = onPrimaryContainer)
        }
    }
}

@Composable
fun WaveCard(
    phase: BreathingPhaseEnum,
    secondsLeft: Int,
    cycleCount: Int,
) {
    val progress = (phase.durationSeconds - secondsLeft).toFloat() / phase.durationSeconds
    BeeCardElevated(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(BeeSpacing.M)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BeeLabelSmall(stringResource(R.string.urge_surfing_cycle_label, cycleCount + 1))
                BeeBodySmall(
                    "${phase.durationSeconds - secondsLeft}/${phase.durationSeconds}s",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.height(BeeSpacing.S))
            WaveVisualizationCanvas(
                phase = phase,
                progress = progress,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(BeeSpacing.XXL * 2),
            )
            Spacer(Modifier.height(BeeSpacing.S))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BeeLabelSmall(
                    stringResource(R.string.urge_surfing_phase_inhale),
                    color =
                        if (phase == BreathingPhaseEnum.INHALE) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                )
                BeeLabelSmall(
                    stringResource(R.string.urge_surfing_phase_hold),
                    color =
                        if (phase == BreathingPhaseEnum.HOLD) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                )
                BeeLabelSmall(
                    stringResource(R.string.urge_surfing_phase_exhale),
                    color =
                        if (phase == BreathingPhaseEnum.EXHALE) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                )
            }
        }
    }
}

@Composable
fun WaveVisualizationCanvas(
    phase: BreathingPhaseEnum,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val progressPercentageCoerce = progress.coerceIn(0f, 1f)
    val (start, end) = phaseRangesForChart.getValue(phase)
    val position = start + (end - start) * progressPercentageCoerce
    val animatedPosition by animateFloatAsState(
        targetValue = position,
        animationSpec =
            if (progressPercentageCoerce == 0f && phase == BreathingPhaseEnum.INHALE) {
                snap()
            } else {
                tween(
                    durationMillis = 1000,
                    easing = LinearEasing,
                )
            },
        label = stringResource(R.string.urge_surfing_title),
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val amplitude = height * 0.25f
        val centerY = height / 2f
        val waveCount = 3f

        val wavePathLine =
            Path().apply {
                val steps = 200
                for (i in 0..steps) {
                    val t = i.toFloat() / steps
                    val x = t * width

                    val angle = t * 2f * PI.toFloat() * waveCount
                    val centerY = size.height / 2f
                    val amplitude = size.height * 0.25f
                    val y = centerY + cos(angle) * amplitude

                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                }
            }

        drawPath(
            path = wavePathLine,
            color = primaryColor,
            style = Stroke(width = 4.dp.toPx()),
        )

        val wavePathAreaFill =
            Path().apply {
                addPath(wavePathLine)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

        drawPath(
            path = wavePathAreaFill,
            color = primaryColor.copy(alpha = 0.15f),
        )

        val ballX = animatedPosition * width
        val angle = animatedPosition * 2f * PI.toFloat() * waveCount
        val ballY = centerY + cos(angle) * amplitude

        drawCircle(
            color = primaryColor,
            radius = 8.dp.toPx(),
            center = Offset(ballX, ballY),
        )
    }
}
