package com.wesley.beefreepoc.domain.detection.ports

import com.wesley.beefreepoc.domain.bus.ports.EventBus
import com.wesley.beefreepoc.domain.events.DomainEvent

interface DetectionEngine<Event : DomainEvent> {
    val eventBus: EventBus

    fun detect(event: Event)
}
