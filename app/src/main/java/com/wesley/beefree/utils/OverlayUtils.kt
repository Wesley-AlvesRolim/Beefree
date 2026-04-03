package com.wesley.beefree.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.wesley.beefree.infrastructure.services.OVERLAY_TRIGGERED_BY_REASON
import com.wesley.beefree.infrastructure.services.OverlayServiceActivity
import com.wesley.beefree.ui.INTERVENTION_REASON
import com.wesley.beefree.ui.InterventionActivity

object OverlayUtils {
    fun startInterventionActivity(
        context: Context,
        reason: String = "",
    ) {
        if (InterventionActivity.isRunning) return
        InterventionActivity.isRunning = true
        val intent =
            Intent(context, InterventionActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(INTERVENTION_REASON, reason)
            }
        context.startActivity(intent)
    }

    fun startOverlayService(
        context: Context,
        reason: String = "",
    ) {
        if (OverlayServiceActivity.isRunning) return
        OverlayServiceActivity.isRunning = true
        val intent =
            Intent(context, OverlayServiceActivity::class.java).apply {
                putExtra(OVERLAY_TRIGGERED_BY_REASON, reason)
            }
        context.startService(intent)
    }

    fun stopOverlayService(context: Context) {
        context.stopService(Intent(context, OverlayServiceActivity::class.java))
    }

    fun openSettingsToEnableTheOverlayPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        context.startActivity(intent)
    }
}
