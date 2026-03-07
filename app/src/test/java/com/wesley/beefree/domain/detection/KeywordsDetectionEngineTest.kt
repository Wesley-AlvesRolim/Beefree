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
        val blockedKeywords = listOf("bet", "porn")
        KeywordsDetectionEngine(eventBus, blockedKeywords)

        var receivedEvent: InterventionTriggered? = null
        eventBus.subscribe(InterventionTriggered::class.java) {
            receivedEvent = it
        }

        val reasonContent = "This is a bet site"
        val captureEvent = ScreenContentCaptured(listOf(reasonContent), "com.example.app")
        eventBus.publish(captureEvent)

        assertNotNull(receivedEvent)
        assertEquals(reasonContent, receivedEvent?.reason)
    }

    @Test
    fun `should NOT publish InterventionTriggered when no blocked keyword is detected`() {
        val eventBus = InMemoryEventBus()
        val blockedKeywords = listOf("bet", "porn")
        KeywordsDetectionEngine(eventBus, blockedKeywords)

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
        val blockedKeywords = listOf("BET")
        KeywordsDetectionEngine(eventBus, blockedKeywords)

        var receivedEvent: InterventionTriggered? = null
        eventBus.subscribe(InterventionTriggered::class.java) {
            receivedEvent = it
        }

        val reasonContent = "This is a bet site"
        val captureEvent = ScreenContentCaptured(listOf(reasonContent), "com.example.app")
        eventBus.publish(captureEvent)

        assertNotNull(receivedEvent)
        assertEquals(reasonContent, receivedEvent?.reason)
    }
}
