package com.wesley.beefree.infrastructure.intervention

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.BankingAppForegrounded
import com.wesley.beefree.domain.events.InterventionUIPending
import com.wesley.beefree.domain.intervention.ports.InterventionUI
import org.junit.Test
import org.mockito.kotlin.*

class EMIInterventionModuleTest {
    private val eventBus: EventBus = mock()
    private val interventionUI: InterventionUI = mock()
    private lateinit var module: EMIInterventionModule

    @Test
    fun `should show intervention UI when InterventionUIPending event is published`() {
        val lambdaCaptor = argumentCaptor<(InterventionUIPending) -> Unit>()

        module = EMIInterventionModule(eventBus, interventionUI)

        verify(eventBus).subscribe(eq(InterventionUIPending::class.java), lambdaCaptor.capture())

        val event = InterventionUIPending(reason = "test reason")
        lambdaCaptor.firstValue.invoke(event)

        verify(interventionUI).show("test reason")
    }

    @Test
    fun `should hide intervention UI when BankingAppForegrounded event is published`() {
        val lambdaCaptor = argumentCaptor<(BankingAppForegrounded) -> Unit>()

        module = EMIInterventionModule(eventBus, interventionUI)

        verify(eventBus).subscribe(eq(BankingAppForegrounded::class.java), lambdaCaptor.capture())

        lambdaCaptor.firstValue.invoke(BankingAppForegrounded)

        verify(interventionUI).hide()
    }
}
