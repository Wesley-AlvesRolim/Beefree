package com.wesley.beefree.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.AddictionCategoryEnum
import com.wesley.beefree.domain.repository.ports.AddictionRepository
import com.wesley.beefree.domain.repository.ports.DatabaseExporterStrategy
import com.wesley.beefree.infrastructure.logging.AndroidLogger
import com.wesley.beefree.infrastructure.logging.Logger
import com.wesley.beefree.infrastructure.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.adapters.db.exporters.FileDatabaseExporter
import com.wesley.beefree.infrastructure.storage.adapters.db.exporters.SqlDatabaseExporterStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val context: Context,
    private val addictionRepository: AddictionRepository,
    private val exporter: FileDatabaseExporter,
    private val exporterStrategy: DatabaseExporterStrategy,
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
                val (mimeType, uri) = withContext(Dispatchers.IO) { createExportFile() }
                val intent =
                    Intent(Intent.ACTION_SEND).apply {
                        type = mimeType
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                val chooser =
                    Intent.createChooser(intent, context.getString(R.string.settings_export_data))
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooser)
            } catch (error: Exception) {
                logger.e(TAG, "Failed to export data", error)
                _errorMessage.value = context.getString(R.string.export_data_error)
            }
        }
    }

    private fun createExportFile(): Pair<String, Uri> {
        val file = exporter.export(context, exporterStrategy)
        val uri =
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file,
            )
        return Pair(exporterStrategy.getMimeType(), uri)
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
                        context = appContext,
                        addictionRepository =
                            RoomAddictionRepository(
                                db.addictionCategoryDao(),
                                db.relapseRecordDao(),
                            ),
                        exporter = exporter,
                        exporterStrategy = exporterStrategy,
                    ) as T
                }
            }
    }
}
