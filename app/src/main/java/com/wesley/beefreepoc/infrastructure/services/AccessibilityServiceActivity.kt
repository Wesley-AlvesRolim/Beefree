package com.wesley.beefreepoc.infrastructure.services

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.wesley.beefreepoc.data.keywords.getBetsKeyWords
import com.wesley.beefreepoc.data.keywords.getPornKeywords
import com.wesley.beefreepoc.domain.detection.KeywordsDetectionEngine
import com.wesley.beefreepoc.infrastructure.bus.adapters.InMemoryEventBus
import com.wesley.beefreepoc.infrastructure.dispatcher.AccessibilityEventDispatcher
import com.wesley.beefreepoc.infrastructure.intervention.OverlayInterventionModule
import com.wesley.beefreepoc.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefreepoc.storage.repositories.KeyValueStorageRepository
import com.wesley.beefreepoc.ui.adapters.AndroidOverlayInterventionUI

class AccessibilityServiceActivity : AccessibilityService() {
    private val tag = "AccessibilityServiceActivity"
    private var repository: KeyValueStorageRepository? = null

    private val eventBus = InMemoryEventBus()
    private lateinit var dispatcher: AccessibilityEventDispatcher
    private lateinit var keyWordsDetectionEngine: KeywordsDetectionEngine
    private lateinit var interventionModule: OverlayInterventionModule

    override fun onCreate() {
        super.onCreate()
        repository = KeyValueStorageRepository(SharedPreferencesKeyValueStorage(this))
        dispatcher = AccessibilityEventDispatcher(eventBus, repository)

        val blockedKeywords = (getPornKeywords() + getBetsKeyWords())
        keyWordsDetectionEngine = KeywordsDetectionEngine(eventBus, blockedKeywords)

        val interventionUI = AndroidOverlayInterventionUI(this)
        interventionModule = OverlayInterventionModule(eventBus, interventionUI)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d(tag, "Event: $event")
        val rootNode = rootInActiveWindow
        dispatcher.dispatch(event, rootNode)
        rootNode?.recycle()
    }

    override fun onInterrupt() {
        Log.d(tag, "Stop AccessibilityService")
    }
}
