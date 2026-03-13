package com.wesley.beefree.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.wesley.beefree.domain.entities.RelapseHistory
import java.util.Calendar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Heatmap(data: List<RelapseHistory>) {
    val countsPerDay =
        remember(data) {
            val counts = mutableMapOf<String, Int>()
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

            data.forEach {
                val dateStr = sdf.format(it.relapseAt)
                counts[dateStr] = counts.getOrDefault(dateStr, 0) + 1
            }
            counts
        }

    val last30Days =
        remember {
            val dates = mutableListOf<String>()
            val cal = Calendar.getInstance()
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

            for (i in 29 downTo 0) {
                val tempCal = cal.clone() as Calendar
                tempCal.add(Calendar.DAY_OF_YEAR, -i)
                dates.add(sdf.format(tempCal.time))
            }
            dates
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxItemsInEachRow = 7,
            ) {
                last30Days.forEach { dateStr ->
                    val count = countsPerDay[dateStr] ?: 0
                    HeatmapSquare(count = count)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Legenda:", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.size(8.dp))
                HeatmapSquare(0)
                Spacer(modifier = Modifier.size(4.dp))
                Text("Sem recaída", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.size(12.dp))
                HeatmapSquare(1)
                Spacer(modifier = Modifier.size(4.dp))
                Text("Recaída", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun HeatmapSquare(count: Int) {
    val color =
        if (count == 0) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outlineVariant
        }

    Box(
        modifier =
            Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color),
    )
}
