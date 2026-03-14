package com.wesley.beefree.infrastructure.intervention

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.domain.intervention.ports.DeviceActionProvider

class DeviceGoBackIntervention(
    private val eventBus: EventBus,
    private val deviceActionProvider: DeviceActionProvider,
) {
    init {
        eventBus.subscribe(InterventionTriggered::class.java) {
            deviceActionProvider.performGoBack()
        }
    }
}
