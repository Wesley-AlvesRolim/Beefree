package com.wesley.beefree.ui.screens.onboarding

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.ClinicalProfile
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.ScaleResult
import com.wesley.beefree.domain.onboarding.StepType
import com.wesley.beefree.domain.onboarding.engine.CompositeOnboardingFlowEngine
import com.wesley.beefree.domain.onboarding.engine.OnboardingFlowFactory
import com.wesley.beefree.ui.viewmodel.ports.OnboardingViewModelPort
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MockOnboardingViewModel : OnboardingViewModelPort {
    private val engine = CompositeOnboardingFlowEngine(OnboardingFlowFactory.factory())

    override val currentStep = MutableStateFlow(engine.currentStep)
    override val answers = MutableStateFlow(OnboardingAnswers())
    override val scaleResult = MutableStateFlow<ScaleResult?>(null)
    override val clinicalProfile = MutableStateFlow<ClinicalProfile?>(null)
    override val isAccessibilityEnabled = MutableStateFlow(false)
    override val isOverlayEnabled = MutableStateFlow(false)

    var nextCalled = 0
    var previousCalled = 0
    var updatePermissionsCalled = 0
    var openAccessibilityCalled = 0
    var openOverlayCalled = 0
    var finishCalled = 0

    override fun updateAnswer(update: OnboardingAnswers.() -> OnboardingAnswers) {
        answers.value = answers.value.update()
    }

    override fun next() {
        nextCalled++
        engine.next(answers.value)
        currentStep.value = engine.currentStep
    }

    override fun previous() {
        previousCalled++
        engine.previous()
        currentStep.value = engine.currentStep
    }

    override fun updatePermissions(context: Context) {
        updatePermissionsCalled++
    }

    override fun openAccessibilitySettings(context: Context) {
        openAccessibilityCalled++
    }

    override fun openOverlaySettings(context: Context) {
        openOverlayCalled++
    }

    override fun finishOnboarding(
        onFinish: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        finishCalled++
        onFinish()
    }
}

class OnboardingScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun onboarding_navigation_flow_ppu_test() {
        val viewModel = MockOnboardingViewModel()

        composeTestRule.setContent {
            OnboardingScreen(
                onFinish = {},
                viewModel = viewModel,
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_welcome_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performClick()
        assertEquals(StepType.PRESENTATION, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_how_it_works_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performClick()
        assertEquals(StepType.ASK_NAME, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_ask_name_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_ask_name_hint)).performTextInput("Wesley")
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performClick()
        assertEquals(StepType.ADDICTION_SELECTOR, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_addiction_selector_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_addiction_ppu)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performClick()
        assertEquals(StepType.GENDER, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_gender_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_gender_male)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performClick()
        assertEquals(StepType.PPCS6_FORM, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_ppcs6_title)).assertExists()
        repeat(6) {
            composeTestRule.onNodeWithText(context.getString(R.string.onboarding_frequency_1)).performClick()
            composeTestRule
                .onNodeWithText(context.getString(R.string.onboarding_btn_continue))
                .performScrollTo()
                .performClick()
        }
        assertEquals(StepType.EMA_FORM, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_ema_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_ema_q1)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_ema_label_3)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performScrollTo().performClick()
        assertEquals(StepType.FREQUENCY_FORM, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_frequency_form_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_ppu_freq_3)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performScrollTo().performClick()
        assertEquals(StepType.SYMPTOMS, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_symptoms_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_symptom_anxiety)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performScrollTo().performClick()
        assertEquals(StepType.NEURODIVERGENCE, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_neurodivergence_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_neurodivergence_no)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performScrollTo().performClick()
        assertEquals(StepType.HOBBIES, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_hobbies_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_hobby_exercise)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performScrollTo().performClick()
        assertEquals(StepType.GOALS, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_goals_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_goal_relationships)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performScrollTo().performClick()
        assertEquals(StepType.SCORE_RESULT, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_score_result_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performScrollTo().performClick()
        assertEquals(StepType.CORE_VALUES, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_core_values_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_core_value_family)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_core_value_faith)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_core_value_honesty)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performScrollTo().performClick()
        assertEquals(StepType.REQUEST_PERMISSIONS, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_request_permissions_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performScrollTo().performClick()
        assertEquals(StepType.REQUEST_PERMISSION_MONITOR, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_monitor_permission_title)).assertExists()
        viewModel.isAccessibilityEnabled.value = true
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performScrollTo().performClick()
        assertEquals(StepType.REQUEST_PERMISSION_OVERLAY, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_overlay_permission_title)).assertExists()
        viewModel.isOverlayEnabled.value = true
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performScrollTo().performClick()
        assertEquals(StepType.FINISH, viewModel.currentStep.value.type)

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_finish_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_start)).performClick()
        assertTrue(viewModel.finishCalled == 1)
    }
}
