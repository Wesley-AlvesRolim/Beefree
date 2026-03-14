package com.wesley.beefree.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.wesley.beefree.R
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
        Text(stringResource(R.string.overlay_permission_enabled_status))
        Button(onClick = onStartService) {
            Text(stringResource(R.string.overlay_service_start))
        }
    } else {
        Text(stringResource(R.string.overlay_permission_disabled_status))
        Button(onClick = onOpenSettings) {
            Text(stringResource(R.string.overlay_go_to_settings))
        }
    }
}
