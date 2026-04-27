package com.wesley.beefree.infrastructure.storage.adapters.db

import com.wesley.beefree.domain.entities.DailyCheckInAnswer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

object DailyCheckInCodec {
    private val json = Json { ignoreUnknownKeys = true }
    private val serializer = MapSerializer(String.serializer(), DailyCheckInAnswer.serializer())

    fun encode(answers: Map<String, DailyCheckInAnswer>): String = json.encodeToString(serializer, answers)

    fun decode(raw: String): Map<String, DailyCheckInAnswer> {
        if (raw.isBlank()) return emptyMap()
        return json.decodeFromString(serializer, raw)
    }
}
