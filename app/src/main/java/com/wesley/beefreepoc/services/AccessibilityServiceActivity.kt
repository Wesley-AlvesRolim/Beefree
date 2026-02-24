package com.wesley.beefreepoc.services

import android.accessibilityservice.AccessibilityService
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.wesley.beefreepoc.data.keywords.getBetsKeyWords
import com.wesley.beefreepoc.data.keywords.getPornKeywords
import com.wesley.beefreepoc.data.repository.StorageRepository
import com.wesley.beefreepoc.exceptions.BlockedFlagDetectedException
import com.wesley.beefreepoc.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefreepoc.utils.OverlayUtils

class AccessibilityServiceActivity : AccessibilityService() {
    private val tag = "AccessibilityServiceActivity"
    private var storage: StorageRepository? = null

    private val blockedRegexList by lazy {
        (getPornKeywords() + getBetsKeyWords()).map { keyword ->
            Regex(keyword, RegexOption.IGNORE_CASE)
        }
    }

    override fun onCreate() {
        super.onCreate()
        storage = StorageRepository(SharedPreferencesKeyValueStorage(this))
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (OverlayServiceActivity.isRunning) {
            return
        }
        storage?.let { repository ->
            if (!repository.getTheScreenReaderStatus()) {
                return
            }
            Log.d(tag, "Event: $event")
            val source = event?.source
            if (source != null) {
                Log.d(tag, "Event Source Text: ${source.text}")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    Log.d(tag, "Root Node Pane Title: ${source.paneTitle}")
                }
                Log.d(tag, "Event Source Content Description: ${source.contentDescription}")
                source.recycle()
            }

            val rootNode = rootInActiveWindow
            if (rootNode != null) {
                Log.d(tag, "Root Node Text: ${rootNode.text}")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    Log.d(tag, "Root Node Pane Title: ${rootNode.paneTitle}")
                }
                Log.d(tag, "Root Node Content Description: ${rootNode.contentDescription}")
                try {
                    logNodeHierarchy(rootNode, 0)
                } catch (error: BlockedFlagDetectedException) {
                    OverlayUtils.startOverlayService(this)
                } finally {
                    rootNode.recycle()
                }
            }
        }
    }

    private fun logNodeHierarchy(
        node: AccessibilityNodeInfo,
        depth: Int,
    ) {
        val textToCheck = mutableListOf<CharSequence>()
        node.text?.let { textToCheck.add(it) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            node.paneTitle?.let { textToCheck.add(it) }
        }
        node.contentDescription?.let { textToCheck.add(it) }

        for (text in textToCheck) {
            if (text.isNotBlank()) {
                val textString = text.toString()
                for (regex in blockedRegexList) {
                    if (regex.containsMatchIn(textString)) {
                        throw BlockedFlagDetectedException(textString)
                    }
                }
            }
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                if (child.isVisibleToUser) {
                    logNodeHierarchy(child, depth + 1)
                }
                child.recycle()
            }
        }
    }

    override fun onInterrupt() {
        Log.d(tag, "Stop AccessibilityService")
    }
}
