package com.wesley.beefree.infrastructure.services

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.wesley.beefree.data.keywords.getBetsKeyWords
import com.wesley.beefree.data.keywords.getPornKeywords
import com.wesley.beefree.domain.detection.KeywordsDetectionEngine
import com.wesley.beefree.domain.entities.AddictionTypeEnum
import com.wesley.beefree.infrastructure.bus.adapters.InMemoryEventBus
import com.wesley.beefree.infrastructure.dispatcher.AccessibilityEventDispatcher
import com.wesley.beefree.infrastructure.intervention.OverlayInterventionModule
import com.wesley.beefree.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository
import com.wesley.beefree.ui.adapters.AndroidOverlayInterventionUI

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

        val keywordsByAddictionType =
            mapOf(
                AddictionTypeEnum.ADULT_CONTENT.ordinal to getPornKeywords(),
                AddictionTypeEnum.BETS.ordinal to getBetsKeyWords(),
            )
        keyWordsDetectionEngine = KeywordsDetectionEngine(eventBus, keywordsByAddictionType)

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
