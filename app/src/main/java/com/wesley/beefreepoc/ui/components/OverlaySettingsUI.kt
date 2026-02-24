package com.wesley.beefreepoc.ui.components

import android.provider.Settings
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.wesley.beefreepoc.utils.OverlayUtils

@Composable
fun OverlaySettingsUI() {
    val context = LocalContext.current
    var isPermissionEnabled by remember { mutableStateOf(false) }

    fun updatePermissionStatus() {
        isPermissionEnabled = Settings.canDrawOverlays(context)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    updatePermissionStatus()
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    fun openSettingsToEnableTheOverlayPermission() {
        OverlayUtils.openSettingsToEnableTheOverlayPermission(context)
    }

    fun startOverlayService() {
        OverlayUtils.startOverlayService(context)
    }

    if (isPermissionEnabled) {
        Text("Overlay permission is ENABLED")
        Button(onClick = { startOverlayService() }) {
            Text("Start overlay")
        }
    } else {
        Text("Overlay permission is DISABLED. Please enable it in settings.")
        Button(onClick = { openSettingsToEnableTheOverlayPermission() }) {
            Text("Go to Permissions Settings")
        }
    }
}
