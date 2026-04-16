package com.wesley.beefree.infrastructure.intervention

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.BankingAppForegrounded
import com.wesley.beefree.domain.events.InterventionUIPending
import com.wesley.beefree.domain.intervention.ports.InterventionUI

class EMIInterventionModule(
    private val eventBus: EventBus,
    private val interventionUI: InterventionUI,
) {
    init {
        eventBus.subscribe(InterventionUIPending::class.java) { event ->
            interventionUI.show(event.reason)
        }
        eventBus.subscribe(BankingAppForegrounded::class.java) {
            interventionUI.hide()
        }
    }
}
