package com.wesley.beefree.infrastructure.intervention

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.domain.intervention.ports.DeviceActionProvider
import org.junit.Test
import org.mockito.kotlin.*

class DeviceGoBackInterventionTest {
    private val eventBus: EventBus = mock()
    private val deviceActionProvider: DeviceActionProvider = mock()
    private lateinit var module: DeviceGoBackIntervention

    @Test
    fun `should perform go back when InterventionTriggered event is published`() {
        val lambdaCaptor = argumentCaptor<(InterventionTriggered) -> Unit>()

        module = DeviceGoBackIntervention(eventBus, deviceActionProvider)

        verify(eventBus).subscribe(eq(InterventionTriggered::class.java), lambdaCaptor.capture())

        val event = InterventionTriggered(reason = "test reason", keyword = "test keyword", addictionTypeId = 1)
        lambdaCaptor.firstValue.invoke(event)

        verify(deviceActionProvider).performGoBack()
    }
}
