package com.wesley.beefree.ui.adapters

import android.content.Context
import com.wesley.beefree.domain.intervention.ports.InterventionUI
import com.wesley.beefree.utils.EMIUtils

class AndroidEMIInterventionUI(
    private val context: Context,
) : InterventionUI {
    override fun show(reason: String) {
        EMIUtils.startInterventionActivity(context, reason)
    }

    override fun hide() {}
}
