package com.wesley.beefree.domain.detection

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.detection.ports.DetectionEngine
import com.wesley.beefree.domain.detection.ports.DetectionScorer
import com.wesley.beefree.domain.events.ScreenContentCaptured

class KeywordsDetectionEngine(
    override val eventBus: EventBus,
    keywords: Map<Int, List<String>>,
    private val scorer: DetectionScorer = SimpleDetectionScorer(),
) : DetectionEngine<ScreenContentCaptured> {
    @Volatile
    private var regexByAddictionType: Map<Int, List<Pair<String, Regex>>> =
        buildRegexMap(keywords)

    init {
        eventBus.subscribe(ScreenContentCaptured::class.java) { event ->
            detect(event)
        }
    }

    override fun detect(event: ScreenContentCaptured) {
        try {
            findAllMatches(event)
            scorer.getIntervention()?.let {
                eventBus.publish(it)
            }
        } finally {
            scorer.reset()
        }
    }

    private fun findAllMatches(event: ScreenContentCaptured) {
        event.texts
            .filter { it.isNotBlank() }
            .flatMap { findMatchesInText(it) }
            .forEach { scorer.addMatch(it.text, it.keyword, it.typeId, event.packageName) }
    }

    private fun findMatchesInText(text: String): List<MatchResult> =
        regexByAddictionType.flatMap { (typeId, regexes) ->
            regexes
                .filter { (_, regex) -> regex.containsMatchIn(text) }
                .map { (keyword, _) -> MatchResult(text, keyword, typeId) }
        }

    fun updateKeywords(keywords: Map<Int, List<String>>) {
        regexByAddictionType = buildRegexMap(keywords)
    }

    private fun buildRegexMap(map: Map<Int, List<String>>): Map<Int, List<Pair<String, Regex>>> =
        map.mapValues { (_, keywords) ->
            keywords.map { keyword -> keyword to Regex(keyword, RegexOption.IGNORE_CASE) }
        }

    private data class MatchResult(
        val text: String,
        val keyword: String,
        val typeId: Int,
    )
}
