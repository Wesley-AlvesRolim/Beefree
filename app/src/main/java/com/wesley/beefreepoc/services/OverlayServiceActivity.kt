package com.wesley.beefreepoc.services

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.wesley.beefreepoc.ui.components.OverlayUI
import com.wesley.beefreepoc.utils.ComposeLifecycleOwner

class OverlayServiceActivity : Service() {
    companion object {
        var isRunning = false
    }

    var androidWindowManager: WindowManager? = null
    var floatyView: View? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        androidWindowManager = getSystemService(WindowManager::class.java)
        addOverlayView()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        if (floatyView != null) {
            androidWindowManager?.removeView(floatyView)
            floatyView = null
        }
    }

    private fun addOverlayView() {
        val composeView =
            ComposeView(this).apply {
                setContent {
                    OverlayUI(onCloseRequest = { stopSelf() })
                }
                val lifecycleOwner = ComposeLifecycleOwner()
                lifecycleOwner.performRestore(null)
                lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
                setViewTreeLifecycleOwner(lifecycleOwner)
                setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            }

        val layoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT,
            )
        layoutParams.x = 0
        layoutParams.y = 0
        layoutParams.gravity = Gravity.CENTER or Gravity.START
        androidWindowManager?.addView(composeView, layoutParams)
        floatyView = composeView
    }
}
