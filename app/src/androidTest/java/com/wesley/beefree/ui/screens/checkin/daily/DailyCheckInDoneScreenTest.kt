package com.wesley.beefree.ui.screens.checkin.daily

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.R
import com.wesley.beefree.ui.theme.BeeFreeTheme
import org.junit.Rule
import org.junit.Test

class DailyCheckInDoneScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    @Test
    fun shows_completion_title_and_subtitle() {
        composeTestRule.setContent {
            BeeFreeTheme { DailyCheckInDoneScreen(onClose = {}) }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.daily_checkin_done_title))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.daily_checkin_done_subtitle))
            .assertIsDisplayed()
    }

    @Test
    fun back_button_invokes_callback() {
        var closeCalled = false
        composeTestRule.setContent {
            BeeFreeTheme { DailyCheckInDoneScreen(onClose = { closeCalled = true }) }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.daily_checkin_done_back))
            .assertIsDisplayed()
            .performClick()

        assert(closeCalled)
    }
}
