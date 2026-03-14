package com.wesley.beefree.infrastructure.intervention

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.domain.intervention.ports.InterventionUI
import org.junit.Test
import org.mockito.kotlin.*

class OverlayInterventionModuleTest {
    private val eventBus: EventBus = mock()
    private val interventionUI: InterventionUI = mock()
    private lateinit var module: OverlayInterventionModule

    @Test
    fun `should show intervention UI when InterventionTriggered event is published`() {
        val lambdaCaptor = argumentCaptor<(InterventionTriggered) -> Unit>()

        module = OverlayInterventionModule(eventBus, interventionUI)

        verify(eventBus).subscribe(eq(InterventionTriggered::class.java), lambdaCaptor.capture())

        val event = InterventionTriggered(reason = "test reason", keyword = "test reason", addictionTypeId = 1)
        lambdaCaptor.firstValue.invoke(event)

        verify(interventionUI).show("test reason")
    }
}
