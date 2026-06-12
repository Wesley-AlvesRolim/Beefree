package com.wesley.beefree.ui.screens

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.ui.theme.BeeFreeTheme
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    private val user =
        UserProfile(
            profileName = "Wesley",
            createdAt = 0L,
            updatedAt = 0L,
        )

    @Test
    fun greeting_shows_user_name() {
        composeTestRule.setContent {
            BeeFreeTheme {
                HomeScreenContent(
                    user = user,
                    psychoeducationMessage = null,
                    relapseHistory = emptyList(),
                    relapseSuccessRate = 1f,
                )
            }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.home_greeting, "Wesley"))
            .assertIsDisplayed()
    }

    @Test
    fun check_in_banner_is_shown_and_invokes_callback_when_not_checked_in() {
        var openCheckInCalled = false
        composeTestRule.setContent {
            BeeFreeTheme {
                HomeScreenContent(
                    user = user,
                    psychoeducationMessage = null,
                    relapseHistory = emptyList(),
                    relapseSuccessRate = 1f,
                    hasCheckedInToday = false,
                    onOpenCheckIn = { openCheckInCalled = true },
                )
            }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.home_checkin_banner_cta))
            .assertIsDisplayed()
            .performClick()

        assert(openCheckInCalled)
    }

    @Test
    fun check_in_banner_is_hidden_and_psychoeducation_shown_when_checked_in() {
        val message = "Continue firme hoje."
        composeTestRule.setContent {
            BeeFreeTheme {
                HomeScreenContent(
                    user = user,
                    psychoeducationMessage = message,
                    relapseHistory = emptyList(),
                    relapseSuccessRate = 1f,
                    hasCheckedInToday = true,
                )
            }
        }

        composeTestRule
            .onAllNodesWithText(context.getString(R.string.home_checkin_banner_cta))
            .assertCountEquals(0)
        composeTestRule
            .onNodeWithText(message)
            .assertIsDisplayed()
    }

    @Test
    fun quick_help_fab_invokes_callback() {
        var openHelpCalled = false
        composeTestRule.setContent {
            BeeFreeTheme {
                HomeScreenContent(
                    user = user,
                    psychoeducationMessage = null,
                    relapseHistory = emptyList(),
                    relapseSuccessRate = 1f,
                    onOpenHelpIntervention = { openHelpCalled = true },
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.quick_help_button_description))
            .assertIsDisplayed()
            .performClick()

        assert(openHelpCalled)
    }
}
