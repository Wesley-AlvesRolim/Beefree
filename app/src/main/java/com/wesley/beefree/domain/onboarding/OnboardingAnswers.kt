package com.wesley.beefree.domain.onboarding

data class OnboardingAnswers(
    val userName: String = "",
    val addictionProfile: AddictionProfile? = null,
    val gender: String = "",
    val ppcs6Answers: List<Int> = emptyList(),
    val emaAnswers: List<Int> = emptyList(),
    val frequencyAnswer: Int = 0,
    val pgsiAnswers: List<Int> = emptyList(),
    val symptoms: List<String> = emptyList(),
    val neurodivergenceAnswer: NeurodivergenceAnswer = NeurodivergenceAnswer.NOT_ANSWERED,
    val hobbies: List<String> = emptyList(),
    val goals: List<String> = emptyList(),
    val coreValues: List<String> = emptyList(),
)
