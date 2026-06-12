package com.wesley.beefree.ui.viewmodel

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wesley.beefree.R
import com.wesley.beefree.domain.repository.ports.DataExportSharer
import com.wesley.beefree.infrastructure.logging.AndroidLogger
import com.wesley.beefree.infrastructure.logging.Logger
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.adapters.db.exporters.AndroidDataExportSharer
import com.wesley.beefree.infrastructure.storage.adapters.db.exporters.FileDatabaseExporter
import com.wesley.beefree.infrastructure.storage.adapters.db.exporters.SqlDatabaseExporterStrategy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

enum class SettingsUiError(
    @StringRes val messageRes: Int,
) {
    EXPORT_FAILED(R.string.export_data_error),
}

class SettingsViewModel(
    private val dataExportSharer: DataExportSharer,
    private val logger: Logger = AndroidLogger,
) : ViewModel() {
    private val _errorMessage = MutableStateFlow<SettingsUiError?>(null)
    val errorMessage = _errorMessage.asSharedFlow()

    fun exportData() {
        viewModelScope.launch {
            try {
                dataExportSharer.shareExportedData()
            } catch (error: Exception) {
                logger.e(TAG, "Failed to export data", error)
                _errorMessage.value = SettingsUiError.EXPORT_FAILED
            }
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
                        dataExportSharer = AndroidDataExportSharer(appContext, exporter, exporterStrategy),
                    ) as T
                }
            }
    }
}
