package com.wesley.beefree.domain.onboarding.scoring

import com.wesley.beefree.domain.onboarding.RiskLevel
import com.wesley.beefree.domain.onboarding.ScaleResult
import com.wesley.beefree.domain.onboarding.ports.ScaleScorer

class PgsiScorer : ScaleScorer {
    override fun score(answers: List<Int>): ScaleResult {
        val raw = answers.sum()
        val level =
            when {
                raw <= 2 -> RiskLevel.LOW
                raw <= 7 -> RiskLevel.MODERATE
                raw <= 19 -> RiskLevel.HIGH
                else -> RiskLevel.VERY_HIGH
            }
        return ScaleResult(raw = raw, level = level)
    }
}
