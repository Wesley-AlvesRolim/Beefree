package com.wesley.beefree.ui.components.designsystem

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.R
import com.wesley.beefree.ui.theme.BeeFreeTheme
import org.junit.Rule
import org.junit.Test

class BeeTimePickerFieldTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shows_formatted_time_when_selected() {
        composeTestRule.setContent {
            BeeFreeTheme {
                BeeTimePickerField(
                    selectedHour = 9,
                    selectedMinute = 5,
                    onTimeSelect = { _, _ -> },
                    emptyText = "--:--",
                )
            }
        }

        composeTestRule.onNodeWithText("09:05").assertExists()
    }

    @Test
    fun opens_dialog_when_clicked() {
        var selectedHour: Int? = null
        var selectedMinute: Int? = null
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()

        composeTestRule.setContent {
            BeeFreeTheme {
                BeeTimePickerField(
                    selectedHour = selectedHour,
                    selectedMinute = selectedMinute,
                    onTimeSelect = { hour, minute ->
                        selectedHour = hour
                        selectedMinute = minute
                    },
                    emptyText = context.getString(R.string.daily_checkin_relapse_time_empty),
                )
            }
        }

        composeTestRule.onNodeWithText(context.getString(R.string.daily_checkin_relapse_time_empty)).performClick()

        composeTestRule.onNodeWithText(context.getString(R.string.confirm)).assertExists()
    }
}
