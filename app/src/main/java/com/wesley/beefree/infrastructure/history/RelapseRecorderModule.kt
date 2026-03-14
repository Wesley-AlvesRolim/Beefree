package com.wesley.beefree.infrastructure.history

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.entities.RelapseHistory
import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.storage.ports.AddictionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong

class RelapseRecorderModule(
    private val eventBus: EventBus,
    private val addictionRepository: AddictionRepository,
    private val coroutineScope: CoroutineScope,
    private val currentTimeProvider: () -> Long = { System.currentTimeMillis() },
) {
    private val eventIntervalMs = 5000L
    private val lastRelapseRecordedAt = AtomicLong(-5000)

    init {
        eventBus.subscribe(InterventionTriggered::class.java) { event ->
            val currentTimeMillis = currentTimeProvider()
            val lastValue = lastRelapseRecordedAt.get()
            if (currentTimeMillis - lastValue >= eventIntervalMs) {
                coroutineScope.launch { recordRelapse(event) }
                lastRelapseRecordedAt.set(currentTimeMillis)
            }
        }
    }

    private suspend fun recordRelapse(event: InterventionTriggered) {
        addictionRepository.insertRelapse(
            RelapseHistory(
                addictionTypeId = event.addictionTypeId,
                keywordDetected = event.keyword,
                detectedText = event.reason,
                appPackage = event.appPackage,
                relapseAt = event.timestamp,
                updatedAt = event.timestamp,
            ),
        )
    }
}
