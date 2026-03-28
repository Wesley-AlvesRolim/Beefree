package com.wesley.beefree.domain.onboarding.scoring

import com.wesley.beefree.domain.onboarding.RiskLevel
import com.wesley.beefree.domain.onboarding.ScaleResult
import com.wesley.beefree.domain.onboarding.ports.ScaleScorer

class Ppcs6Scorer : ScaleScorer {
    override fun score(answers: List<Int>): ScaleResult {
        val raw = answers.sum()
        val level =
            when (raw) {
                in 6..19 -> RiskLevel.LOW
                in 20..42 -> RiskLevel.HIGH
                else -> error("PPCS-6 score must be between 6 and 42, got $raw")
            }
        return ScaleResult(raw = raw, level = level)
    }
}
