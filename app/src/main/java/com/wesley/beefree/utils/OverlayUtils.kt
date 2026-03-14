package com.wesley.beefree.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.wesley.beefree.infrastructure.services.OVERLAY_TRIGGERED_BY_REASON
import com.wesley.beefree.infrastructure.services.OverlayServiceActivity

object OverlayUtils {
    fun startOverlayService(
        context: Context,
        reason: String = "",
    ) {
        if (OverlayServiceActivity.isRunning) {
            return
        }
        val intent =
            Intent(context, OverlayServiceActivity::class.java).apply {
                putExtra(OVERLAY_TRIGGERED_BY_REASON, reason)
            }
        context.startService(intent)
    }

    fun openSettingsToEnableTheOverlayPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        context.startActivity(intent)
    }
}
