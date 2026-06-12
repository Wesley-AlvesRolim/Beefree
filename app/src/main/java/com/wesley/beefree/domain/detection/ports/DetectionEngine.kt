package com.wesley.beefree.domain.detection.ports

import com.wesley.beefree.domain.events.DomainEvent
import com.wesley.beefree.domain.events.ports.EventBus

interface DetectionEngine<Event : DomainEvent> {
    val eventBus: EventBus

    fun detect(event: Event)
}
