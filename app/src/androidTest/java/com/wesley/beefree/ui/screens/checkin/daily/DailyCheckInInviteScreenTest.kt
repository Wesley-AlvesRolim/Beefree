package com.wesley.beefree.ui.screens.checkin.daily

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.R
import com.wesley.beefree.domain.treatments.checkin.TccDailyCheckInFlow
import com.wesley.beefree.ui.theme.BeeFreeTheme
import org.junit.Rule
import org.junit.Test

class DailyCheckInInviteScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    @Test
    fun shows_greeting_and_starts_check_in_when_not_done() {
        var startCheckInCalled = false
        composeTestRule.setContent {
            BeeFreeTheme {
                DailyCheckInInviteScreen(
                    flow = TccDailyCheckInFlow.flow,
                    profileName = "Wesley",
                    isCheckInDone = false,
                    onStartCheckIn = { startCheckInCalled = true },
                    onStartEmotionalRecord = {},
                )
            }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.daily_checkin_invite_greeting, "Wesley"))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.daily_checkin_invite_steps_title))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.daily_checkin_invite_start))
            .assertIsDisplayed()
            .performClick()

        assert(startCheckInCalled)
    }

    @Test
    fun shows_generic_greeting_when_name_is_null() {
        composeTestRule.setContent {
            BeeFreeTheme {
                DailyCheckInInviteScreen(
                    flow = TccDailyCheckInFlow.flow,
                    profileName = null,
                    isCheckInDone = false,
                    onStartCheckIn = {},
                    onStartEmotionalRecord = {},
                )
            }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.daily_checkin_invite_greeting_no_name))
            .assertIsDisplayed()
    }

    @Test
    fun when_already_done_hides_start_and_offers_emotional_record() {
        var startEmotionalRecordCalled = false
        composeTestRule.setContent {
            BeeFreeTheme {
                DailyCheckInInviteScreen(
                    flow = TccDailyCheckInFlow.flow,
                    profileName = "Wesley",
                    isCheckInDone = true,
                    onStartCheckIn = {},
                    onStartEmotionalRecord = { startEmotionalRecordCalled = true },
                )
            }
        }

        composeTestRule
            .onAllNodesWithText(context.getString(R.string.daily_checkin_invite_start))
            .assertCountEquals(0)
        composeTestRule
            .onNodeWithText(context.getString(R.string.daily_checkin_already_done_message))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.daily_checkin_invite_start_emotional_record))
            .assertIsDisplayed()
            .performClick()

        assert(startEmotionalRecordCalled)
    }
}
