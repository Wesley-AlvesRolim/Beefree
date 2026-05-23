package com.wesley.beefree.domain.onboarding.engine

import com.wesley.beefree.domain.onboarding.AddictionProfile
import com.wesley.beefree.domain.onboarding.IncongruenceLevel
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.OnboardingBranch
import com.wesley.beefree.domain.onboarding.OnboardingNode
import com.wesley.beefree.domain.onboarding.OnboardingSequence
import com.wesley.beefree.domain.onboarding.OnboardingStep
import com.wesley.beefree.domain.onboarding.StepType
import com.wesley.beefree.domain.onboarding.usecases.ComputeClinicalProfileUseCase

object OnboardingFlowFactory {
    fun factory(): OnboardingNode =
        OnboardingSequence(
            listOf(
                OnboardingStep("welcome", StepType.WELCOME),
                OnboardingStep("ask_name", StepType.ASK_NAME),
                OnboardingBranch { ppuFlow() },
                OnboardingStep("score_result", StepType.SCORE_RESULT),
                OnboardingBranch { answers ->
                    if (answers.addictionProfile == AddictionProfile.PPU &&
                        requiresCoreValues(
                            answers,
                        )
                    ) {
                        OnboardingStep("core_values", StepType.CORE_VALUES)
                    } else {
                        OnboardingSequence(emptyList())
                    }
                },
                OnboardingStep("finish", StepType.FINISH),
            ),
        )

    private fun ppuFlow(): OnboardingNode =
        OnboardingSequence(
            listOf(
                OnboardingStep("gender", StepType.GENDER),
                OnboardingStep("ppcs6_form", StepType.PPCS6_FORM),
                OnboardingStep("ema_form", StepType.EMA_FORM),
                OnboardingStep("frequency_form", StepType.FREQUENCY_FORM),
                OnboardingStep("symptoms", StepType.SYMPTOMS),
                OnboardingStep("neurodivergence", StepType.NEURODIVERGENCE),
                OnboardingStep("hobbies", StepType.HOBBIES),
                OnboardingStep("goals", StepType.GOALS),
            ),
        )

    private fun requiresCoreValues(answers: OnboardingAnswers): Boolean {
        val moralIncongruenceScore =
            ComputeClinicalProfileUseCase().computeMoralIncongruenceScore(answers)
        return ComputeClinicalProfileUseCase().classifyIncongruenceLevel(moralIncongruenceScore) != IncongruenceLevel.BAIXA
    }
}
