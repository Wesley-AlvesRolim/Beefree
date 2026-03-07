package com.wesley.beefreepoc.domain.bus.ports

import com.wesley.beefreepoc.domain.events.DomainEvent

interface EventBus {
    fun publish(event: DomainEvent)

    fun <T : DomainEvent> subscribe(
        eventType: Class<T>,
        subscriber: (T) -> Unit,
    )
}
