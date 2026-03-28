package com.wesley.beefree.domain.onboarding.scoring

import com.wesley.beefree.domain.onboarding.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Test

class Ppcs6ScorerTest {
    private val scorer = Ppcs6Scorer()

    @Test
    fun `minimum score of 6 is LOW`() {
        val result = scorer.score(listOf(1, 1, 1, 1, 1, 1))
        assertEquals(6, result.raw)
        assertEquals(RiskLevel.LOW, result.level)
    }

    @Test
    fun `score of 19 is LOW upper boundary`() {
        val result = scorer.score(listOf(4, 4, 4, 4, 2, 1))
        assertEquals(19, result.raw)
        assertEquals(RiskLevel.LOW, result.level)
    }

    @Test
    fun `score of 20 is HIGH lower boundary`() {
        val result = scorer.score(listOf(4, 4, 4, 4, 2, 2))
        assertEquals(20, result.raw)
        assertEquals(RiskLevel.HIGH, result.level)
    }

    @Test
    fun `maximum score of 42 is HIGH`() {
        val result = scorer.score(listOf(7, 7, 7, 7, 7, 7))
        assertEquals(42, result.raw)
        assertEquals(RiskLevel.HIGH, result.level)
    }

    @Test(expected = IllegalStateException::class)
    fun `score below 6 throws`() {
        scorer.score(listOf(0, 0, 0, 0, 0, 0))
    }
}
