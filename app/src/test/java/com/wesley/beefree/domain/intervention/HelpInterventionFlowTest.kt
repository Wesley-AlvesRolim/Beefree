package com.wesley.beefree.domain.intervention

import com.wesley.beefree.domain.intervention.strategies.ActHelpInterventionFlow
import com.wesley.beefree.domain.intervention.strategies.CommonHelpInterventionFlow
import com.wesley.beefree.domain.intervention.strategies.HybridHelpInterventionFlow
import com.wesley.beefree.domain.intervention.strategies.TccHelpInterventionFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HelpInterventionFlowTest {
    @Test
    fun actFlowBuildsCorrectStepSequence() {
        val flow = createActFlow()
        val allSteps = flow.allSteps

        assertTrue("Flow should start with common steps", allSteps[0] is HelpInterventionStep.IntensityStep)
        assertTrue("Flow should have ACT values step", allSteps.any { it is HelpInterventionStep.ActValuesStep })
        assertTrue("Flow should have ACT direction step", allSteps.any { it is HelpInterventionStep.ActDirectionStep })
        assertTrue("Flow should have ACT action step", allSteps.any { it is HelpInterventionStep.ActCommittedActionStep })
        assertTrue("Flow should end with timer and reflection", allSteps.last() is HelpInterventionStep.ReflectionStep)
    }

    @Test
    fun tccFlowBuildsCorrectStepSequence() {
        val flow = createTccFlow()
        val allSteps = flow.allSteps

        assertTrue("Flow should start with common steps", allSteps[0] is HelpInterventionStep.IntensityStep)
        assertTrue("Flow should have TCC auto thought step", allSteps.any { it is HelpInterventionStep.TccAutomaticThoughtStep })
        assertTrue("Flow should have TCC evidence for step", allSteps.any { it is HelpInterventionStep.TccEvidenceForStep })
        assertTrue("Flow should have TCC evidence against step", allSteps.any { it is HelpInterventionStep.TccEvidenceAgainstStep })
        assertTrue("Flow should have TCC alternative step", allSteps.any { it is HelpInterventionStep.TccAlternativeThoughtStep })
        assertTrue("Flow should end with timer and reflection", allSteps.last() is HelpInterventionStep.ReflectionStep)
    }

    @Test
    fun hybridFlowBuildsCorrectStepSequence() {
        val flow = createHybridFlow()
        val allSteps = flow.allSteps

        assertTrue("Flow should start with common steps", allSteps[0] is HelpInterventionStep.IntensityStep)
        assertTrue("Flow should have ACT values step", allSteps.any { it is HelpInterventionStep.ActValuesStep })
        assertTrue("Flow should have TCC auto thought step", allSteps.any { it is HelpInterventionStep.TccAutomaticThoughtStep })
        assertTrue("Flow should end with timer and reflection", allSteps.last() is HelpInterventionStep.ReflectionStep)
    }

    @Test
    fun actFlowHasCorrectStepCount() {
        val flow = createActFlow()
        val totalSteps = flow.allSteps.size
        assertTrue("ACT flow should have at least 9 steps", totalSteps >= 9)
        assertTrue("ACT flow should have less than 15 steps", totalSteps < 15)
    }

    @Test
    fun tccFlowHasCorrectStepCount() {
        val flow = createTccFlow()
        val totalSteps = flow.allSteps.size
        assertTrue("TCC flow should have at least 10 steps", totalSteps >= 10)
        assertTrue("TCC flow should have less than 16 steps", totalSteps < 16)
    }

    @Test
    fun hybridFlowHasCorrectStepCount() {
        val flow = createHybridFlow()
        val totalSteps = flow.allSteps.size
        assertTrue("Hybrid flow should have at least 9 steps", totalSteps >= 9)
        assertTrue("Hybrid flow should have less than 15 steps", totalSteps < 15)
    }

    @Test
    fun commonStepsContainRequiredSteps() {
        val commonSteps = CommonHelpInterventionFlow.commonSteps
        assertTrue("Should have intensity step", commonSteps.any { it is HelpInterventionStep.IntensityStep })
        assertTrue("Should have body location step", commonSteps.any { it is HelpInterventionStep.BodyLocationStep })
        assertTrue("Should have urge surfing step", commonSteps.any { it is HelpInterventionStep.UrgeSurfingStep })
        assertTrue("Should have post-surf intensity step", commonSteps.any { it is HelpInterventionStep.PostSurfIntensityStep })
    }

    @Test
    fun finalStepsContainTimerAndReflection() {
        val finalSteps = CommonHelpInterventionFlow.finalSteps
        assertTrue("Should have timer step", finalSteps.any { it is HelpInterventionStep.TimerStep })
        assertTrue("Should have reflection step", finalSteps.any { it is HelpInterventionStep.ReflectionStep })
        assertEquals("Should have exactly 2 final steps", 2, finalSteps.size)
    }

    @Test
    fun postSurfIntensityStepLoopsBackWhenAnswerAtThreshold() {
        val postSurfStep =
            HelpInterventionStep.PostSurfIntensityStep(
                titleKey = "test",
                loopThreshold = 7,
            )
        assertTrue("Should trigger loop when answer = 7", 7 >= postSurfStep.loopThreshold)
    }

    @Test
    fun postSurfIntensityStepLoopsBackWhenAnswerAboveThreshold() {
        val postSurfStep =
            HelpInterventionStep.PostSurfIntensityStep(
                titleKey = "test",
                loopThreshold = 7,
            )
        assertTrue("Should trigger loop when answer = 8", 8 >= postSurfStep.loopThreshold)
    }

    @Test
    fun postSurfIntensityStepDoesNotLoopWhenAnswerBelowThreshold() {
        val postSurfStep =
            HelpInterventionStep.PostSurfIntensityStep(
                titleKey = "test",
                loopThreshold = 7,
            )
        assertFalse("Should not trigger loop when answer = 6", 6 >= postSurfStep.loopThreshold)
    }

    @Test
    fun urgeSurfingStepHasMeditationSteps() {
        val step =
            HelpInterventionStep.UrgeSurfingStep(
                meditationStepKeys = CommonHelpInterventionFlow.MEDITATION_STEP_KEYS,
            )
        assertTrue("Should have meditation steps", step.meditationStepKeys.isNotEmpty())
        assertEquals("Should have 12 meditation steps", 12, step.meditationStepKeys.size)
    }

    private fun createActFlow(): HelpInterventionFlow =
        HelpInterventionFlow(
            commonSteps = CommonHelpInterventionFlow.commonSteps,
            clinicalSteps = ActHelpInterventionFlow.steps,
            finalSteps = CommonHelpInterventionFlow.finalSteps,
        )

    private fun createTccFlow(): HelpInterventionFlow =
        HelpInterventionFlow(
            commonSteps = CommonHelpInterventionFlow.commonSteps,
            clinicalSteps = TccHelpInterventionFlow.steps,
            finalSteps = CommonHelpInterventionFlow.finalSteps,
        )

    private fun createHybridFlow(): HelpInterventionFlow =
        HelpInterventionFlow(
            commonSteps = CommonHelpInterventionFlow.commonSteps,
            clinicalSteps = HybridHelpInterventionFlow.steps,
            finalSteps = CommonHelpInterventionFlow.finalSteps,
        )
}
