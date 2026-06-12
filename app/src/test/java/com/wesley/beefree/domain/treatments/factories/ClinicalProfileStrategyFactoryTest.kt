package com.wesley.beefree.domain.treatments.factories

import com.wesley.beefree.domain.intervention.EmiTool
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.treatments.strategies.ActProfileStrategy
import com.wesley.beefree.domain.treatments.strategies.HybridProfileStrategy
import com.wesley.beefree.domain.treatments.strategies.PreventionProfileStrategy
import com.wesley.beefree.domain.treatments.strategies.TccProfileStrategy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ClinicalProfileStrategyFactoryTest {
    @Test
    fun `ACT profile returns ActProfileStrategy`() {
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.ACT)
        assertTrue(strategy is ActProfileStrategy)
    }

    @Test
    fun `ACT profile enables weekly check-in and core values in EMI`() {
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.ACT)
        assertTrue(strategy.showsWeeklyCheckIn)
        assertTrue(strategy.showsCoreValuesInEmi)
        assertEquals(EmiTool.URGE_SURFING, strategy.emiTool)
    }

    @Test
    fun `TCC profile returns TccProfileStrategy`() {
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.TCC)
        assertTrue(strategy is TccProfileStrategy)
    }

    @Test
    fun `TCC profile disables weekly check-in and core values in EMI`() {
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.TCC)
        assertFalse(strategy.showsWeeklyCheckIn)
        assertFalse(strategy.showsCoreValuesInEmi)
        assertEquals(EmiTool.THOUGHT_RECORD, strategy.emiTool)
    }

    @Test
    fun `HYBRID profile returns HybridProfileStrategy`() {
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.HYBRID)
        assertTrue(strategy is HybridProfileStrategy)
    }

    @Test
    fun `HYBRID profile enables weekly check-in and core values with both EMI tools`() {
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.HYBRID)
        assertTrue(strategy.showsWeeklyCheckIn)
        assertTrue(strategy.showsCoreValuesInEmi)
        assertEquals(EmiTool.BOTH, strategy.emiTool)
    }

    @Test
    fun `PREVENTION profile returns PreventionProfileStrategy`() {
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.PREVENTION)
        assertTrue(strategy is PreventionProfileStrategy)
    }

    @Test
    fun `PREVENTION profile disables weekly check-in and core values`() {
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.PREVENTION)
        assertFalse(strategy.showsWeeklyCheckIn)
        assertFalse(strategy.showsCoreValuesInEmi)
        assertEquals(EmiTool.THOUGHT_RECORD, strategy.emiTool)
    }
}
