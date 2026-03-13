package com.wesley.beefree.ui.viewmodel.mocks

import android.content.Context
import com.wesley.beefree.ui.viewmodel.ports.AddictionCategory
import com.wesley.beefree.ui.viewmodel.ports.OnboardingStep
import com.wesley.beefree.ui.viewmodel.ports.OnboardingViewModelPort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnboardingViewModelMock : OnboardingViewModelPort {
    override val currentStep: MutableStateFlow<OnboardingStep> =
        MutableStateFlow(OnboardingStep.WELCOME)
    override val selectedAddictions: MutableStateFlow<Set<AddictionCategory>> =
        MutableStateFlow(emptySet())
    override val isAccessibilityEnabled: StateFlow<Boolean> = MutableStateFlow(false)
    override val isOverlayEnabled: StateFlow<Boolean> = MutableStateFlow(false)

    override fun updatePermissions(context: Context) {}

    override fun openAccessibilitySettings(context: Context) {}

    override fun openOverlaySettings(context: Context) {}

    override fun toggleAddiction(category: AddictionCategory) {
        val current = selectedAddictions.value
        selectedAddictions.value =
            if (current.contains(category)) current - category else current + category
    }

    override fun moveToStep(step: OnboardingStep) {
        currentStep.value = step
    }

    override fun nextStep() {
        currentStep.value =
            when (currentStep.value) {
                OnboardingStep.WELCOME -> OnboardingStep.HOW_IT_WORKS
                OnboardingStep.HOW_IT_WORKS -> OnboardingStep.ADDICTION_SELECTOR
                OnboardingStep.ADDICTION_SELECTOR -> OnboardingStep.REQUEST_PERMISSIONS
                OnboardingStep.REQUEST_PERMISSIONS -> OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR
                OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR -> OnboardingStep.REQUEST_PERMISSION_SCREEN_OVERLAY
                OnboardingStep.REQUEST_PERMISSION_SCREEN_OVERLAY -> OnboardingStep.FINISH
                OnboardingStep.FINISH -> OnboardingStep.FINISH
            }
    }

    override fun previousStep() {
        currentStep.value =
            when (currentStep.value) {
                OnboardingStep.WELCOME -> OnboardingStep.WELCOME
                OnboardingStep.HOW_IT_WORKS -> OnboardingStep.WELCOME
                OnboardingStep.ADDICTION_SELECTOR -> OnboardingStep.HOW_IT_WORKS
                OnboardingStep.REQUEST_PERMISSIONS -> OnboardingStep.ADDICTION_SELECTOR
                OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR -> OnboardingStep.REQUEST_PERMISSIONS
                OnboardingStep.REQUEST_PERMISSION_SCREEN_OVERLAY -> OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR
                OnboardingStep.FINISH -> OnboardingStep.REQUEST_PERMISSION_SCREEN_OVERLAY
            }
    }

    override fun finishOnboarding(onFinish: () -> Unit) {
        onFinish()
    }
}
