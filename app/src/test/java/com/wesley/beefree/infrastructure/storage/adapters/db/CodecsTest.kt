package com.wesley.beefree.infrastructure.storage.adapters.db

import com.wesley.beefree.domain.checkin.DailyCheckInAnswer
import org.junit.Assert.assertEquals
import org.junit.Test

class CodecsTest {
    @Test
    fun `encode then decode round trips all answer types`() {
        val original: Map<String, DailyCheckInAnswer> =
            mapOf(
                "scale" to DailyCheckInAnswer.Scale(7),
                "dual" to DailyCheckInAnswer.DualScale(6, 8),
                "multi" to DailyCheckInAnswer.MultiSelect(listOf("a", "b"), "context note"),
                "multi_no_context" to DailyCheckInAnswer.MultiSelect(listOf("x")),
                "single" to DailyCheckInAnswer.SingleSelect("guilt"),
                "text" to DailyCheckInAnswer.Text("hello world | with separators"),
                "bool" to DailyCheckInAnswer.Bool(true),
            )

        val encoded = DailyCheckInCodec.encode(original)
        val decoded = DailyCheckInCodec.decode(encoded)

        assertEquals(original, decoded)
    }

    @Test
    fun `decode of blank string returns empty map`() {
        assertEquals(emptyMap<String, DailyCheckInAnswer>(), DailyCheckInCodec.decode(""))
    }
}
