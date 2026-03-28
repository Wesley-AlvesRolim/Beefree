package com.wesley.beefree.domain.onboarding.usecases

import com.wesley.beefree.domain.onboarding.AddictionProfile
import com.wesley.beefree.domain.onboarding.IncongruenceLevel
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ComputeClinicalProfileUseCaseTest {
    private val useCase = ComputeClinicalProfileUseCase()

    private fun ppuAnswers(
        ppcs6Sum: Int = 6,
        emaIndex: Int = 0,
        frequency: Int = 1,
    ): OnboardingAnswers {
        val ppcs6PerQuestion = ppcs6Sum / 6
        return OnboardingAnswers(
            addictionProfile = AddictionProfile.PPU,
            ppcs6Answers = List(6) { ppcs6PerQuestion },
            emaAnswers = listOf(emaIndex),
            frequencyAnswer = frequency,
        )
    }

    @Test
    fun `returns null when no addiction profile`() {
        val result = useCase.execute(OnboardingAnswers(addictionProfile = null))
        assertNull(result)
    }

    @Test
    fun `ALTA incongruence with low PPCS-6 returns ACT`() {
        val answers = ppuAnswers(ppcs6Sum = 6, emaIndex = 6, frequency = 5)
        val result = useCase.execute(answers)!!
        assertEquals(IncongruenceLevel.ALTA, result.incongruenceLevel)
        assertEquals(TreatmentProfile.ACT, result.treatmentProfile)
    }

    @Test
    fun `ALTA incongruence with high PPCS-6 returns ACT_AND_TCC`() {
        val answers = ppuAnswers(ppcs6Sum = 42, emaIndex = 6, frequency = 5)
        val result = useCase.execute(answers)!!
        assertEquals(IncongruenceLevel.ALTA, result.incongruenceLevel)
        assertEquals(TreatmentProfile.ACT_AND_TCC, result.treatmentProfile)
    }

    @Test
    fun `MEDIA incongruence with high PPCS-6 returns HYBRID_TCC_FOCUS`() {
        val answers = ppuAnswers(ppcs6Sum = 24, emaIndex = 3, frequency = 3)
        val result = useCase.execute(answers)!!
        assertEquals(IncongruenceLevel.MEDIA, result.incongruenceLevel)
        assertEquals(TreatmentProfile.HYBRID_TCC_FOCUS, result.treatmentProfile)
    }

    @Test
    fun `MEDIA incongruence with low PPCS-6 returns HYBRID_ACT_FOCUS`() {
        val answers = ppuAnswers(ppcs6Sum = 6, emaIndex = 3, frequency = 3)
        val result = useCase.execute(answers)!!
        assertEquals(IncongruenceLevel.MEDIA, result.incongruenceLevel)
        assertEquals(TreatmentProfile.HYBRID_ACT_FOCUS, result.treatmentProfile)
    }

    @Test
    fun `BAIXA incongruence with high PPCS-6 returns TCC`() {
        val answers = ppuAnswers(ppcs6Sum = 42, emaIndex = 0, frequency = 1)
        val result = useCase.execute(answers)!!
        assertEquals(IncongruenceLevel.BAIXA, result.incongruenceLevel)
        assertEquals(TreatmentProfile.TCC, result.treatmentProfile)
    }

    @Test
    fun `BAIXA incongruence with low PPCS-6 returns PREVENTION`() {
        val answers = ppuAnswers(ppcs6Sum = 6, emaIndex = 0, frequency = 1)
        val result = useCase.execute(answers)!!
        assertEquals(IncongruenceLevel.BAIXA, result.incongruenceLevel)
        assertEquals(TreatmentProfile.PREVENTION, result.treatmentProfile)
    }

    @Test
    fun `ALTA boundary at 70 percent (score 25 of 35)`() {
        val answers = ppuAnswers(ppcs6Sum = 6, emaIndex = 4, frequency = 5)
        val result = useCase.execute(answers)!!
        assertEquals(IncongruenceLevel.ALTA, result.incongruenceLevel)
    }

    @Test
    fun `MEDIA boundary at 30 percent (score 11 of 35)`() {
        val answers = ppuAnswers(ppcs6Sum = 6, emaIndex = 4, frequency = 2)
        val result = useCase.execute(answers)!!
        val score = 2 * (4 + 1)
        assertEquals(10, score)
        assertEquals(IncongruenceLevel.BAIXA, result.incongruenceLevel)
    }

    @Test
    fun `GAMBLING returns null incongruenceLevel`() {
        val answers =
            OnboardingAnswers(
                addictionProfile = AddictionProfile.GAMBLING,
                pgsiAnswers = List(9) { 0 },
            )
        val result = useCase.execute(answers)!!
        assertNull(result.incongruenceLevel)
    }

    @Test
    fun `GAMBLING low PGSI returns PREVENTION`() {
        val answers =
            OnboardingAnswers(
                addictionProfile = AddictionProfile.GAMBLING,
                pgsiAnswers = List(9) { 0 },
            )
        val result = useCase.execute(answers)!!
        assertEquals(TreatmentProfile.PREVENTION, result.treatmentProfile)
    }

    @Test
    fun `GAMBLING moderate PGSI returns TCC`() {
        val answers =
            OnboardingAnswers(
                addictionProfile = AddictionProfile.GAMBLING,
                pgsiAnswers = listOf(3, 0, 0, 0, 0, 0, 0, 0, 0),
            )
        val result = useCase.execute(answers)!!
        assertEquals(TreatmentProfile.TCC, result.treatmentProfile)
    }

    @Test
    fun `GAMBLING high PGSI returns ACT_AND_TCC`() {
        val answers =
            OnboardingAnswers(
                addictionProfile = AddictionProfile.GAMBLING,
                pgsiAnswers = List(9) { 1 },
            )
        val result = useCase.execute(answers)!!
        assertEquals(TreatmentProfile.ACT_AND_TCC, result.treatmentProfile)
    }
}
