package com.wesley.beefreepoc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wesley.beefreepoc.ui.theme.BeeFreePOCTheme
import java.util.Calendar
import java.util.Date

@Composable
fun HomeScreen() {
    val dummyData = remember { generateDummyData() }
    val motivationalMessage = remember { getRandomMotivationalMessage() }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Bem-vindo ao BeeFree",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        MotivationalCard(message = motivationalMessage)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Seu progresso nos últimos 30 dias",
            style = MaterialTheme.typography.titleLarge,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
        )

        Heatmap(data = dummyData)

        Spacer(modifier = Modifier.height(32.dp))

        // Placeholder for other stats
        StatsSummary(data = dummyData)
    }
}

@Composable
fun MotivationalCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = Color(0xFFFFD54F), // Bee Yellow
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "\"",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Heatmap(data: List<IndividualVicesMonitoringData>) {
    val countsPerDay =
        remember(data) {
            val counts = mutableMapOf<String, Int>()
            val cal = Calendar.getInstance()
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

            data.forEach {
                val dateStr = sdf.format(it.created_at)
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
                Text("Menos", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.size(4.dp))
                HeatmapSquare(0)
                HeatmapSquare(1)
                HeatmapSquare(3)
                HeatmapSquare(5)
                Spacer(modifier = Modifier.size(4.dp))
                Text("Mais", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun HeatmapSquare(count: Int) {
    val color =
        when {
            count == 0 -> Color.LightGray.copy(alpha = 0.3f)
            count < 2 -> Color(0xFFFFECB3) // Very light yellow
            count < 4 -> Color(0xFFFFD54F) // Medium yellow
            count < 6 -> Color(0xFFFFB300) // Dark yellow
            else -> Color(0xFFF57F17) // Deep orange/yellow
        }

    Box(
        modifier =
            Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color),
    )
}

@Composable
fun StatsSummary(data: List<IndividualVicesMonitoringData>) {
    val total = data.size
    val lastWeekCount =
        remember(data) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -7)
            data.count { it.created_at.after(cal.time) }
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
        Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

data class IndividualVicesMonitoringData(
    val id: String,
    val keyword: String,
    val addition_type: String,
    val created_at: Date,
)

private fun generateDummyData(): List<IndividualVicesMonitoringData> {
    val data = mutableListOf<IndividualVicesMonitoringData>()
    val cal = Calendar.getInstance()
    val random = java.util.Random()

    // Add some random entries in the last 30 days
    for (i in 0 until 50) {
        val daysAgo = random.nextInt(30)
        val tempCal = cal.clone() as Calendar
        tempCal.add(Calendar.DAY_OF_YEAR, -daysAgo)
        data.add(
            IndividualVicesMonitoringData(
                id = i.toString(),
                keyword = "Betano",
                addition_type = "Bets",
                created_at = tempCal.time,
            ),
        )
    }
    return data
}

private fun getRandomMotivationalMessage(): String {
    val messages =
        listOf(
            "Um dia de cada vez. Você está no controle.",
            "Respire fundo. O progresso é melhor que a perfeição.",
            "Cada escolha positiva é um passo para sua liberdade.",
            "Seja gentil consigo mesmo. A jornada é longa, mas vale a pena.",
            "O sucesso é a soma de pequenos esforços repetidos dia após dia.",
            "Você é mais forte do que qualquer impulso passageiro.",
            "Não olhe para trás, você não está indo para lá.",
        )
    return messages.random()
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BeeFreePOCTheme {
        HomeScreen()
    }
}
