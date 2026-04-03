package com.wesley.beefree.infrastructure.dispatcher

import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.wesley.beefree.data.apps.BRAZILIAN_BANK_PACKAGE_NAMES
import com.wesley.beefree.data.apps.HELP_APPS_PACKAGE_NAMES
import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.detection.ports.WindowContentProvider
import com.wesley.beefree.domain.events.BankingAppForegrounded
import com.wesley.beefree.domain.events.EventDispatcher
import com.wesley.beefree.domain.events.ScreenContentCaptured
import com.wesley.beefree.infrastructure.services.OverlayServiceActivity
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository

class AccessibilityEventDispatcher(
    private val eventBus: EventBus,
    private val repository: KeyValueStorageRepository?,
) : EventDispatcher<AccessibilityEvent?> {
    override fun dispatch(
        event: AccessibilityEvent?,
        vararg args: Any?,
    ) {
        if (cannotDispatchTheEvent(event)) return

        val provider = args.filterIsInstance<WindowContentProvider>().firstOrNull() ?: return
        val rootNode = provider.getRootNode() ?: return

        try {
            val texts = mutableListOf<String>()
            extractText(rootNode, texts)
            event?.packageName?.let {
                eventBus.publish(ScreenContentCaptured(texts, it.toString()))
            } ?: run {
                eventBus.publish(ScreenContentCaptured(texts, null))
            }
        } finally {
            rootNode.recycle()
        }
    }

    private fun cannotDispatchTheEvent(event: AccessibilityEvent?): Boolean {
        if (OverlayServiceActivity.isRunning) return true
        if (repository?.getTheScreenReaderStatus() == false) return true

        if (event?.packageName != null && BRAZILIAN_BANK_PACKAGE_NAMES.contains(event.packageName.toString())) {
            if (OverlayServiceActivity.isRunning) eventBus.publish(BankingAppForegrounded)
            return true
        }

        if (event?.packageName != null && HELP_APPS_PACKAGE_NAMES.contains(event.packageName.toString())) {
            return true
        }

        return false
    }

    private fun extractText(
        node: AccessibilityNodeInfo,
        texts: MutableList<String>,
    ) {
        node.text?.let { texts.add(it.toString()) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            node.paneTitle?.let { texts.add(it.toString()) }
        }
        node.contentDescription?.let { texts.add(it.toString()) }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                if (child.isVisibleToUser) {
                    extractText(child, texts)
                }
                child.recycle()
            }
        }
    }
}
