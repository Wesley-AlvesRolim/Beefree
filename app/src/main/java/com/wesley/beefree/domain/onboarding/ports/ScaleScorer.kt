package com.wesley.beefree.domain.onboarding.ports

import com.wesley.beefree.domain.onboarding.ScaleResult

interface ScaleScorer {
    fun score(answers: List<Int>): ScaleResult
}
