package com.wesley.beefree.ui.adapters

import android.content.Context
import com.wesley.beefree.domain.intervention.ports.InterventionUI
import com.wesley.beefree.utils.OverlayUtils

class AndroidOverlayInterventionUI(
    private val context: Context,
) : InterventionUI {
    override fun show(reason: String) {
        OverlayUtils.startOverlayService(context, reason)
    }

    override fun hide() {
        OverlayUtils.stopOverlayService(context)
    }
}
