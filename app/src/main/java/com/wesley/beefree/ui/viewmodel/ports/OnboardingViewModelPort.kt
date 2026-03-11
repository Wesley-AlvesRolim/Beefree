package com.wesley.beefree.ui.viewmodel.ports

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

enum class OnboardingStep {
    WELCOME,
    HOW_IT_WORKS,
    ADDICTION_SELECTOR,
    REQUEST_PERMISSIONS,
    REQUEST_PERMISSION_SCREEN_MONITOR,
    REQUEST_PERMISSION_SCREEN_OVERLAY,
    FINISH,
}

enum class AddictionCategory {
    ADULT_CONTENT,
    BETS,
    OTHERS,
}

interface OnboardingViewModelPort {
    val currentStep: StateFlow<OnboardingStep>
    val selectedAddictions: StateFlow<Set<AddictionCategory>>
    val isAccessibilityEnabled: StateFlow<Boolean>
    val isOverlayEnabled: StateFlow<Boolean>

    fun updatePermissions(context: Context)

    fun openAccessibilitySettings(context: Context)

    fun openOverlaySettings(context: Context)

    fun toggleAddiction(category: AddictionCategory)

    fun moveToStep(step: OnboardingStep)

    fun nextStep()

    fun previousStep()

    fun finishOnboarding(onFinish: () -> Unit)
}
