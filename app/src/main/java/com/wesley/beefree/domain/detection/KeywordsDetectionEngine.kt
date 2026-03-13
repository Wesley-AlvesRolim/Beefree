package com.wesley.beefree.domain.detection

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.detection.ports.DetectionEngine
import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.domain.events.ScreenContentCaptured

class KeywordsDetectionEngine(
    override val eventBus: EventBus,
    private val keywordsByAddictionType: Map<Int, List<String>>,
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
        for (text in event.texts) {
            if (text.isBlank()) {
                continue
            }
            for ((addictionTypeId, regexList) in regexByAddictionType) {
                for ((keyword, regex) in regexList) {
                    if (regex.containsMatchIn(text)) {
                        eventBus.publish(
                            InterventionTriggered(
                                reason = text,
                                keyword = keyword,
                                addictionTypeId = addictionTypeId,
                                appPackage = event.packageName,
                            ),
                        )
                        return
                    }
                }
            }
        }
    }
}
