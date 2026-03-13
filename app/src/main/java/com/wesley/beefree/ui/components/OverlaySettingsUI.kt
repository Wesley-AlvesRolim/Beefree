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
fun OverlaySettingsUI(viewModel: SettingsViewModel) {
    val isPermissionEnabled by viewModel.isOverlayPermissionEnabled.collectAsState()

    OverlaySettingsContent(
        isPermissionEnabled = isPermissionEnabled,
        onUpdateStatuses = { viewModel.updateStatuses() },
        onStartService = { viewModel.startOverlayService() },
        onOpenSettings = { viewModel.openOverlaySettings() },
    )
}

@Composable
fun OverlaySettingsContent(
    isPermissionEnabled: Boolean,
    onUpdateStatuses: () -> Unit,
    onStartService: () -> Unit,
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

    if (isPermissionEnabled) {
        Text("Overlay permission is ENABLED")
        Button(onClick = onStartService) {
            Text("Start overlay")
        }
    } else {
        Text("Overlay permission is DISABLED. Please enable it in settings.")
        Button(onClick = onOpenSettings) {
            Text("Go to Permissions Settings")
        }
    }
}
