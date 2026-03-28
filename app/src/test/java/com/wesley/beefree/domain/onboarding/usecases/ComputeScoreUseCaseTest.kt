package com.wesley.beefree.domain.onboarding.usecases

import com.wesley.beefree.domain.onboarding.AddictionProfile
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ComputeScoreUseCaseTest {
    private val useCase = ComputeScoreUseCase()

    @Test
    fun `returns null when no addiction profile`() {
        val result = useCase.execute(OnboardingAnswers(addictionProfile = null))

        assertNull(result)
    }

    @Test
    fun `delegates to Ppcs6Scorer for PPU profile`() {
        val answers =
            OnboardingAnswers(
                addictionProfile = AddictionProfile.PPU,
                ppcs6Answers = List(6) { 1 },
            )

        val result = useCase.execute(answers)

        assertEquals(6, result?.raw)
        assertEquals(RiskLevel.LOW, result?.level)
    }

    @Test
    fun `delegates to PgsiScorer for GAMBLING profile`() {
        val answers =
            OnboardingAnswers(
                addictionProfile = AddictionProfile.GAMBLING,
                pgsiAnswers = List(9) { 0 },
            )

        val result = useCase.execute(answers)

        assertEquals(0, result?.raw)
        assertEquals(RiskLevel.LOW, result?.level)
    }
}
