package com.wesley.beefreepoc.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.wesley.beefreepoc.data.repository.StorageRepository
import com.wesley.beefreepoc.services.AccessibilityServiceActivity
import com.wesley.beefreepoc.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefreepoc.utils.AccessibilityUtils

@Composable
fun AccessibilitySettingsUI() {
    val context = LocalContext.current
    var isServiceEnabled by remember { mutableStateOf(false) }
    var isServiceStarted by remember { mutableStateOf(false) }
    val storage = StorageRepository(SharedPreferencesKeyValueStorage(context))

    fun updateServiceStatuses() {
        isServiceEnabled =
            AccessibilityUtils.isAccessibilityServiceEnabledAlternative(
                context,
                AccessibilityServiceActivity::class.java,
            )
        isServiceStarted = storage.getTheScreenReaderStatus()
    }

    fun toggleServiceToReadScreen() {
        val newServiceStatus = !isServiceStarted
        storage.saveTheScreenReaderStatus(newServiceStatus)
        isServiceStarted = newServiceStatus
    }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    updateServiceStatuses()
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (isServiceEnabled) {
        Text("Accessibility Service is ENABLED")
        Button(onClick = { toggleServiceToReadScreen() }) {
            Text(if (isServiceStarted) "Stop service" else "Start Service")
        }
    } else {
        Text("Accessibility Service is DISABLED. Please enable it in settings.")
        Button(onClick = { AccessibilityUtils.openAccessibilitySettings(context) }) {
            Text("Go to Accessibility Settings")
        }
    }
}
