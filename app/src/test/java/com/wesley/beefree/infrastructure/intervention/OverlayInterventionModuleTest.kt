package com.wesley.beefree.infrastructure.intervention

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.BankingAppForegrounded
import com.wesley.beefree.domain.events.InterventionUIPending
import com.wesley.beefree.domain.intervention.ports.InterventionUI
import org.junit.Test
import org.mockito.kotlin.*

class OverlayInterventionModuleTest {
    private val eventBus: EventBus = mock()
    private val interventionUI: InterventionUI = mock()
    private lateinit var module: OverlayInterventionModule

    @Test
    fun `should show intervention UI when InterventionUIPending event is published`() {
        val lambdaCaptor = argumentCaptor<(InterventionUIPending) -> Unit>()

        module = OverlayInterventionModule(eventBus, interventionUI)

        verify(eventBus).subscribe(eq(InterventionUIPending::class.java), lambdaCaptor.capture())

        val event = InterventionUIPending(reason = "test reason")
        lambdaCaptor.firstValue.invoke(event)

        verify(interventionUI).show("test reason")
    }

    @Test
    fun `should hide intervention UI when BankingAppForegrounded event is published`() {
        val lambdaCaptor = argumentCaptor<(BankingAppForegrounded) -> Unit>()

        module = OverlayInterventionModule(eventBus, interventionUI)

        verify(eventBus).subscribe(eq(BankingAppForegrounded::class.java), lambdaCaptor.capture())

        lambdaCaptor.firstValue.invoke(BankingAppForegrounded)

        verify(interventionUI).hide()
    }
}
