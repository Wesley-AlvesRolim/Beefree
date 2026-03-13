package com.wesley.beefree.infrastructure.services

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
import com.wesley.beefree.ui.components.OverlayUI
import com.wesley.beefree.utils.ComposeLifecycleOwner

class OverlayServiceActivity : Service() {
    companion object {
        var isRunning = false
    }

    var androidWindowManager: WindowManager? = null
    var floatyView: View? = null
    private var lifecycleOwner: ComposeLifecycleOwner? = null
    private var reason: String = ""

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        reason = intent?.getStringExtra("EXTRA_REASON") ?: ""
        if (floatyView == null) {
            addOverlayView()
        }
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        androidWindowManager = getSystemService(WindowManager::class.java)
    }

    override fun onDestroy() {
        lifecycleOwner?.apply {
            handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        }
        isRunning = false
        if (floatyView != null) {
            androidWindowManager?.removeView(floatyView)
            floatyView = null
        }
        super.onDestroy()
    }

    private fun addOverlayView() {
        lifecycleOwner =
            ComposeLifecycleOwner().apply {
                performRestore(null)
                handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
                handleLifecycleEvent(Lifecycle.Event.ON_START)
                handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            }

        val composeView =
            ComposeView(this).apply {
                setContent {
                    OverlayUI(
                        reason = reason,
                        onCloseRequest = { stopSelf() },
                    )
                }
                setViewTreeLifecycleOwner(lifecycleOwner)
                setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            }

        val layoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
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
