package com.wesley.beefree.domain.usecases.onboarding

import com.wesley.beefree.domain.onboarding.AddictionProfile
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.ScaleResult
import com.wesley.beefree.domain.onboarding.scoring.PgsiScorer
import com.wesley.beefree.domain.onboarding.scoring.Ppcs6Scorer

class ComputeScoreUseCase {
    fun execute(answers: OnboardingAnswers): ScaleResult? =
        when (answers.addictionProfile) {
            AddictionProfile.PPU -> Ppcs6Scorer().score(answers.ppcs6Answers)
            AddictionProfile.GAMBLING -> PgsiScorer().score(answers.pgsiAnswers)
            null -> null
        }
}
