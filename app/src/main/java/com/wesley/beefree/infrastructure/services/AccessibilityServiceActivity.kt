package com.wesley.beefree.infrastructure.services

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.wesley.beefree.data.keywords.getBetsKeyWords
import com.wesley.beefree.data.keywords.getPornKeywords
import com.wesley.beefree.domain.detection.KeywordsDetectionEngine
import com.wesley.beefree.domain.entities.AddictionTypeEnum
import com.wesley.beefree.domain.intervention.ports.DeviceActionProvider
import com.wesley.beefree.infrastructure.bus.adapters.InMemoryEventBus
import com.wesley.beefree.infrastructure.dispatcher.AccessibilityEventDispatcher
import com.wesley.beefree.infrastructure.history.RelapseRecorderModule
import com.wesley.beefree.infrastructure.intervention.DeviceGoBackIntervention
import com.wesley.beefree.infrastructure.intervention.OverlayInterventionModule
import com.wesley.beefree.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.storage.adapters.db.AppDatabase
import com.wesley.beefree.storage.ports.AddictionRepository
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository
import com.wesley.beefree.ui.adapters.AndroidOverlayInterventionUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AccessibilityServiceActivity :
    AccessibilityService(),
    DeviceActionProvider,
    CoroutineScope {
    private val tag = "AccessibilityServiceActivity"
    private var keyValueStorageRepository: KeyValueStorageRepository? = null
    private var addictionRepository: AddictionRepository? = null

    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.Main + job

    private val eventBus = InMemoryEventBus()
    private lateinit var dispatcher: AccessibilityEventDispatcher
    private lateinit var keyWordsDetectionEngine: KeywordsDetectionEngine
    private lateinit var interventionModule: OverlayInterventionModule
    private lateinit var deviceGoBackIntervention: DeviceGoBackIntervention
    private lateinit var relapseRecorderModule: RelapseRecorderModule

    override fun onCreate() {
        super.onCreate()
        keyValueStorageRepository =
            KeyValueStorageRepository(SharedPreferencesKeyValueStorage(this))
        val db = AppDatabase.getDatabase(this)
        addictionRepository =
            RoomAddictionRepository(
                db.addictionTypeDao(),
                db.addictionKeywordDao(),
                db.relapseHistoryDao(),
            )
        dispatcher = AccessibilityEventDispatcher(eventBus, keyValueStorageRepository)

        val keywordsByAddictionType =
            mapOf(
                AddictionTypeEnum.ADULT_CONTENT.ordinal to getPornKeywords(),
                AddictionTypeEnum.BETS.ordinal to getBetsKeyWords(),
            )
        keyWordsDetectionEngine = KeywordsDetectionEngine(eventBus, keywordsByAddictionType)

        val interventionUI = AndroidOverlayInterventionUI(this)
        interventionModule = OverlayInterventionModule(eventBus, interventionUI)
        deviceGoBackIntervention = DeviceGoBackIntervention(eventBus, this)

        relapseRecorderModule = RelapseRecorderModule(eventBus, addictionRepository!!, this)
    }

    override fun performGoBack() {
        performGlobalAction(GLOBAL_ACTION_BACK)
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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
