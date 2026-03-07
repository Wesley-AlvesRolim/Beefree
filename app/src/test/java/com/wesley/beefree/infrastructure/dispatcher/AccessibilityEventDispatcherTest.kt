package com.wesley.beefree.infrastructure.dispatcher

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.events.ScreenContentCaptured
import com.wesley.beefree.infrastructure.services.OverlayServiceActivity
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class AccessibilityEventDispatcherTest {
    private val eventBus: EventBus = mock()
    private val repository: KeyValueStorageRepository = mock()
    private lateinit var dispatcher: AccessibilityEventDispatcher

    @Before
    fun setUp() {
        OverlayServiceActivity.isRunning = false
        whenever(repository.getTheScreenReaderStatus()).thenReturn(true)
        dispatcher = AccessibilityEventDispatcher(eventBus, repository)
    }

    @Test
    fun `should publish ScreenContentCaptured when event is dispatched`() {
        val packageName = "com.example.app"
        val text = "Hello World"

        val event: AccessibilityEvent = mock()
        whenever(event.packageName).thenReturn(packageName)

        val rootNode: AccessibilityNodeInfo = mock()
        whenever(rootNode.text).thenReturn(text)
        whenever(rootNode.childCount).thenReturn(0)
        whenever(rootNode.isVisibleToUser).thenReturn(true)

        dispatcher.dispatch(event, rootNode)

        argumentCaptor<ScreenContentCaptured>().apply {
            verify(eventBus).publish(capture())
            assertEquals(packageName, firstValue.packageName)
            assertTrue(firstValue.texts.contains(text))
        }
    }

    @Test
    fun `should NOT publish anything when overlay is running`() {
        OverlayServiceActivity.isRunning = true
        val event: AccessibilityEvent = mock()
        val rootNode: AccessibilityNodeInfo = mock()

        dispatcher.dispatch(event, rootNode)

        verify(eventBus, never()).publish(any())
    }

    @Test
    fun `should NOT publish anything when screen reader status is disabled`() {
        whenever(repository.getTheScreenReaderStatus()).thenReturn(false)
        val event: AccessibilityEvent = mock()
        val rootNode: AccessibilityNodeInfo = mock()

        dispatcher.dispatch(event, rootNode)

        verify(eventBus, never()).publish(any())
    }

    @Test
    fun `should extract text from child nodes`() {
        val rootText = "Root Node"
        val childText = "Child Node"

        val event: AccessibilityEvent = mock()
        whenever(event.packageName).thenReturn("com.example.app")

        val rootNode: AccessibilityNodeInfo = mock()
        whenever(rootNode.text).thenReturn(rootText)
        whenever(rootNode.childCount).thenReturn(1)
        whenever(rootNode.isVisibleToUser).thenReturn(true)

        val childNode: AccessibilityNodeInfo = mock()
        whenever(childNode.text).thenReturn(childText)
        whenever(childNode.childCount).thenReturn(0)
        whenever(childNode.isVisibleToUser).thenReturn(true)

        whenever(rootNode.getChild(0)).thenReturn(childNode)

        dispatcher.dispatch(event, rootNode)

        argumentCaptor<ScreenContentCaptured>().apply {
            verify(eventBus).publish(capture())
            assertTrue(firstValue.texts.contains(rootText))
            assertTrue(firstValue.texts.contains(childText))
        }
    }

    @Test
    fun `should extract text from contentDescription and paneTitle`() {
        val text = "Content Description of something bad"

        val event: AccessibilityEvent = mock()
        whenever(event.packageName).thenReturn("com.example.app")

        val rootNode: AccessibilityNodeInfo = mock()
        whenever(rootNode.text).thenReturn(null)
        whenever(rootNode.contentDescription).thenReturn(text)
        whenever(rootNode.childCount).thenReturn(0)
        whenever(rootNode.isVisibleToUser).thenReturn(true)

        dispatcher.dispatch(event, rootNode)

        argumentCaptor<ScreenContentCaptured>().apply {
            verify(eventBus).publish(capture())
            assertTrue(firstValue.texts.contains(text))
        }
    }

    @Test
    fun `should NOT extract text from nodes that are NOT visible to user`() {
        val visibleText = "Visible Node"
        val hiddenText = "Hidden Node"

        val event: AccessibilityEvent = mock()
        whenever(event.packageName).thenReturn("com.example.app")

        val rootNode: AccessibilityNodeInfo = mock()
        whenever(rootNode.text).thenReturn(visibleText)
        whenever(rootNode.isVisibleToUser).thenReturn(true)
        whenever(rootNode.childCount).thenReturn(1)

        val childNode: AccessibilityNodeInfo = mock()
        whenever(childNode.text).thenReturn(hiddenText)
        whenever(childNode.isVisibleToUser).thenReturn(false)
        whenever(childNode.childCount).thenReturn(0)

        whenever(rootNode.getChild(0)).thenReturn(childNode)

        dispatcher.dispatch(event, rootNode)

        argumentCaptor<ScreenContentCaptured>().apply {
            verify(eventBus).publish(capture())
            assertTrue(firstValue.texts.contains(visibleText))
            assertFalse(firstValue.texts.contains(hiddenText))
        }
    }
}
