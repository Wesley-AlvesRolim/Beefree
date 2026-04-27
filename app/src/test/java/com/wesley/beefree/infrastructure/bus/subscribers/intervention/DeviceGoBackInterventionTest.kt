package com.wesley.beefree.infrastructure.bus.subscribers.intervention

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.domain.events.InterventionUIPending
import com.wesley.beefree.domain.intervention.ports.DeviceActionProvider
import com.wesley.beefree.infrastructure.bus.subscribers.intervention.DeviceGoBackIntervention
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DeviceGoBackInterventionTest {
    private val eventBus: EventBus = mock()
    private val deviceActionProvider: DeviceActionProvider = mock()
    private lateinit var module: DeviceGoBackIntervention

    @Test
    fun `should perform go back when InterventionTriggered event is published`() {
        runBlocking {
            val lambdaCaptor = argumentCaptor<(InterventionTriggered) -> Unit>()

            module = DeviceGoBackIntervention(eventBus, deviceActionProvider)

            verify(eventBus).subscribe(
                eq(InterventionTriggered::class.java),
                lambdaCaptor.capture(),
            )

            val event =
                InterventionTriggered(
                    reason = "test reason",
                    keyword = "test keyword",
                    addictionCategoryId = 1,
                )
            lambdaCaptor.firstValue.invoke(event)

            verify(deviceActionProvider).performGoBack()

            val startTime = System.currentTimeMillis()
            while (System.currentTimeMillis() - startTime < 1000) {
                try {
                    verify(eventBus).publish(any<InterventionUIPending>())
                    verify(eventBus).publish(
                        argThat {
                            this is InterventionUIPending && this.reason == event.reason
                        },
                    )
                    break
                } catch (e: Throwable) {
                    delay(50)
                }
            }
        }
    }
}
