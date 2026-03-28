package com.wesley.beefree.domain.onboarding

import com.wesley.beefree.domain.onboarding.scoring.Ppcs6Scorer

class ComputeClinicalProfileUseCase {
    fun execute(answers: OnboardingAnswers): ClinicalProfile? =
        when (answers.addictionProfile) {
            AddictionProfile.PPU -> computePpuProfile(answers)
            AddictionProfile.GAMBLING -> computeGamblingProfile(answers)
            null -> null
        }

    private fun computePpuProfile(answers: OnboardingAnswers): ClinicalProfile {
        val incongruenceLevel = computeIncongruenceLevel(answers)
        val ppcs6Level = Ppcs6Scorer().score(answers.ppcs6Answers).level
        val treatmentProfile = resolvePpuTreatment(incongruenceLevel, ppcs6Level)
        return ClinicalProfile(incongruenceLevel = incongruenceLevel, treatmentProfile = treatmentProfile)
    }

    private fun computeIncongruenceLevel(answers: OnboardingAnswers): IncongruenceLevel {
        val frequency = answers.frequencyAnswer
        val moralDisapproval = (answers.emaAnswers.firstOrNull() ?: 0) + 1
        val score = frequency * moralDisapproval
        val percentage = score.toFloat() / MAX_INCONGRUENCE_SCORE

        return when {
            percentage >= ALTA_THRESHOLD -> IncongruenceLevel.ALTA
            percentage >= MEDIA_THRESHOLD -> IncongruenceLevel.MEDIA
            else -> IncongruenceLevel.BAIXA
        }
    }

    private fun resolvePpuTreatment(
        incongruence: IncongruenceLevel,
        ppcs6Level: RiskLevel,
    ): TreatmentProfile =
        when (incongruence) {
            IncongruenceLevel.ALTA ->
                when (ppcs6Level) {
                    RiskLevel.LOW -> TreatmentProfile.ACT
                    else -> TreatmentProfile.ACT_AND_TCC
                }
            IncongruenceLevel.MEDIA ->
                when (ppcs6Level) {
                    RiskLevel.LOW -> TreatmentProfile.HYBRID_ACT_FOCUS
                    else -> TreatmentProfile.HYBRID_TCC_FOCUS
                }
            IncongruenceLevel.BAIXA ->
                when (ppcs6Level) {
                    RiskLevel.LOW -> TreatmentProfile.PREVENTION
                    else -> TreatmentProfile.TCC
                }
        }

    private fun computeGamblingProfile(answers: OnboardingAnswers): ClinicalProfile {
        val pgsiRaw = answers.pgsiAnswers.sum()
        val treatmentProfile =
            when {
                pgsiRaw <= 2 -> TreatmentProfile.PREVENTION
                pgsiRaw <= 7 -> TreatmentProfile.TCC
                else -> TreatmentProfile.ACT_AND_TCC
            }
        return ClinicalProfile(incongruenceLevel = null, treatmentProfile = treatmentProfile)
    }

    companion object {
        private const val MAX_INCONGRUENCE_SCORE = 35f
        private const val ALTA_THRESHOLD = 0.70f
        private const val MEDIA_THRESHOLD = 0.30f
    }
}
