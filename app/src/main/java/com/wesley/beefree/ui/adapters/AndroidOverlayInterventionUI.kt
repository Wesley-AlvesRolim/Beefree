package com.wesley.beefree.ui.adapters

import android.content.Context
import android.os.Build
import com.wesley.beefree.domain.intervention.ports.InterventionUI
import com.wesley.beefree.utils.OverlayUtils

class AndroidOverlayInterventionUI(
    private val context: Context,
) : InterventionUI {
    override fun show(reason: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            OverlayUtils.startInterventionActivity(context, reason)
        } else {
            OverlayUtils.startOverlayService(context, reason)
        }
    }

    override fun hide() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            OverlayUtils.stopOverlayService(context)
        }
    }
}
