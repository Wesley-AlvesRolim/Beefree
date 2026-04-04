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
    fun factory(requiresOverlayPermission: Boolean = true): OnboardingNode {
        val permissionSteps =
            buildList {
                add(OnboardingStep("request_permissions", StepType.REQUEST_PERMISSIONS))
                add(OnboardingStep("request_permission_monitor", StepType.REQUEST_PERMISSION_MONITOR))
                if (requiresOverlayPermission) {
                    add(OnboardingStep("request_permission_overlay", StepType.REQUEST_PERMISSION_OVERLAY))
                }
                add(OnboardingStep("finish", StepType.FINISH))
            }

        return OnboardingSequence(
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
            ) + permissionSteps,
        )
    }

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

    private fun requiresCoreValues(answers: OnboardingAnswers): Boolean =
        ComputeClinicalProfileUseCase().computeIncongruenceLevel(answers) != IncongruenceLevel.BAIXA
}
