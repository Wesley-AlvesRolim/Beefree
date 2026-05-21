package com.wesley.beefree.infrastructure.bus.subscribers.history

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.domain.repository.ports.AddictionRepository
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
            RelapseRecord(
                addictionCategoryId = event.addictionCategoryId,
                keywordDetected = event.keyword,
                detectedText = extractPhraseContaining(event.reason, event.keyword),
                createdAt = event.timestamp,
            ),
        )
    }

    private fun extractPhraseContaining(
        text: String,
        keyword: String,
    ): String {
        val index = text.indexOf(keyword, ignoreCase = true)
        if (index == -1) return text.take(150)
        val start =
            text.lastIndexOfAny(charArrayOf('.', '!', '?', '\n'), index).let {
                if (it == -1) 0 else it + 1
            }
        val end =
            text.indexOfAny(charArrayOf('.', '!', '?', '\n'), index + keyword.length).let {
                if (it == -1) text.length else it + 1
            }
        return text.substring(start, end).trim()
    }
}
