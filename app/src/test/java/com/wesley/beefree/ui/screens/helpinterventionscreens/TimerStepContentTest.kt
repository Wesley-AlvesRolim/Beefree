package com.wesley.beefree.ui.screens.helpinterventionscreens

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TimerStepContentTest {
    @Test
    fun `timer does not auto complete before duration`() {
        assertFalse(shouldAutoCompleteTimer(9, timerStarted = true, timerCompleted = false))
    }

    @Test
    fun `timer auto completes at ten seconds when not already completed`() {
        assertTrue(shouldAutoCompleteTimer(10, timerStarted = true, timerCompleted = false))
    }

    @Test
    fun `timer does not auto complete after completion`() {
        assertFalse(shouldAutoCompleteTimer(10, timerStarted = true, timerCompleted = true))
    }
}
