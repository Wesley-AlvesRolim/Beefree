package com.wesley.beefree.ui.components.designsystem

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.wesley.beefree.ui.theme.BeeFreeTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BeeScale0to10Test {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clickingBarUpdatesValue() {
        var value = 2

        composeTestRule.setContent {
            BeeFreeTheme {
                BeeScale0to10(
                    value = value,
                    onChange = { value = it },
                )
            }
        }

        composeTestRule.onNodeWithTag("bee_scale_option_7").performClick()

        assertEquals(7, value)
    }

    @Test
    fun draggingTrackUpdatesValue() {
        var value = 2

        composeTestRule.setContent {
            BeeFreeTheme {
                BeeScale0to10(
                    value = value,
                    onChange = { value = it },
                )
            }
        }

        composeTestRule.onNodeWithTag("bee_scale_track").performTouchInput {
            down(Offset(8f, 20f))
            moveBy(Offset(1000f, 0f))
            up()
        }

        assertEquals(10, value)
    }
}
