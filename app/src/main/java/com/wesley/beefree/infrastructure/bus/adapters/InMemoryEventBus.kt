package com.wesley.beefree.infrastructure.bus.adapters

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.DomainEvent

class InMemoryEventBus : EventBus {
    private val subscribers = mutableMapOf<Class<out DomainEvent>, MutableList<(DomainEvent) -> Unit>>()

    override fun publish(event: DomainEvent) {
        subscribers[event::class.java]?.forEach { subscriber ->
            subscriber(event)
        }
    }

    override fun <T : DomainEvent> subscribe(
        eventType: Class<T>,
        subscriber: (T) -> Unit,
    ) {
        val list = subscribers.getOrPut(eventType) { mutableListOf() }
        list.add(subscriber as (DomainEvent) -> Unit)
    }
}
