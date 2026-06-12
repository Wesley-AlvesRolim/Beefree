package com.wesley.beefree.ui.viewmodel

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.AddictionCategoryEnum
import com.wesley.beefree.domain.repository.ports.AddictionRepository
import com.wesley.beefree.domain.repository.ports.DataExportSharer
import com.wesley.beefree.infrastructure.logging.AndroidLogger
import com.wesley.beefree.infrastructure.logging.Logger
import com.wesley.beefree.infrastructure.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.adapters.db.exporters.AndroidDataExportSharer
import com.wesley.beefree.infrastructure.storage.adapters.db.exporters.FileDatabaseExporter
import com.wesley.beefree.infrastructure.storage.adapters.db.exporters.SqlDatabaseExporterStrategy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val addictionRepository: AddictionRepository,
    private val dataExportSharer: DataExportSharer,
    private val logger: Logger = AndroidLogger,
) : ViewModel() {
    private val _isAdultMonitoringEnabled = MutableStateFlow(true)
    val isAdultMonitoringEnabled: StateFlow<Boolean> = _isAdultMonitoringEnabled.asStateFlow()

    private val _isBetsMonitoringEnabled = MutableStateFlow(false)
    val isBetsMonitoringEnabled: StateFlow<Boolean> = _isBetsMonitoringEnabled.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asSharedFlow()

    init {
        viewModelScope.launch {
            addictionRepository.getAllAddictionCategories().collect { types ->
                types.find { it.name == AddictionCategoryEnum.ADULT_CONTENT.label }?.let {
                    _isAdultMonitoringEnabled.value = it.isMonitoringEnabled
                }
                types.find { it.name == AddictionCategoryEnum.BETS.label }?.let {
                    _isBetsMonitoringEnabled.value = it.isMonitoringEnabled
                }
            }
        }
    }

    fun exportData() {
        viewModelScope.launch {
            try {
                dataExportSharer.shareExportedData()
            } catch (error: Exception) {
                logger.e(TAG, "Failed to export data", error)
                _errorMessage.value = context.getString(R.string.export_data_error)
            }
        }
    }

    fun toggleAdultMonitoring() {
        viewModelScope.launch {
            val type =
                addictionRepository
                    .getAllAddictionCategories()
                    .first()
                    .find { it.name == AddictionCategoryEnum.ADULT_CONTENT.label } ?: return@launch
            addictionRepository.updateAddictionCategory(type.copy(isMonitoringEnabled = !type.isMonitoringEnabled))
        }
    }

    fun toggleBetsMonitoring() {
        viewModelScope.launch {
            val type =
                addictionRepository
                    .getAllAddictionCategories()
                    .first()
                    .find { it.name == AddictionCategoryEnum.BETS.label } ?: return@launch
            addictionRepository.updateAddictionCategory(type.copy(isMonitoringEnabled = !type.isMonitoringEnabled))
        }
    }

    fun resetError() {
        _errorMessage.value = null
    }

    companion object {
        private const val TAG = "SettingsViewModel"

        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val appContext = context.applicationContext
                    val db = AppDatabase.getDatabase(appContext)
                    val exporter = FileDatabaseExporter()
                    val exporterStrategy = SqlDatabaseExporterStrategy(db)
                    @Suppress("UNCHECKED_CAST")
                    return SettingsViewModel(
                        addictionRepository =
                            RoomAddictionRepository(
                                db.addictionCategoryDao(),
                                db.relapseRecordDao(),
                            ),
                        dataExportSharer = AndroidDataExportSharer(appContext, exporter, exporterStrategy),
                    ) as T
                }
            }
    }
}
