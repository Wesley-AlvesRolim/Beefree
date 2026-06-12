package com.wesley.beefree.ui.screens.settings

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.R
import com.wesley.beefree.ui.theme.BeeFreeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AboutScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun about_screen_renders_title_and_mascot() {
        composeTestRule.setContent {
            BeeFreeTheme {
                AboutScreen()
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        composeTestRule
            .onNodeWithText(context.getString(R.string.settings_about_title))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.onboarding_mascot_description))
            .assertIsDisplayed()
    }

    @Test
    fun about_screen_back_button_invokes_callback() {
        var backCalled = false
        val context = ApplicationProvider.getApplicationContext<Context>()
        composeTestRule.setContent {
            BeeFreeTheme {
                AboutScreen(onBack = { backCalled = true })
            }
        }

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.onboarding_btn_back))
            .performClick()

        assertTrue(backCalled)
    }
}
