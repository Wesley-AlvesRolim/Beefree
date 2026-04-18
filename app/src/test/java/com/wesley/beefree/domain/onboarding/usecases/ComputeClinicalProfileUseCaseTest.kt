package com.wesley.beefree.domain.onboarding.usecases

import com.wesley.beefree.domain.onboarding.AddictionProfile
import com.wesley.beefree.domain.onboarding.IncongruenceLevel
import com.wesley.beefree.domain.onboarding.NeurodivergenceAnswer
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
        neurodivergence: NeurodivergenceAnswer = NeurodivergenceAnswer.NO,
    ): OnboardingAnswers {
        val ppcs6PerQuestion = ppcs6Sum / 6
        return OnboardingAnswers(
            addictionProfile = AddictionProfile.PPU,
            ppcs6Answers = List(6) { ppcs6PerQuestion },
            emaAnswers = listOf(emaIndex),
            frequencyAnswer = frequency,
            neurodivergenceAnswer = neurodivergence,
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
    fun `ALTA incongruence with high PPCS-6 returns HYBRID`() {
        val answers = ppuAnswers(ppcs6Sum = 42, emaIndex = 6, frequency = 5)
        val result = useCase.execute(answers)!!
        assertEquals(IncongruenceLevel.ALTA, result.incongruenceLevel)
        assertEquals(TreatmentProfile.HYBRID, result.treatmentProfile)
    }

    @Test
    fun `MEDIA incongruence with high PPCS-6 returns HYBRID`() {
        val answers = ppuAnswers(ppcs6Sum = 24, emaIndex = 3, frequency = 3)
        val result = useCase.execute(answers)!!
        assertEquals(IncongruenceLevel.MEDIA, result.incongruenceLevel)
        assertEquals(TreatmentProfile.HYBRID, result.treatmentProfile)
    }

    @Test
    fun `MEDIA incongruence with low PPCS-6 returns HYBRID`() {
        val answers = ppuAnswers(ppcs6Sum = 6, emaIndex = 3, frequency = 3)
        val result = useCase.execute(answers)!!
        assertEquals(IncongruenceLevel.MEDIA, result.incongruenceLevel)
        assertEquals(TreatmentProfile.HYBRID, result.treatmentProfile)
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
    fun `GAMBLING high PGSI returns HYBRID`() {
        val answers =
            OnboardingAnswers(
                addictionProfile = AddictionProfile.GAMBLING,
                pgsiAnswers = List(9) { 1 },
            )
        val result = useCase.execute(answers)!!
        assertEquals(TreatmentProfile.HYBRID, result.treatmentProfile)
    }

    @Test
    fun `ALTA with low PPCS-6 and neurodivergence returns HYBRID`() {
        val answers = ppuAnswers(ppcs6Sum = 6, emaIndex = 6, frequency = 5, neurodivergence = NeurodivergenceAnswer.YES)
        val result = useCase.execute(answers)!!
        assertEquals(TreatmentProfile.HYBRID, result.treatmentProfile)
    }

    @Test
    fun `ALTA with high PPCS-6 and neurodivergence still returns HYBRID`() {
        val answers = ppuAnswers(ppcs6Sum = 42, emaIndex = 6, frequency = 5, neurodivergence = NeurodivergenceAnswer.YES)
        val result = useCase.execute(answers)!!
        assertEquals(TreatmentProfile.HYBRID, result.treatmentProfile)
    }

    @Test
    fun `MEDIA with low PPCS-6 and neurodivergence returns HYBRID`() {
        val answers = ppuAnswers(ppcs6Sum = 6, emaIndex = 3, frequency = 3, neurodivergence = NeurodivergenceAnswer.YES)
        val result = useCase.execute(answers)!!
        assertEquals(TreatmentProfile.HYBRID, result.treatmentProfile)
    }

    @Test
    fun `MEDIA with high PPCS-6 and neurodivergence returns HYBRID`() {
        val answers = ppuAnswers(ppcs6Sum = 24, emaIndex = 3, frequency = 3, neurodivergence = NeurodivergenceAnswer.YES)
        val result = useCase.execute(answers)!!
        assertEquals(TreatmentProfile.HYBRID, result.treatmentProfile)
    }

    @Test
    fun `BAIXA with neurodivergence does not change treatment`() {
        val answers = ppuAnswers(ppcs6Sum = 42, emaIndex = 0, frequency = 1, neurodivergence = NeurodivergenceAnswer.YES)
        val result = useCase.execute(answers)!!
        assertEquals(TreatmentProfile.TCC, result.treatmentProfile)
    }

    @Test
    fun `BAIXA low PPCS-6 with neurodivergence does not change treatment`() {
        val answers = ppuAnswers(ppcs6Sum = 6, emaIndex = 0, frequency = 1, neurodivergence = NeurodivergenceAnswer.YES)
        val result = useCase.execute(answers)!!
        assertEquals(TreatmentProfile.PREVENTION, result.treatmentProfile)
    }

    @Test
    fun `neurodivergence NO does not alter ALTA low PPCS-6`() {
        val answers = ppuAnswers(ppcs6Sum = 6, emaIndex = 6, frequency = 5, neurodivergence = NeurodivergenceAnswer.NO)
        val result = useCase.execute(answers)!!
        assertEquals(TreatmentProfile.ACT, result.treatmentProfile)
    }

    @Test
    fun `neurodivergence PREFER_NOT_SAY does not alter treatment`() {
        val answers = ppuAnswers(ppcs6Sum = 6, emaIndex = 6, frequency = 5, neurodivergence = NeurodivergenceAnswer.PREFER_NOT_SAY)
        val result = useCase.execute(answers)!!
        assertEquals(TreatmentProfile.ACT, result.treatmentProfile)
    }
}
