package com.wesley.beefree.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.wesley.beefree.ui.components.OverlayUI

const val INTERVENTION_REASON = "INTERVENTION_REASON"

class InterventionActivity : ComponentActivity() {
    companion object {
        var isRunning = false
    }

    override fun onStart() {
        super.onStart()
        isRunning = true
    }

    override fun onStop() {
        isRunning = false
        super.onStop()
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        updateUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        updateUI()
    }

    private fun updateUI() {
        val reason = intent.getStringExtra(INTERVENTION_REASON) ?: ""
        setContent {
            OverlayUI(
                reason = reason,
                onCloseRequest = {
                    finish()
                },
            )
        }
    }
}
