package com.wesley.beefree.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.wesley.beefree.ui.viewmodel.SettingsViewModel

@Composable
fun AccessibilitySettingsUI(viewModel: SettingsViewModel) {
    val isServiceEnabled by viewModel.isAccessibilityServiceEnabled.collectAsState()
    val isServiceStarted by viewModel.isAccessibilityServiceStarted.collectAsState()

    AccessibilitySettingsContent(
        isServiceEnabled = isServiceEnabled,
        isServiceStarted = isServiceStarted,
        onUpdateStatuses = { viewModel.updateStatuses() },
        onToggleService = { viewModel.toggleAccessibilityService() },
        onOpenSettings = { viewModel.openAccessibilitySettings() },
    )
}

@Composable
fun AccessibilitySettingsContent(
    isServiceEnabled: Boolean,
    isServiceStarted: Boolean,
    onUpdateStatuses: () -> Unit,
    onToggleService: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    onUpdateStatuses()
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (isServiceEnabled) {
        Text("Accessibility Service is ENABLED")
        Button(onClick = onToggleService) {
            Text(if (isServiceStarted) "Stop service" else "Start Service")
        }
    } else {
        Text("Accessibility Service is DISABLED. Please enable it in settings.")
        Button(onClick = onOpenSettings) {
            Text("Go to Accessibility Settings")
        }
    }
}
