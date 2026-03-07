package com.wesley.beefreepoc.infrastructure.intervention

import com.wesley.beefreepoc.domain.bus.ports.EventBus
import com.wesley.beefreepoc.domain.events.InterventionTriggered
import com.wesley.beefreepoc.domain.intervention.ports.InterventionUI
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

        val event = InterventionTriggered("test reason")
        lambdaCaptor.firstValue.invoke(event)

        verify(interventionUI).show()
    }
}
