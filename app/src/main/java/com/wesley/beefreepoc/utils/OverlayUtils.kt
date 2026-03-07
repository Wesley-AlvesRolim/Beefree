package com.wesley.beefreepoc.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.wesley.beefreepoc.infrastructure.services.OverlayServiceActivity

object OverlayUtils {
    fun startOverlayService(context: Context) {
        if (OverlayServiceActivity.isRunning) {
            return
        }
        val intent = Intent(context, OverlayServiceActivity::class.java)
        context.startService(intent)
    }

    fun openSettingsToEnableTheOverlayPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        context.startActivity(intent)
    }
}
