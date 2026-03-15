package com.wesley.beefree.domain.detection

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.detection.ports.DetectionEngine
import com.wesley.beefree.domain.detection.ports.DetectionScorer
import com.wesley.beefree.domain.events.ScreenContentCaptured

class KeywordsDetectionEngine(
    override val eventBus: EventBus,
    private val keywordsByAddictionType: Map<Int, List<String>>,
    private val scorer: DetectionScorer = SimpleDetectionScorer(),
) : DetectionEngine<ScreenContentCaptured> {
    private val regexByAddictionType: Map<Int, List<Pair<String, Regex>>> =
        keywordsByAddictionType.mapValues { (_, keywords) ->
            keywords.map { keyword -> keyword to Regex(keyword, RegexOption.IGNORE_CASE) }
        }

    init {
        eventBus.subscribe(ScreenContentCaptured::class.java) { event ->
            detect(event)
        }
    }

    override fun detect(event: ScreenContentCaptured) {
        try {
            findFirstMatchingScore(event)
                ?.let { scorer.getIntervention() }
                ?.let {
                    eventBus.publish(it)
                }
        } finally {
            scorer.reset()
        }
    }

    private fun findFirstMatchingScore(event: ScreenContentCaptured): MatchResult? =
        event.texts
            .asSequence()
            .filter { it.isNotBlank() }
            .flatMap { text -> findMatchesInText(text) }
            .firstOrNull { match ->
                scorer.addMatch(match.text, match.keyword, match.typeId, event.packageName)
            }

    private fun findMatchesInText(text: String): Sequence<MatchResult> =
        regexByAddictionType.asSequence().flatMap { (typeId, regexes) ->
            regexes
                .asSequence()
                .filter { (_, regex) -> regex.containsMatchIn(text) }
                .map { (keyword, _) -> MatchResult(text, keyword, typeId) }
        }

    private data class MatchResult(
        val text: String,
        val keyword: String,
        val typeId: Int,
    )
}
