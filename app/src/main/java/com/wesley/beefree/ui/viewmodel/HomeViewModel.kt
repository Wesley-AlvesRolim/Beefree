package com.wesley.beefree.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wesley.beefree.domain.entities.RelapseHistory
import com.wesley.beefree.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.storage.adapters.db.AppDatabase
import com.wesley.beefree.storage.ports.AddictionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val addictionRepository: AddictionRepository,
) : ViewModel() {
    private val _relapseHistory = MutableStateFlow<List<RelapseHistory>>(emptyList())
    val relapseHistory: StateFlow<List<RelapseHistory>> = _relapseHistory.asStateFlow()

    private val _motivationalMessage = MutableStateFlow("")
    val motivationalMessage: StateFlow<String> = _motivationalMessage.asStateFlow()

    init {
        viewModelScope.launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        withContext(Dispatchers.IO) {
            _relapseHistory.value = getTheLast30DaysOfHistory()
            _motivationalMessage.value = getRandomMotivationalMessage()
        }
    }

    private suspend fun getTheLast30DaysOfHistory(): List<RelapseHistory> {
        val relapseHistory = addictionRepository.getRelapseHistory().first()

        val thirtyDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)

        return relapseHistory
            .filter { it.relapseAt >= thirtyDaysAgo }
            .sortedByDescending { it.relapseAt }
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

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val database = AppDatabase.getDatabase(application)
                    @Suppress("UNCHECKED_CAST")
                    return HomeViewModel(
                        RoomAddictionRepository(
                            database.addictionTypeDao(),
                            database.addictionKeywordDao(),
                            database.relapseHistoryDao(),
                        ),
                    ) as T
                }
            }
    }
}
