package com.wesley.beefree.ui.screens.checkin.daily

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.treatments.checkin.TccDailyCheckInFlow
import com.wesley.beefree.ui.theme.BeeFreeTheme
import org.junit.Rule
import org.junit.Test

class DailyCheckInFlowScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    private val flow = TccDailyCheckInFlow.flow

    private fun setFlowContent(onClose: () -> Unit = {}) {
        composeTestRule.setContent {
            BeeFreeTheme {
                DailyCheckInFlowScreen(
                    flow = flow,
                    treatmentProfile = TreatmentProfile.TCC,
                    currentStep = flow.steps.first(),
                    stepNumber = 1,
                    totalSteps = flow.steps.size,
                    answers = emptyMap(),
                    hasEmotionalRecordToday = false,
                    todaysEmotionRecord = null,
                    selectedActivity = null,
                    onAnswer = { _, _ -> },
                    onSelectActivity = {},
                    onGoRecord = {},
                    onPrevious = {},
                    onNext = {},
                    onClose = onClose,
                    canGoBack = false,
                )
            }
        }
    }

    @Test
    fun shows_flow_title_and_close_action() {
        setFlowContent()

        composeTestRule
            .onNodeWithText(context.getString(R.string.daily_checkin_flow_title))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.daily_checkin_close_description))
            .assertIsDisplayed()
    }

    @Test
    fun close_action_invokes_callback() {
        var closeCalled = false
        setFlowContent(onClose = { closeCalled = true })

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.daily_checkin_close_description))
            .performClick()

        assert(closeCalled)
    }
}
