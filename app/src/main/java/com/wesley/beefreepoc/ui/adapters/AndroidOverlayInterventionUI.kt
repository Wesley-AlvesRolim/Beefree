package com.wesley.beefreepoc.ui.adapters

import android.content.Context
import com.wesley.beefreepoc.domain.intervention.ports.InterventionUI
import com.wesley.beefreepoc.utils.OverlayUtils

class AndroidOverlayInterventionUI(
    private val context: Context,
) : InterventionUI {
    override fun show() {
        OverlayUtils.startOverlayService(context)
    }
}
