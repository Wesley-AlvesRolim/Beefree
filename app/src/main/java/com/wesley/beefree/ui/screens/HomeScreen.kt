package com.wesley.beefree.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesley.beefree.domain.entities.RelapseHistory
import com.wesley.beefree.ui.components.Heatmap
import com.wesley.beefree.ui.components.MotivationalCard
import com.wesley.beefree.ui.components.StatsSummary
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val relapseHistory by viewModel.relapseHistory.collectAsState()
    val motivationalMessage by viewModel.motivationalMessage.collectAsState()

    HomeScreenContent(
        relapseHistory = relapseHistory,
        motivationalMessage = motivationalMessage,
    )
}

@Composable
fun HomeScreenContent(
    relapseHistory: List<RelapseHistory>,
    motivationalMessage: String,
) {
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

        Heatmap(data = relapseHistory)

        Spacer(modifier = Modifier.height(32.dp))

        StatsSummary(data = relapseHistory)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BeeFreeTheme {
        HomeScreenContent(
            relapseHistory = emptyList(),
            motivationalMessage = "Um dia de cada vez. Você está no controle.",
        )
    }
}
