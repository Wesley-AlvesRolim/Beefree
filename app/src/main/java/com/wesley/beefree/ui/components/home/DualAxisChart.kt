package com.wesley.beefree.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun DualAxisChart(
    anxietySeries: List<Float>,
    satisfactionSeries: List<Float>,
    modifier: Modifier = Modifier,
) {
    if (anxietySeries.isEmpty()) {
        BeeBodySmall(stringResource(R.string.emotional_chart_empty))
        return
    }

    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val primaryColor = MaterialTheme.colorScheme.primary

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(anxietySeries, satisfactionSeries) {
        modelProducer.runTransaction {
            lineSeries {
                series(*anxietySeries.toTypedArray())
                series(*satisfactionSeries.toTypedArray())
            }
        }
    }

    Column(modifier = modifier) {
        CartesianChartHost(
            rememberCartesianChart(
                rememberLineCartesianLayer(
                    lineProvider =
                        LineCartesianLayer.LineProvider.series(
                            LineCartesianLayer.rememberLine(
                                fill =
                                    LineCartesianLayer.LineFill.single(
                                        Fill(tertiaryColor),
                                    ),
                            ),
                            LineCartesianLayer.rememberLine(
                                fill =
                                    LineCartesianLayer.LineFill.single(
                                        Fill(primaryColor),
                                    ),
                            ),
                        ),
                ),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(),
            ),
            modelProducer = modelProducer,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(BeeSpacing.XXL * 3),
        )

        Spacer(Modifier.height(BeeSpacing.S))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
        ) {
            LegendDot(
                color = tertiaryColor,
                label = stringResource(R.string.emotional_chart_anxiety),
            )
            if (satisfactionSeries.any { it > 0f }) {
                LegendDot(
                    color = primaryColor,
                    label = stringResource(R.string.emotional_chart_satisfaction),
                )
            }
        }
    }
}

@Composable
private fun LegendDot(
    color: Color,
    label: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(width = BeeSpacing.S, height = BeeSpacing.XS),
            shape = RoundedCornerShape(BeeSpacing.XS),
            color = color,
        ) {}
        Spacer(Modifier.width(BeeSpacing.XS))
        BeeBodySmall(label)
    }
}
