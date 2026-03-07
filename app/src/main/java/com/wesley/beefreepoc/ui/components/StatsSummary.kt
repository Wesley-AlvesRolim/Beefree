package com.wesley.beefreepoc.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.wesley.beefreepoc.domain.entities.RelapseHistory
import java.util.Calendar

@Composable
fun StatsSummary(data: List<RelapseHistory>) {
    val total = data.size
    val lastWeekCount =
        remember(data) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -7)
            data.count { it.relapseAt > cal.time.time }
        }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        StatItem(label = "Total (30d)", value = total.toString())
        StatItem(label = "Esta Semana", value = lastWeekCount.toString())
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}
