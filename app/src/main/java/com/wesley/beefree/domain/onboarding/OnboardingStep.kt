package com.wesley.beefree.domain.onboarding

data class OnboardingStep(
    val id: String,
    val type: StepType,
) : OnboardingNode
