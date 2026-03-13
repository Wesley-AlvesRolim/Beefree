package com.wesley.beefree.infrastructure.intervention

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.domain.intervention.ports.InterventionUI

class OverlayInterventionModule(
    private val eventBus: EventBus,
    private val interventionUI: InterventionUI,
) {
    init {
        eventBus.subscribe(InterventionTriggered::class.java) { event ->
            interventionUI.show(event.reason)
        }
    }
}
