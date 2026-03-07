package com.wesley.beefree.domain.bus.ports

import com.wesley.beefree.domain.events.DomainEvent

interface EventBus {
    fun publish(event: DomainEvent)

    fun <T : DomainEvent> subscribe(
        eventType: Class<T>,
        subscriber: (T) -> Unit,
    )
}
