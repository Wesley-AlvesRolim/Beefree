package com.wesley.beefreepoc.infrastructure.bus

import com.wesley.beefreepoc.domain.events.InterventionTriggered
import com.wesley.beefreepoc.domain.events.ScreenContentCaptured
import com.wesley.beefreepoc.infrastructure.bus.adapters.InMemoryEventBus
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

        val event = InterventionTriggered("Test reason")
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
        eventBus.publish(InterventionTriggered("Reason"))

        assertEquals(1, receivedCount)
    }
}
