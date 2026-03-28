package com.wesley.beefree.domain.onboarding

object OnboardingFlowFactory {
    fun factory(): OnboardingNode =
        OnboardingSequence(
            listOf(
                OnboardingStep("welcome", StepType.WELCOME),
                OnboardingStep("presentation", StepType.PRESENTATION),
                OnboardingStep("ask_name", StepType.ASK_NAME),
                OnboardingStep("addiction_selector", StepType.ADDICTION_SELECTOR),
                OnboardingBranch { answers ->
                    when (answers.addictionProfile) {
                        AddictionProfile.PPU -> ppuFlow()
                        AddictionProfile.GAMBLING -> gamblingFlow()
                        null -> error("No addiction profile selected before branch resolution")
                    }
                },
                OnboardingStep("score_result", StepType.SCORE_RESULT),
                OnboardingBranch { answers ->
                    if (answers.addictionProfile == AddictionProfile.PPU && requiresCoreValues(answers)) {
                        OnboardingStep("core_values", StepType.CORE_VALUES)
                    } else {
                        OnboardingSequence(emptyList())
                    }
                },
                OnboardingStep("request_permissions", StepType.REQUEST_PERMISSIONS),
                OnboardingStep("request_permission_monitor", StepType.REQUEST_PERMISSION_MONITOR),
                OnboardingStep("request_permission_overlay", StepType.REQUEST_PERMISSION_OVERLAY),
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

    private fun gamblingFlow(): OnboardingNode =
        OnboardingSequence(
            listOf(
                OnboardingStep("gender", StepType.GENDER),
                OnboardingStep("pgsi_form", StepType.PGSI_FORM),
                OnboardingStep("symptoms", StepType.SYMPTOMS),
                OnboardingStep("neurodivergence", StepType.NEURODIVERGENCE),
                OnboardingStep("hobbies", StepType.HOBBIES),
                OnboardingStep("goals", StepType.GOALS),
            ),
        )

    private fun requiresCoreValues(answers: OnboardingAnswers): Boolean {
        val frequency = answers.frequencyAnswer
        val moralDisapproval = (answers.emaAnswers.firstOrNull() ?: 0) + 1
        val score = frequency * moralDisapproval
        val percentage = score.toFloat() / 35f
        return percentage >= 0.30f
    }
}
