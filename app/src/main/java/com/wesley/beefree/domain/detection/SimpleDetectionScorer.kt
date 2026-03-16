package com.wesley.beefree.domain.detection

import com.wesley.beefree.domain.detection.ports.DetectionScorer
import com.wesley.beefree.domain.entities.AddictionTypeEnum
import com.wesley.beefree.domain.events.InterventionTriggered

class SimpleDetectionScorer : DetectionScorer {
    private var totalPoints = 0
    private var lastIntervention: InterventionTriggered? = null
    private val matchedKeywords = mutableSetOf<String>()

    private val threshold = 9

    override fun addMatch(
        reason: String,
        keyword: String,
        addictionTypeId: Int,
        appPackage: String?,
    ): Boolean {
        if (matchedKeywords.contains(keyword.lowercase())) {
            return isTriggered()
        }

        matchedKeywords.add(keyword.lowercase())

        val points =
            when (addictionTypeId) {
                AddictionTypeEnum.ADULT_CONTENT.ordinal,
                AddictionTypeEnum.BETS.ordinal,
                -> 3

                else -> 1
            }

        totalPoints += points

        if (lastIntervention == null || isTriggered()) {
            lastIntervention =
                InterventionTriggered(
                    reason = reason,
                    keyword = keyword,
                    addictionTypeId = addictionTypeId,
                    appPackage = appPackage,
                )
        }

        return isTriggered()
    }

    override fun isTriggered(): Boolean = totalPoints >= threshold

    override fun getIntervention(): InterventionTriggered? = if (isTriggered()) lastIntervention else null

    override fun reset() {
        totalPoints = 0
        lastIntervention = null
        matchedKeywords.clear()
    }
}
