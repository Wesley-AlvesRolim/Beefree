package com.wesley.beefree.utils

import android.content.Context
import android.content.Intent
import com.wesley.beefree.ui.INTERVENTION_REASON
import com.wesley.beefree.ui.InterventionActivity

object EMIUtils {
    private var lastActivityAttempt = 0L
    private const val ATTEMPT_COOLDOWN = 1000L

    fun startInterventionActivity(
        context: Context,
        reason: String = "",
    ) {
        if (InterventionActivity.isRunning) return
        val now = System.currentTimeMillis()
        if (now - lastActivityAttempt < ATTEMPT_COOLDOWN) return
        lastActivityAttempt = now

        val intent =
            Intent(context, InterventionActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(INTERVENTION_REASON, reason)
            }
        context.startActivity(intent)
    }
}
