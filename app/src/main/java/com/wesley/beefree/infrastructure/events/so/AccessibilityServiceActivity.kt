package com.wesley.beefree.infrastructure.events.so

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.wesley.beefree.data.keywords.buildKeywordsMap
import com.wesley.beefree.domain.detection.KeywordsDetectionEngine
import com.wesley.beefree.domain.detection.ports.WindowContentProvider
import com.wesley.beefree.domain.intervention.ports.DeviceActionProvider
import com.wesley.beefree.infrastructure.bus.adapters.InMemoryEventBus
import com.wesley.beefree.infrastructure.dispatcher.AccessibilityEventDispatcher
import com.wesley.beefree.infrastructure.history.RelapseRecorderModule
import com.wesley.beefree.infrastructure.intervention.DeviceGoBackIntervention
import com.wesley.beefree.infrastructure.intervention.EMIInterventionModule
import com.wesley.beefree.infrastructure.logging.AndroidLogger
import com.wesley.beefree.infrastructure.trigger.TriggerRecorderModule
import com.wesley.beefree.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.storage.adapters.RoomCheckInRepository
import com.wesley.beefree.storage.adapters.RoomEMIRepository
import com.wesley.beefree.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.storage.adapters.db.AppDatabase
import com.wesley.beefree.storage.ports.AddictionRepository
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository
import com.wesley.beefree.ui.adapters.AndroidEMIInterventionUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AccessibilityServiceActivity :
    AccessibilityService(),
    DeviceActionProvider,
    WindowContentProvider,
    CoroutineScope {
    private val tag = "AccessibilityServiceActivity"
    private var keyValueStorageRepository: KeyValueStorageRepository? = null
    private var addictionRepository: AddictionRepository? = null

    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.Main + job

    private val eventBus = InMemoryEventBus(AndroidLogger)
    private lateinit var dispatcher: AccessibilityEventDispatcher
    private lateinit var keyWordsDetectionEngine: KeywordsDetectionEngine
    private lateinit var interventionModule: EMIInterventionModule
    private lateinit var deviceGoBackIntervention: DeviceGoBackIntervention
    private lateinit var relapseRecorderModule: RelapseRecorderModule
    private lateinit var triggerRecorderModule: TriggerRecorderModule

    override fun onCreate() {
        super.onCreate()
        keyValueStorageRepository =
            KeyValueStorageRepository(SharedPreferencesKeyValueStorage(this))
        val db = AppDatabase.Companion.getDatabase(this)
        addictionRepository =
            RoomAddictionRepository(
                db.addictionTypeDao(),
                db.addictionKeywordDao(),
                db.relapseHistoryDao(),
            )
        dispatcher = AccessibilityEventDispatcher(eventBus, keyValueStorageRepository)

        keyWordsDetectionEngine = KeywordsDetectionEngine(eventBus, emptyMap())
        launch {
            addictionRepository!!.getAllAddictionTypes().collect { types ->
                val keywords = buildKeywordsMap(types, addictionRepository!!)
                keyWordsDetectionEngine.updateKeywords(keywords)
            }
        }

        val interventionUI = AndroidEMIInterventionUI(this)
        interventionModule = EMIInterventionModule(eventBus, interventionUI)
        deviceGoBackIntervention = DeviceGoBackIntervention(eventBus, this)

        relapseRecorderModule = RelapseRecorderModule(eventBus, addictionRepository!!, this)

        val emiRepository =
            RoomEMIRepository(
                db.triggerMappingDao(),
                db.interventionLogDao(),
                db.thoughtRecordDao(),
                db.urgeSurfingSessionDao(),
            )
        val checkInRepository =
            RoomCheckInRepository(
                db.dailyCheckInDao(),
                db.weeklyCheckInDao(),
            )
        val userProfileRepository =
            RoomUserProfileRepository(
                db.userProfileDao(),
                db.userProfileAddictionDao(),
            )
        triggerRecorderModule =
            TriggerRecorderModule(
                eventBus = eventBus,
                emiRepository = emiRepository,
                checkInRepository = checkInRepository,
                coroutineScope = this,
                userIdProvider = {
                    userProfileRepository
                        .getAllProfiles()
                        .first()
                        .firstOrNull()
                        ?.id ?: 0
                },
            )
    }

    override fun performGoBack() {
        performGlobalAction(GLOBAL_ACTION_BACK)
    }

    override fun getRootNode(): AccessibilityNodeInfo? = rootInActiveWindow

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        AndroidLogger.d(tag, "onAccessibilityEvent: ${event?.packageName}")
        dispatcher.dispatch(event, this)
    }

    override fun onInterrupt() {
        AndroidLogger.d(tag, "Stop AccessibilityService")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}