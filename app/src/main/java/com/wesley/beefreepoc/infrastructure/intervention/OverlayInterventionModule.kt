package com.wesley.beefreepoc.infrastructure.intervention

import com.wesley.beefreepoc.domain.bus.ports.EventBus
import com.wesley.beefreepoc.domain.events.InterventionTriggered
import com.wesley.beefreepoc.domain.intervention.ports.InterventionUI

class OverlayInterventionModule(
    private val eventBus: EventBus,
    private val interventionUI: InterventionUI,
) {
    init {
        eventBus.subscribe(InterventionTriggered::class.java) { event ->
            interventionUI.show()
        }
    }
}
