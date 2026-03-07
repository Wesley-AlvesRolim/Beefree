package com.wesley.beefree.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.wesley.beefree.domain.entities.RelapseHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import java.util.Random

class HomeViewModel : ViewModel() {
    private val _relapseHistory = MutableStateFlow<List<RelapseHistory>>(emptyList())
    val relapseHistory: StateFlow<List<RelapseHistory>> = _relapseHistory.asStateFlow()

    private val _motivationalMessage = MutableStateFlow("")
    val motivationalMessage: StateFlow<String> = _motivationalMessage.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _relapseHistory.value = generateDummyData()
        _motivationalMessage.value = getRandomMotivationalMessage()
    }

    private fun generateDummyData(): List<RelapseHistory> {
        val data = mutableListOf<RelapseHistory>()
        val cal = Calendar.getInstance()
        val random = Random()

        for (i in 0 until 50) {
            val daysAgo = random.nextInt(30)
            val tempCal = cal.clone() as Calendar
            tempCal.add(Calendar.DAY_OF_YEAR, -daysAgo)
            data.add(
                RelapseHistory(
                    id = i,
                    addictionTypeId = i,
                    keywordDetected = "Betano",
                    relapseAt = tempCal.time.time,
                    updatedAt = tempCal.time.time,
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
}
