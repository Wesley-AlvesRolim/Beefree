package com.wesley.beefree.infrastructure.bus.adapters

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.DomainEvent
import com.wesley.beefree.infrastructure.logging.Logger

class InMemoryEventBus(
    private val logger: Logger =
        object : Logger {
            override fun d(
                tag: String,
                message: String,
            ) {}

            override fun info(tag: String, message: String) {}
        },
) : EventBus {
    private val subscribers = mutableMapOf<Class<out DomainEvent>, MutableList<(DomainEvent) -> Unit>>()
    private val tag = "EventBus"

    override fun publish(event: DomainEvent) {
        logger.d(tag, "publish ${event::class.java.name}")
        subscribers[event::class.java]?.forEach { subscriber ->
            subscriber(event)
        }
    }

    override fun <T : DomainEvent> subscribe(
        eventType: Class<T>,
        subscriber: (T) -> Unit,
    ) {
        logger.d(tag, "subscribe ${eventType.name}")
        val list = subscribers.getOrPut(eventType) { mutableListOf() }
        list.add(subscriber as (DomainEvent) -> Unit)
    }
}
