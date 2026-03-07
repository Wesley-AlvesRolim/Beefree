package com.wesley.beefreepoc.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.wesley.beefreepoc.ui.screens.OnboardingStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel : ViewModel() {
    private val _currentStep = MutableStateFlow(OnboardingStep.WELCOME)
    val currentStep: StateFlow<OnboardingStep> = _currentStep.asStateFlow()

    fun moveToStep(step: OnboardingStep) {
        _currentStep.value = step
    }

    fun nextStep() {
        val next =
            when (_currentStep.value) {
                OnboardingStep.WELCOME -> OnboardingStep.HELP
                OnboardingStep.HELP -> OnboardingStep.PERMISSION_SCREEN_MONITOR
                OnboardingStep.PERMISSION_SCREEN_MONITOR -> OnboardingStep.PERMISSION_SCREEN_OVERLAY
                OnboardingStep.PERMISSION_SCREEN_OVERLAY -> OnboardingStep.PERMISSION_SCREEN_OVERLAY
            }
        _currentStep.value = next
    }

    fun previousStep() {
        val prev =
            when (_currentStep.value) {
                OnboardingStep.WELCOME -> OnboardingStep.WELCOME
                OnboardingStep.HELP -> OnboardingStep.WELCOME
                OnboardingStep.PERMISSION_SCREEN_MONITOR -> OnboardingStep.HELP
                OnboardingStep.PERMISSION_SCREEN_OVERLAY -> OnboardingStep.PERMISSION_SCREEN_MONITOR
            }
        _currentStep.value = prev
    }
}
