package com.wesley.beefree.domain.onboarding.engine

import com.wesley.beefree.domain.onboarding.AddictionProfile
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.StepType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingFlowEngineTest {
    private fun engine() = CompositeOnboardingFlowEngine(OnboardingFlowFactory.factory())

    private fun answersWithProfile(profile: AddictionProfile) = OnboardingAnswers(addictionProfile = profile)

    private fun advanceToGender(engine: CompositeOnboardingFlowEngine) {
        repeat(3) { engine.next(OnboardingAnswers()) }
    }

    @Test
    fun `starts at WELCOME`() {
        assertEquals(StepType.WELCOME, engine().currentStep.type)
    }

    @Test
    fun `isFirst is true at WELCOME`() {
        val engine = engine()
        assertEquals(StepType.WELCOME, engine.currentStep.type)
        assertTrue(engine.isFirst)
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
        assertEquals(StepType.GENDER, engine.currentStep.type)
    }

    @Test
    fun `enters PPU flow after ASK_NAME`() {
        val engine = engine()
        advanceToGender(engine)
        engine.next(answersWithProfile(AddictionProfile.PPU))

        assertEquals(StepType.PPCS6_FORM, engine.currentStep.type)
    }

    @Test
    fun `PPU path contains PPCS6_FORM and EMA_FORM`() {
        val engine = engine()
        val answers = answersWithProfile(AddictionProfile.PPU)
        advanceToGender(engine)
        engine.next(answers)
        assertEquals(StepType.PPCS6_FORM, engine.currentStep.type)

        engine.next(answers)
        assertEquals(StepType.EMA_FORM, engine.currentStep.type)
    }

    @Test
    fun `post-branch steps appear after branch flow`() {
        val engine = engine()
        val answers = answersWithProfile(AddictionProfile.PPU)
        advanceToGender(engine)
        engine.next(answers)
        repeat(7) { engine.next(answers) } // PPCS6, EMA, FrequencyForm, Symptoms, Neuro, Hobbies, Goals

        assertEquals(StepType.SCORE_RESULT, engine.currentStep.type)
    }

    @Test
    fun `ends at FINISH and isLast is true`() {
        val engine = engine()
        val answers = answersWithProfile(AddictionProfile.PPU)
        advanceToGender(engine)
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
    fun `previous goes back from PPCS6_FORM to GENDER`() {
        val engine = engine()
        advanceToGender(engine)

        engine.next(answersWithProfile(AddictionProfile.PPU))
        assertEquals(StepType.PPCS6_FORM, engine.currentStep.type)

        engine.previous()
        assertEquals(StepType.GENDER, engine.currentStep.type)
    }
}
