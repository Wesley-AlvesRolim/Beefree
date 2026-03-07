package com.wesley.beefree.domain.detection.ports

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.DomainEvent

interface DetectionEngine<Event : DomainEvent> {
    val eventBus: EventBus

    fun detect(event: Event)
}
