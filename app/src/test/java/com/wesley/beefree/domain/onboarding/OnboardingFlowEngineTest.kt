package com.wesley.beefree.domain.onboarding

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingFlowEngineTest {
    private fun engine() = CompositeOnboardingFlowEngine(OnboardingFlowFactory.factory())

    private fun answersWithProfile(profile: AddictionProfile) = OnboardingAnswers(addictionProfile = profile)

    private fun advanceToAddictionSelector(engine: CompositeOnboardingFlowEngine) {
        repeat(3) { engine.next(OnboardingAnswers()) }
    }

    @Test
    fun `starts at WELCOME`() {
        assertEquals(StepType.WELCOME, engine().currentStep.type)
    }

    @Test
    fun `isFirst is true at WELCOME`() {
        assertEquals(StepType.WELCOME, engine().currentStep.type)
        assertTrue(engine().isFirst)
    }

    @Test
    fun `previous does nothing at first step`() {
        val engine = engine()
        engine.previous()
        assertEquals(StepType.WELCOME, engine.currentStep.type)
    }

    @Test
    fun `navigates linearly through pre-branch steps`() {
        val engine = engine()
        val emptyAnswers = OnboardingAnswers()

        engine.next(emptyAnswers)
        assertEquals(StepType.PRESENTATION, engine.currentStep.type)

        engine.next(emptyAnswers)
        assertEquals(StepType.ASK_NAME, engine.currentStep.type)

        engine.next(emptyAnswers)
        assertEquals(StepType.ADDICTION_SELECTOR, engine.currentStep.type)
    }

    @Test
    fun `resolves PPU branch after addiction selector`() {
        val engine = engine()
        advanceToAddictionSelector(engine)
        engine.next(answersWithProfile(AddictionProfile.PPU))

        assertEquals(StepType.GENDER, engine.currentStep.type)
    }

    @Test
    fun `resolves GAMBLING branch after addiction selector`() {
        val engine = engine()
        advanceToAddictionSelector(engine)
        engine.next(answersWithProfile(AddictionProfile.GAMBLING))

        assertEquals(StepType.GENDER, engine.currentStep.type)
    }

    @Test
    fun `PPU path contains PPCS6_FORM and EMA_FORM`() {
        val engine = engine()
        val answers = answersWithProfile(AddictionProfile.PPU)
        advanceToAddictionSelector(engine)
        engine.next(answers) // GENDER
        engine.next(answers) // PPCS6_FORM
        assertEquals(StepType.PPCS6_FORM, engine.currentStep.type)

        engine.next(answers) // EMA_FORM
        assertEquals(StepType.EMA_FORM, engine.currentStep.type)
    }

    @Test
    fun `GAMBLING path contains PGSI_FORM not PPCS6_FORM`() {
        val engine = engine()
        val answers = answersWithProfile(AddictionProfile.GAMBLING)
        advanceToAddictionSelector(engine)
        engine.next(answers) // GENDER
        engine.next(answers) // PGSI_FORM

        assertEquals(StepType.PGSI_FORM, engine.currentStep.type)
    }

    @Test
    fun `post-branch steps appear after branch flow`() {
        val engine = engine()
        val answers = answersWithProfile(AddictionProfile.PPU)
        advanceToAddictionSelector(engine)
        engine.next(answers)
        repeat(8) { engine.next(answers) } // Gender, PPCS6, EMA, FrequencyForm, Symptoms, Neuro, Hobbies, Goals

        assertEquals(StepType.SCORE_RESULT, engine.currentStep.type)
    }

    @Test
    fun `ends at FINISH and isLast is true`() {
        val engine = engine()
        val answers = answersWithProfile(AddictionProfile.PPU)
        advanceToAddictionSelector(engine)
        engine.next(answers)
        repeat(12) { engine.next(answers) }

        assertEquals(StepType.FINISH, engine.currentStep.type)
        assertTrue(engine.isLast)
    }

    @Test
    fun `previous goes back one step`() {
        val engine = engine()
        engine.next(OnboardingAnswers())
        assertEquals(StepType.PRESENTATION, engine.currentStep.type)

        engine.previous()
        assertEquals(StepType.WELCOME, engine.currentStep.type)
    }

    @Test
    fun `isFirst becomes false after first next`() {
        val engine = engine()
        engine.next(OnboardingAnswers())
        assertFalse(engine.isFirst)
    }

    @Test
    fun `re-resolves branch if going back and changing answer`() {
        val engine = engine()
        advanceToAddictionSelector(engine)

        // Resolve to PPU
        engine.next(answersWithProfile(AddictionProfile.PPU))
        assertEquals(StepType.GENDER, engine.currentStep.type)
        engine.next(OnboardingAnswers()) // PPCS6_FORM
        assertEquals(StepType.PPCS6_FORM, engine.currentStep.type)

        // Go back to addiction selector
        engine.previous() // GENDER
        engine.previous() // ADDICTION_SELECTOR

        // Resolve to GAMBLING
        engine.next(answersWithProfile(AddictionProfile.GAMBLING))
        assertEquals(StepType.GENDER, engine.currentStep.type)
        engine.next(OnboardingAnswers()) // PGSI_FORM
        assertEquals(StepType.PGSI_FORM, engine.currentStep.type)
    }
}
