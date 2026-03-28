package com.wesley.beefree.domain.onboarding

data class OnboardingBranch(
    val resolve: (OnboardingAnswers) -> OnboardingNode,
) : OnboardingNode
