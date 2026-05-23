package com.wesley.beefree.ui.viewmodel.ports

import com.wesley.beefree.domain.onboarding.ClinicalProfile
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.OnboardingStep
import com.wesley.beefree.domain.onboarding.ScaleResult
import kotlinx.coroutines.flow.StateFlow

interface OnboardingViewModelPort {
    val currentStep: StateFlow<OnboardingStep>
    val answers: StateFlow<OnboardingAnswers>
    val scaleResult: StateFlow<ScaleResult?>
    val clinicalProfile: StateFlow<ClinicalProfile?>

    fun updateAnswer(update: OnboardingAnswers.() -> OnboardingAnswers)

    fun next()

    fun previous()

    fun finishOnboarding(
        onFinish: () -> Unit,
        onError: (Throwable) -> Unit,
    )
}
