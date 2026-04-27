package com.wesley.beefree.domain.detection

import com.wesley.beefree.domain.detection.ports.DetectionScorer
import com.wesley.beefree.domain.entities.AddictionCategoryEnum
import com.wesley.beefree.domain.events.InterventionTriggered

class SimpleDetectionScorer : DetectionScorer {
    private var totalPoints = 0
    private var lastIntervention: InterventionTriggered? = null
    private val matchedKeywords = mutableSetOf<String>()

    private val threshold = 9

    override fun addMatch(
        reason: String,
        keyword: String,
        addictionCategoryId: Int,
        appPackage: String?,
    ): Boolean {
        if (matchedKeywords.contains(keyword.lowercase())) {
            return isTriggered()
        }

        matchedKeywords.add(keyword.lowercase())

        val categoryIndex = addictionCategoryId - 1
        val points =
            when (categoryIndex) {
                AddictionCategoryEnum.ADULT_CONTENT.ordinal,
                AddictionCategoryEnum.BETS.ordinal,
                -> 3

                else -> 1
            }

        totalPoints += points

        if (lastIntervention == null || isTriggered()) {
            lastIntervention =
                InterventionTriggered(
                    reason = reason,
                    keyword = keyword,
                    addictionCategoryId = addictionCategoryId,
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
