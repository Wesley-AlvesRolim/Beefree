package com.wesley.beefree.domain.detection

import com.wesley.beefree.domain.detection.ports.DetectionEngine
import com.wesley.beefree.domain.detection.ports.DetectionScorer
import com.wesley.beefree.domain.events.ScreenContentCaptured
import com.wesley.beefree.domain.events.ports.EventBus

class KeywordsDetectionEngine(
    override val eventBus: EventBus,
    keywords: Map<Int, List<String>>,
    private val scorer: DetectionScorer = SimpleDetectionScorer(),
) : DetectionEngine<ScreenContentCaptured> {
    @Volatile
    private var regexByCategory: Map<Int, List<Pair<String, Regex>>> =
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
            .forEach { scorer.addMatch(it.text, it.keyword, it.categoryId, event.packageName) }
    }

    private fun findMatchesInText(text: String): List<MatchResult> =
        regexByCategory.flatMap { (categoryId, regexes) ->
            regexes
                .filter { (_, regex) -> regex.containsMatchIn(text) }
                .map { (keyword, _) -> MatchResult(text, keyword, categoryId) }
        }

    fun updateKeywords(keywords: Map<Int, List<String>>) {
        regexByCategory = buildRegexMap(keywords)
    }

    private fun buildRegexMap(map: Map<Int, List<String>>): Map<Int, List<Pair<String, Regex>>> =
        map.mapValues { (_, keywords) ->
            keywords.map { keyword -> keyword to Regex(keyword, RegexOption.IGNORE_CASE) }
        }

    private data class MatchResult(
        val text: String,
        val keyword: String,
        val categoryId: Int,
    )
}
