package com.wesley.beefree.infrastructure.intervention

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.domain.events.InterventionUIPending
import com.wesley.beefree.domain.intervention.ports.DeviceActionProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DeviceGoBackIntervention(
    private val eventBus: EventBus,
    private val deviceActionProvider: DeviceActionProvider,
) {
    init {
        eventBus.subscribe(InterventionTriggered::class.java) { event ->
            deviceActionProvider.performGoBack()
            CoroutineScope(Dispatchers.Main).launch {
                delay(300)
                eventBus.publish(InterventionUIPending(event.reason))
            }
        }
    }
}
