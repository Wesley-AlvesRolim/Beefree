package com.wesley.beefree.infrastructure.bus.subscribers.history

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.infrastructure.storage.ports.AddictionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class RelapseRecorderModuleTest {
    private lateinit var eventBus: EventBus
    private lateinit var addictionRepository: AddictionRepository
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
    private var currentTime: Long = 0
    private lateinit var module: RelapseRecorderModule
    private lateinit var subscriber: (InterventionTriggered) -> Unit

    @Before
    fun setup() {
        eventBus = mock()
        addictionRepository = mock()
        val lambdaCaptor = argumentCaptor<(InterventionTriggered) -> Unit>()
        module =
            RelapseRecorderModule(eventBus, addictionRepository, coroutineScope, { currentTime })

        verify(eventBus).subscribe(
            eq(InterventionTriggered::class.java),
            lambdaCaptor.capture(),
        )
        subscriber = lambdaCaptor.firstValue
    }

    @Test
    fun `should save relapse when InterventionTriggered event is published`() {
        runBlocking {
            currentTime = 1000
            val event =
                InterventionTriggered(
                    reason = "I bet you",
                    keyword = "bet",
                    addictionCategoryId = 1,
                    appPackage = "com.example.app",
                )
            subscriber.invoke(event)

            verify(addictionRepository).insertRelapse(
                RelapseRecord(
                    addictionCategoryId = 1,
                    keywordDetected = "bet",
                    detectedText = "I bet you",
                    createdAt = event.timestamp,
                ),
            )
        }
    }

    @Test
    fun `should not save relapse if another was saved within 5 seconds`() {
        runBlocking {
            currentTime = 1000
            val firstEvent = InterventionTriggered("reason1", "key1", 1, "app1")
            subscriber.invoke(firstEvent)

            currentTime = 5999
            val secondEventThatShouldNotBeTriggered = InterventionTriggered("reason2", "key2", 1, "app1")
            subscriber.invoke(secondEventThatShouldNotBeTriggered)

            verify(addictionRepository, times(1)).insertRelapse(any())
        }
    }

    @Test
    fun `should save only the phrase containing the keyword, not the full text`() {
        runBlocking {
            currentTime = 1000
            val event =
                InterventionTriggered(
                    reason = "Safe sentence. I bet you something bad. Another safe sentence.",
                    keyword = "bet",
                    addictionCategoryId = 1,
                    appPackage = "com.example.app",
                )
            subscriber.invoke(event)

            verify(addictionRepository).insertRelapse(
                RelapseRecord(
                    addictionCategoryId = 1,
                    keywordDetected = "bet",
                    detectedText = "I bet you something bad.",
                    createdAt = event.timestamp,
                ),
            )
        }
    }

    @Test
    fun `should save relapse again after 5 seconds have passed`() {
        runBlocking {
            currentTime = 1000
            val event1 = InterventionTriggered("reason1", "key1", 1, "app1")
            subscriber.invoke(event1)

            currentTime = 6000
            val secondEventThatShouldTrigger = InterventionTriggered("reason2", "key2", 1, "app1")
            subscriber.invoke(secondEventThatShouldTrigger)

            verify(addictionRepository, times(2)).insertRelapse(any())
        }
    }
}
