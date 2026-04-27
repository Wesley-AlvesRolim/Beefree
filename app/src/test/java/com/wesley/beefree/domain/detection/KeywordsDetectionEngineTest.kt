package com.wesley.beefree.domain.detection

import com.wesley.beefree.domain.detection.ports.DetectionScorer
import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.domain.events.ScreenContentCaptured
import com.wesley.beefree.infrastructure.bus.adapters.InMemoryEventBus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class KeywordsDetectionEngineTest {
    @Test
    fun `should publish InterventionTriggered when a blocked keyword is detected`() {
        val eventBus = InMemoryEventBus()
        val keywordsByCategory = mapOf(1 to listOf("bet", "another_bet", "famous_bet"))
        KeywordsDetectionEngine(eventBus, keywordsByCategory)

        var receivedEvent: InterventionTriggered? = null
        eventBus.subscribe(InterventionTriggered::class.java) {
            receivedEvent = it
        }

        val reasonContent =
            keywordsByCategory[1]?.joinToString(", ", limit = 3, truncated = "...")
        val captureEvent = ScreenContentCaptured(listOf(reasonContent!!), "com.example.app")
        eventBus.publish(captureEvent)

        assertNotNull(receivedEvent)
        assertEquals(reasonContent, receivedEvent?.reason)
        assertEquals(keywordsByCategory[1]?.last(), receivedEvent?.keyword)
        assertEquals(1, receivedEvent?.addictionCategoryId)
        assertEquals("com.example.app", receivedEvent?.appPackage)
    }

    @Test
    fun `should NOT publish InterventionTriggered when no blocked keyword is detected`() {
        val eventBus = InMemoryEventBus()
        val keywordsByCategory = mapOf(1 to listOf("bet", "porn"))
        KeywordsDetectionEngine(eventBus, keywordsByCategory)

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
        val keywordsByCategory = mapOf(1 to listOf("BET", "ANOTHER_BET", "FAMOUS_BET"))
        KeywordsDetectionEngine(eventBus, keywordsByCategory)

        var receivedEvent: InterventionTriggered? = null
        eventBus.subscribe(InterventionTriggered::class.java) {
            receivedEvent = it
        }

        val reasonContent =
            keywordsByCategory[1]
                ?.joinToString(", ", limit = 3, truncated = "...")
                ?.lowercase()
        val captureEvent = ScreenContentCaptured(listOf(reasonContent!!), "com.example.app")
        eventBus.publish(captureEvent)

        assertNotNull(receivedEvent)
        assertEquals(reasonContent, receivedEvent?.reason)
    }

    @Test
    fun `should reset scorer after triggering intervention`() {
        val eventBus = InMemoryEventBus()
        val keywordsByCategory = mapOf(1 to listOf("k1", "k2", "k3", "k4", "k5", "k6"))
        val engine = KeywordsDetectionEngine(eventBus, keywordsByCategory)

        var triggerCount = 0
        eventBus.subscribe(InterventionTriggered::class.java) {
            triggerCount++
        }

        engine.detect(ScreenContentCaptured(listOf("k1"), "pkg"))
        engine.detect(ScreenContentCaptured(listOf("k2"), "pkg"))
        assertEquals(0, triggerCount)

        engine.detect(ScreenContentCaptured(listOf("k1", "k2", "k3"), "pkg"))
        assertEquals(1, triggerCount)

        engine.detect(ScreenContentCaptured(listOf("k4"), "pkg"))
        assertEquals(1, triggerCount)

        engine.detect(ScreenContentCaptured(listOf("k4", "k5", "k6"), "pkg"))
        assertEquals(2, triggerCount)
    }

    @Test
    fun `should reset scorer even if an exception occurs during detection`() {
        val eventBus = InMemoryEventBus()
        val mockScorer =
            mock<DetectionScorer> {
                on {
                    addMatch(
                        any(),
                        any(),
                        any(),
                        any(),
                    )
                } doThrow RuntimeException("Failure during detection")
            }
        val keywordsByCategory = mapOf(1 to listOf("bet"))
        val engine = KeywordsDetectionEngine(eventBus, keywordsByCategory, mockScorer)

        try {
            engine.detect(ScreenContentCaptured(listOf("bet"), "pkg"))
        } catch (e: Exception) {
        }

        verify(mockScorer, times(1)).reset()
    }

    @Test
    fun `should reset scorer after every detection attempt`() {
        val eventBus = InMemoryEventBus()
        val mockScorer = mock<DetectionScorer>()
        val engine = KeywordsDetectionEngine(eventBus, emptyMap(), mockScorer)

        engine.detect(ScreenContentCaptured(listOf("Safe"), "pkg"))
        verify(mockScorer, times(1)).reset()

        engine.detect(ScreenContentCaptured(listOf("Another Safe"), "pkg"))
        verify(mockScorer, times(2)).reset()
    }

    @Test
    fun `should not detect keywords removed via updateKeywords`() {
        val eventBus = InMemoryEventBus()
        val engine = KeywordsDetectionEngine(eventBus, mapOf(1 to listOf("bet")))

        var triggerCount = 0
        eventBus.subscribe(InterventionTriggered::class.java) { triggerCount++ }

        engine.updateKeywords(emptyMap())
        engine.detect(ScreenContentCaptured(listOf("bet, another_bet, famous_bet"), "pkg"))

        assertEquals(0, triggerCount)
    }

    @Test
    fun `should detect new keywords added via updateKeywords`() {
        val eventBus = InMemoryEventBus()
        val engine = KeywordsDetectionEngine(eventBus, emptyMap())

        var receivedEvent: InterventionTriggered? = null
        eventBus.subscribe(InterventionTriggered::class.java) { receivedEvent = it }

        engine.updateKeywords(mapOf(1 to listOf("k1", "k2", "k3")))
        engine.detect(ScreenContentCaptured(listOf("k1, k2, k3"), "pkg"))

        assertNotNull(receivedEvent)
        assertEquals(1, receivedEvent?.addictionCategoryId)
    }
}
