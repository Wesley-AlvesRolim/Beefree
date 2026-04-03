package com.wesley.beefree.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.wesley.beefree.ui.components.OverlayUI

const val INTERVENTION_REASON = "INTERVENTION_REASON"

class InterventionActivity : ComponentActivity() {
    companion object {
        var isRunning = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRunning = true
        val reason = intent.getStringExtra(INTERVENTION_REASON) ?: ""
        setContent {
            OverlayUI(
                reason = reason,
                onCloseRequest = { finish() },
            )
        }
    }

    override fun onDestroy() {
        isRunning = false
        super.onDestroy()
    }
}
