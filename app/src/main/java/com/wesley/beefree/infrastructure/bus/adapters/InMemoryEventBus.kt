package com.wesley.beefree.infrastructure.bus.adapters

import android.util.Log
import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.DomainEvent

class InMemoryEventBus : EventBus {
    private val subscribers = mutableMapOf<Class<out DomainEvent>, MutableList<(DomainEvent) -> Unit>>()
    private val tag = "EventBus"

    override fun publish(event: DomainEvent) {
        Log.d(tag, "publish ${event::class.java.name}")
        subscribers[event::class.java]?.forEach { subscriber ->
            subscriber(event)
        }
    }

    override fun <T : DomainEvent> subscribe(
        eventType: Class<T>,
        subscriber: (T) -> Unit,
    ) {
        Log.d(tag, "subscribe ${eventType.name}")
        val list = subscribers.getOrPut(eventType) { mutableListOf() }
        list.add(subscriber as (DomainEvent) -> Unit)
    }
}
