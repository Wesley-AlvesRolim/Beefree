package com.wesley.beefree.ui.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.columnSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.LineComponent
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun RiskAssessmentTodayCard(assessments: List<RiskAssessment>) {
    BeeCardSection(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = BeeSpacing.M),
    ) {
        Column(modifier = Modifier.padding(BeeSpacing.M)) {
            BeeHeadlineSmall(stringResource(R.string.home_risk_assessment_title))

            if (assessments.isEmpty()) {
                Spacer(Modifier.height(BeeSpacing.S))
                BeeBodySmall(
                    text = stringResource(R.string.home_risk_assessment_empty),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Spacer(Modifier.height(BeeSpacing.S))
                RiskBarChart(assessments = assessments)
            }
        }
    }
}

@Composable
private fun RiskBarChart(assessments: List<RiskAssessment>) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val modelProducer = remember { CartesianChartModelProducer() }
    val timeLabels = assessments.map { formatTimeWindowShort(it) }

    LaunchedEffect(assessments) {
        modelProducer.runTransaction {
            columnSeries {
                series(*assessments.map { it.riskScore.toFloat() }.toTypedArray())
            }
        }
    }

    val axisFormatter =
        CartesianValueFormatter { _, x, _ ->
            timeLabels.getOrElse(x.toInt()) { "" }
        }

    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider =
                    ColumnCartesianLayer.ColumnProvider.series(
                        LineComponent(
                            thickness = BeeSpacing.S,
                            fill = Fill(primaryColor),
                        ),
                    ),
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(valueFormatter = axisFormatter),
        ),
        modelProducer = modelProducer,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(BeeSpacing.XXL * 3),
    )
}

private fun formatTimeWindowShort(assessment: RiskAssessment): String {
    val timeWindowStart = assessment.timeWindowStart
    if (timeWindowStart != null) {
        val initialHourForTimeWindow =
            Calendar
                .getInstance()
                .apply {
                    timeInMillis = timeWindowStart
                }.get(Calendar.HOUR_OF_DAY)
        return "$initialHourForTimeWindow:00"
    }
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(assessment.createdAt))
}
