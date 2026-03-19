package com.wesley.beefree.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wesley.beefree.R
import com.wesley.beefree.infrastructure.services.AccessibilityServiceActivity
import com.wesley.beefree.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.storage.adapters.db.AppDatabase
import com.wesley.beefree.storage.adapters.db.exporters.FileDatabaseExporter
import com.wesley.beefree.storage.adapters.db.exporters.SqlDatabaseExporterStrategy
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository
import com.wesley.beefree.utils.AccessibilityUtils
import com.wesley.beefree.utils.OverlayUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val context: Context,
    private val storageRepository: KeyValueStorageRepository,
) : ViewModel() {
    private val _isAccessibilityServiceEnabled = MutableStateFlow(false)
    val isAccessibilityServiceEnabled: StateFlow<Boolean> =
        _isAccessibilityServiceEnabled.asStateFlow()

    private val _isAccessibilityServiceStarted = MutableStateFlow(false)
    val isAccessibilityServiceStarted: StateFlow<Boolean> =
        _isAccessibilityServiceStarted.asStateFlow()

    private val _isOverlayPermissionEnabled = MutableStateFlow(false)
    val isOverlayPermissionEnabled: StateFlow<Boolean> = _isOverlayPermissionEnabled.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asSharedFlow()

    init {
        updateStatuses()
    }

    fun exportData() {
        try {
            val database = AppDatabase.getDatabase(context)
            val exporter = FileDatabaseExporter()
            val strategy = SqlDatabaseExporterStrategy(database)
            val file = exporter.export(context, strategy)

            val uri =
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file,
                )
            val intent =
                Intent(Intent.ACTION_SEND).apply {
                    type = strategy.getMimeType()
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            val chooser =
                Intent.createChooser(intent, context.getString(R.string.settings_export_data))
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (error: Exception) {
            _errorMessage.value = context.getString(R.string.export_data_error)
        }
    }

    fun updateStatuses() {
        _isAccessibilityServiceEnabled.value =
            AccessibilityUtils.isAccessibilityServiceEnabledAlternative(
                context,
                AccessibilityServiceActivity::class.java,
            )
        _isAccessibilityServiceStarted.value = storageRepository.getTheScreenReaderStatus()
        _isOverlayPermissionEnabled.value = Settings.canDrawOverlays(context)
    }

    fun toggleAccessibilityService() {
        val newStatus = !_isAccessibilityServiceStarted.value
        storageRepository.saveTheScreenReaderStatus(newStatus)
        _isAccessibilityServiceStarted.value = newStatus
    }

    fun openAccessibilitySettings() {
        AccessibilityUtils.openAccessibilitySettings(context)
    }

    fun openOverlaySettings() {
        OverlayUtils.openSettingsToEnableTheOverlayPermission(context)
    }

    fun startOverlayService() {
        OverlayUtils.startOverlayService(context)
    }

    fun resetError() {
        _errorMessage.value = null
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val appContext = context.applicationContext
                    @Suppress("UNCHECKED_CAST")
                    return SettingsViewModel(
                        appContext,
                        KeyValueStorageRepository(SharedPreferencesKeyValueStorage(appContext)),
                    ) as T
                }
            }
    }
}
