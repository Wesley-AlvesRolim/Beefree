package com.wesley.beefree.ui.screens.onboading

import android.app.Application
import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.wesley.beefree.R
import com.wesley.beefree.ui.viewmodel.OnboardingViewModelImpl
import com.wesley.beefree.ui.viewmodel.ports.AddictionCategory
import com.wesley.beefree.ui.viewmodel.ports.OnboardingStep
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MockOnboardingViewModel(
    application: Application,
) : OnboardingViewModelImpl(application) {
    var nextStepCalled = 0
    var toggleAddictionCalledWith = mutableListOf<AddictionCategory>()
    var openAccessibilitySettingsCalledCount = 0
    var openOverlaySettingsCalledCount = 0
    var finishOnboardingCalledCount = 0
    var updatePermissionsCalledCount = 0

    private var pinnedAccessibility: Boolean? = null
    private var pinnedOverlay: Boolean? = null

    fun setAccessibilityEnabled(enabled: Boolean) {
        pinnedAccessibility = enabled
        openIsAccessibilityEnabled.value = enabled
    }

    fun setOverlayEnabled(enabled: Boolean) {
        pinnedOverlay = enabled
        openIsOverlayEnabled.value = enabled
    }

    override fun updatePermissions(context: Context) {
        updatePermissionsCalledCount++
        super.updatePermissions(context)
        pinnedAccessibility?.let { openIsAccessibilityEnabled.value = it }
        pinnedOverlay?.let { openIsOverlayEnabled.value = it }
    }

    override fun openAccessibilitySettings(context: Context) {
        openAccessibilitySettingsCalledCount++
        super.openAccessibilitySettings(context)
    }

    override fun openOverlaySettings(context: Context) {
        openOverlaySettingsCalledCount++
        super.openOverlaySettings(context)
    }

    override fun toggleAddiction(category: AddictionCategory) {
        toggleAddictionCalledWith.add(category)
        super.toggleAddiction(category)
    }

    override fun nextStep() {
        nextStepCalled++
        super.nextStep()
    }

    override fun finishOnboarding(onFinish: () -> Unit) {
        finishOnboardingCalledCount++
        onFinish()
    }
}

class OnboardingScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val application = context.applicationContext as Application

    @Test
    fun onboarding_navigation_flow_test() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val viewModel = MockOnboardingViewModel(application)

        composeTestRule.setContent {
            OnboardingScreen(
                onFinish = {},
                viewModel = viewModel,
            )
        }

        // 1. Welcome Screen
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_welcome_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performClick()
        assertTrue(viewModel.nextStepCalled == 1)
        assertTrue(viewModel.currentStep.value == OnboardingStep.HOW_IT_WORKS)

        // 2. How it Works Screen
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_how_it_works_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performClick()
        assertTrue(viewModel.nextStepCalled == 2)
        assertTrue(viewModel.currentStep.value == OnboardingStep.ADDICTION_SELECTOR)

        // 3. Addiction Selector Screen
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_addiction_selector_title)).assertExists()
        // Select an addiction to enable "Continue"
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_addiction_bets)).performClick()
        assertTrue(viewModel.toggleAddictionCalledWith.contains(AddictionCategory.BETS))
        assertTrue(viewModel.selectedAddictions.value.contains(AddictionCategory.BETS))

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performClick()
        assertTrue(viewModel.nextStepCalled == 3)
        assertTrue(viewModel.currentStep.value == OnboardingStep.REQUEST_PERMISSIONS)

        // 4. Request Permissions Summary Screen
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_request_permissions_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performClick()
        assertTrue(viewModel.nextStepCalled == 4)
        assertTrue(viewModel.currentStep.value == OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR)

        // 5. Accessibility Permission Screen
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_monitor_permission_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_monitor_permission_enable)).performClick()
        assertTrue(viewModel.openAccessibilitySettingsCalledCount == 1)

        // Simulate permission granted
        device.pressBack()
        composeTestRule.waitForIdle()
        viewModel.setAccessibilityEnabled(true) // activated after DisposableEffect is called
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performClick()
        assertTrue(viewModel.nextStepCalled == 5)
        assertTrue(viewModel.currentStep.value == OnboardingStep.REQUEST_PERMISSION_SCREEN_OVERLAY)

        // 6. Overlay Permission Screen
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_overlay_permission_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_overlay_permission_enable)).performClick()
        assertTrue(viewModel.openOverlaySettingsCalledCount == 1)

        // Simulate permission granted
        device.pressBack()
        composeTestRule.waitForIdle()
        viewModel.setOverlayEnabled(true)
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_continue)).performClick()
        assertTrue(viewModel.nextStepCalled == 6)
        assertTrue(viewModel.currentStep.value == OnboardingStep.FINISH)

        // 7. Finish Screen
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_finish_title)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_start)).performClick()
        assertTrue(viewModel.finishOnboardingCalledCount == 1)
    }

    @Test
    fun onboarding_permissions_update_state() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val viewModel = MockOnboardingViewModel(application)
        viewModel.moveToStep(OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR)

        composeTestRule.setContent {
            OnboardingScreen(
                onFinish = {},
                viewModel = viewModel,
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_monitor_permission_enable)).performClick()
        assertTrue(viewModel.openAccessibilitySettingsCalledCount == 1)
        viewModel.updatePermissionsCalledCount = 0
        device.pressBack()
        composeTestRule.waitForIdle()
        assertTrue(viewModel.updatePermissionsCalledCount == 1)
    }

    @Test
    fun finishOnboarding_calls_viewModel_finishOnboarding() {
        val viewModel = MockOnboardingViewModel(application)
        viewModel.moveToStep(OnboardingStep.FINISH)
        viewModel.toggleAddiction(AddictionCategory.BETS)

        var finished = false
        composeTestRule.setContent {
            OnboardingScreen(
                onFinish = { finished = true },
                viewModel = viewModel,
            )
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText(context.getString(R.string.onboarding_btn_start)).performClick()
        assertTrue(viewModel.finishOnboardingCalledCount == 1)
        composeTestRule.waitForIdle()
        assertTrue(finished)
    }
}
