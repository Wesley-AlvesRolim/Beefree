package com.wesley.beefree.ui.viewmodel

import android.content.Context
import android.provider.Settings
import androidx.lifecycle.ViewModel
import com.wesley.beefree.infrastructure.services.AccessibilityServiceActivity
import com.wesley.beefree.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository
import com.wesley.beefree.utils.AccessibilityUtils
import com.wesley.beefree.utils.OverlayUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val context: Context,
) : ViewModel() {
    private val storageRepository = KeyValueStorageRepository(SharedPreferencesKeyValueStorage(context))

    private val _isAccessibilityServiceEnabled = MutableStateFlow(false)
    val isAccessibilityServiceEnabled: StateFlow<Boolean> = _isAccessibilityServiceEnabled.asStateFlow()

    private val _isAccessibilityServiceStarted = MutableStateFlow(false)
    val isAccessibilityServiceStarted: StateFlow<Boolean> = _isAccessibilityServiceStarted.asStateFlow()

    private val _isOverlayPermissionEnabled = MutableStateFlow(false)
    val isOverlayPermissionEnabled: StateFlow<Boolean> = _isOverlayPermissionEnabled.asStateFlow()

    init {
        updateStatuses()
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
}
