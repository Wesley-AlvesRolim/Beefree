package com.wesley.beefree.infrastructure.bus.adapters

import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.domain.events.ScreenContentCaptured
import org.junit.Assert.assertEquals
import org.junit.Test

class InMemoryEventBusTest {
    @Test
    fun `should publish and subscribe to events`() {
        val eventBus = InMemoryEventBus()
        var receivedEvent: InterventionTriggered? = null

        eventBus.subscribe(InterventionTriggered::class.java) {
            receivedEvent = it
        }

        val event = InterventionTriggered("Test reason", "keyword", 1, "package")
        eventBus.publish(event)

        assertEquals(event, receivedEvent)
    }

    @Test
    fun `should only receive events of the subscribed type`() {
        val eventBus = InMemoryEventBus()
        var receivedCount = 0

        eventBus.subscribe(InterventionTriggered::class.java) {
            receivedCount++
        }

        eventBus.publish(ScreenContentCaptured(listOf("safe"), "com.package"))
        eventBus.publish(InterventionTriggered("Reason", "keyword", 1, "package"))

        assertEquals(1, receivedCount)
    }
}
