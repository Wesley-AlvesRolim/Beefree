package com.wesley.beefree.domain.detection

import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.domain.events.ScreenContentCaptured
import com.wesley.beefree.infrastructure.bus.adapters.InMemoryEventBus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class KeywordsDetectionEngineTest {
    @Test
    fun `should publish InterventionTriggered when a blocked keyword is detected`() {
        val eventBus = InMemoryEventBus()
        val keywordsByAddictionType = mapOf(1 to listOf("bet", "another_bet", "famous_bet"))
        KeywordsDetectionEngine(eventBus, keywordsByAddictionType)

        var receivedEvent: InterventionTriggered? = null
        eventBus.subscribe(InterventionTriggered::class.java) {
            receivedEvent = it
        }

        val reasonContent = keywordsByAddictionType[1]?.joinToString(", ", limit = 3, truncated = "...")
        val captureEvent = ScreenContentCaptured(listOf(reasonContent!!), "com.example.app")
        eventBus.publish(captureEvent)

        assertNotNull(receivedEvent)
        assertEquals(reasonContent, receivedEvent?.reason)
        assertEquals(keywordsByAddictionType[1]?.last(), receivedEvent?.keyword)
        assertEquals(1, receivedEvent?.addictionTypeId)
        assertEquals("com.example.app", receivedEvent?.appPackage)
    }

    @Test
    fun `should NOT publish InterventionTriggered when no blocked keyword is detected`() {
        val eventBus = InMemoryEventBus()
        val keywordsByAddictionType = mapOf(1 to listOf("bet", "porn"))
        KeywordsDetectionEngine(eventBus, keywordsByAddictionType)

        var receivedEvent: InterventionTriggered? = null
        eventBus.subscribe(InterventionTriggered::class.java) {
            receivedEvent = it
        }

        val captureEvent = ScreenContentCaptured(listOf("Safe content"), "com.example.app")
        eventBus.publish(captureEvent)

        assertNull(receivedEvent)
    }

    @Test
    fun `should be case-insensitive when detecting blocked keywords`() {
        val eventBus = InMemoryEventBus()
        val keywordsByAddictionType = mapOf(1 to listOf("BET", "ANOTHER_BET", "FAMOUS_BET"))
        KeywordsDetectionEngine(eventBus, keywordsByAddictionType)

        var receivedEvent: InterventionTriggered? = null
        eventBus.subscribe(InterventionTriggered::class.java) {
            receivedEvent = it
        }

        val reasonContent = keywordsByAddictionType[1]?.joinToString(", ", limit = 3, truncated = "...")
        val captureEvent = ScreenContentCaptured(listOf(reasonContent!!), "com.example.app")
        eventBus.publish(captureEvent)

        assertNotNull(receivedEvent)
        assertEquals(reasonContent.lowercase(), receivedEvent?.reason)
    }

    @Test
    fun `should reset scorer after triggering intervention`() {
        val eventBus = InMemoryEventBus()
        val keywordsByAddictionType = mapOf(1 to listOf("k1", "k2", "k3", "k4", "k5", "k6"))
        val engine = KeywordsDetectionEngine(eventBus, keywordsByAddictionType)

        var triggerCount = 0
        eventBus.subscribe(InterventionTriggered::class.java) {
            triggerCount++
        }

        engine.detect(ScreenContentCaptured(listOf("k1"), "pkg"))
        engine.detect(ScreenContentCaptured(listOf("k2"), "pkg"))
        assertEquals(0, triggerCount)

        engine.detect(ScreenContentCaptured(listOf("k3"), "pkg"))
        assertEquals(1, triggerCount)

        engine.detect(ScreenContentCaptured(listOf("k4"), "pkg"))
        engine.detect(ScreenContentCaptured(listOf("k5"), "pkg"))
        assertEquals(1, triggerCount)

        engine.detect(ScreenContentCaptured(listOf("k6"), "pkg"))
        assertEquals(2, triggerCount)
    }
}
