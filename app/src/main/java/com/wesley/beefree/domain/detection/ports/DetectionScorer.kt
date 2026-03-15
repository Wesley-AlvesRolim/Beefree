package com.wesley.beefree.domain.detection.ports

import com.wesley.beefree.domain.events.InterventionTriggered

interface DetectionScorer {
    fun addMatch(
        reason: String,
        keyword: String,
        addictionTypeId: Int,
        appPackage: String?,
    ): Boolean

    fun isTriggered(): Boolean

    fun getIntervention(): InterventionTriggered?

    fun reset()
}
