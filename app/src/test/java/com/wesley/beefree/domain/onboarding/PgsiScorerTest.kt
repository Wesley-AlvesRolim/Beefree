package com.wesley.beefree.domain.onboarding

import com.wesley.beefree.domain.onboarding.scoring.PgsiScorer
import org.junit.Assert.assertEquals
import org.junit.Test

class PgsiScorerTest {
    private val scorer = PgsiScorer()

    @Test
    fun `score of 0 is LOW`() {
        val result = scorer.score(listOf(0, 0, 0, 0, 0, 0, 0, 0, 0))
        assertEquals(0, result.raw)
        assertEquals(RiskLevel.LOW, result.level)
    }

    @Test
    fun `score of 2 is LOW boundary`() {
        val result = scorer.score(listOf(1, 1, 0, 0, 0, 0, 0, 0, 0))
        assertEquals(2, result.raw)
        assertEquals(RiskLevel.LOW, result.level)
    }

    @Test
    fun `score of 3 is MODERATE boundary`() {
        val result = scorer.score(listOf(1, 1, 1, 0, 0, 0, 0, 0, 0))
        assertEquals(3, result.raw)
        assertEquals(RiskLevel.MODERATE, result.level)
    }

    @Test
    fun `score of 7 is MODERATE boundary`() {
        val result = scorer.score(listOf(1, 1, 1, 1, 1, 1, 1, 0, 0))
        assertEquals(7, result.raw)
        assertEquals(RiskLevel.MODERATE, result.level)
    }

    @Test
    fun `score of 8 is HIGH boundary`() {
        val result = scorer.score(listOf(1, 1, 1, 1, 1, 1, 1, 1, 0))
        assertEquals(8, result.raw)
        assertEquals(RiskLevel.HIGH, result.level)
    }

    @Test
    fun `score of 19 is HIGH boundary`() {
        val result = scorer.score(listOf(3, 3, 3, 3, 3, 2, 1, 1, 0))
        assertEquals(19, result.raw)
        assertEquals(RiskLevel.HIGH, result.level)
    }

    @Test
    fun `score of 20 is VERY_HIGH boundary`() {
        val result = scorer.score(listOf(3, 3, 3, 3, 3, 2, 1, 1, 1))
        assertEquals(20, result.raw)
        assertEquals(RiskLevel.VERY_HIGH, result.level)
    }

    @Test
    fun `score of 27 is VERY_HIGH max`() {
        val result = scorer.score(listOf(3, 3, 3, 3, 3, 3, 3, 3, 3))
        assertEquals(27, result.raw)
        assertEquals(RiskLevel.VERY_HIGH, result.level)
    }
}
