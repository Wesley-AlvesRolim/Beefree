package com.wesley.beefree.domain.onboarding

data class OnboardingSequence(
    val children: List<OnboardingNode>,
) : OnboardingNode
