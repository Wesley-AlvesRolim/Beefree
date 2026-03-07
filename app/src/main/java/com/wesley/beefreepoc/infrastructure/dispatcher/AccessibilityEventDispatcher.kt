package com.wesley.beefreepoc.infrastructure.dispatcher

import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.wesley.beefreepoc.domain.bus.ports.EventBus
import com.wesley.beefreepoc.domain.events.EventDispatcher
import com.wesley.beefreepoc.domain.events.ScreenContentCaptured
import com.wesley.beefreepoc.infrastructure.services.OverlayServiceActivity
import com.wesley.beefreepoc.storage.repositories.KeyValueStorageRepository

class AccessibilityEventDispatcher(
    private val eventBus: EventBus,
    private val repository: KeyValueStorageRepository?,
) : EventDispatcher<AccessibilityEvent?> {
    override fun dispatch(
        event: AccessibilityEvent?,
        vararg args: Any?,
    ) {
        if (cannotDispatchTheEvent()) return
        val nodes = args.filterIsInstance<AccessibilityNodeInfo>()
        if (nodes.firstOrNull() == null) {
            return
        }

        val texts = mutableListOf<String>()
        extractText(nodes.first(), texts)
        event?.packageName?.let {
            eventBus.publish(ScreenContentCaptured(texts, it.toString()))
        } ?: run {
            eventBus.publish(ScreenContentCaptured(texts, null))
        }
    }

    private fun cannotDispatchTheEvent(): Boolean = OverlayServiceActivity.isRunning || repository?.getTheScreenReaderStatus() == false

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
