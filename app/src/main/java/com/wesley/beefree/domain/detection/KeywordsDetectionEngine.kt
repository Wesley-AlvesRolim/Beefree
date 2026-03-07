package com.wesley.beefree.domain.detection

import com.wesley.beefree.domain.bus.ports.EventBus
import com.wesley.beefree.domain.detection.ports.DetectionEngine
import com.wesley.beefree.domain.events.InterventionTriggered
import com.wesley.beefree.domain.events.ScreenContentCaptured

class KeywordsDetectionEngine(
    override val eventBus: EventBus,
    private val blockedKeywords: List<String>,
) : DetectionEngine<ScreenContentCaptured> {
    private val blockedRegexList =
        blockedKeywords.map { keyword ->
            Regex(keyword, RegexOption.IGNORE_CASE)
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
            for (regex in blockedRegexList) {
                if (regex.containsMatchIn(text)) {
                    eventBus.publish(InterventionTriggered(text))
                    return
                }
            }
        }
    }
}
