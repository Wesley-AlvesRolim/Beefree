package com.wesley.beefree.domain.onboarding.ports

import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.OnboardingStep

interface OnboardingFlowEngine {
    val currentStep: OnboardingStep
    val isFirst: Boolean
    val isLast: Boolean

    fun next(answers: OnboardingAnswers)

    fun previous()
}
