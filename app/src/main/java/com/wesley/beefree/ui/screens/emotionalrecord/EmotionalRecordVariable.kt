package com.wesley.beefree.ui.screens.emotionalrecord

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.FeelingType

data class EmotionalRecordVariable(
    val emotion: FeelingType,
    val color: Color,
    val icon: ImageVector,
    @StringRes val labelRes: Int,
    @StringRes val hintRes: Int,
    @StringRes val lowLabelRes: Int,
    @StringRes val highLabelRes: Int,
)

@Composable
fun rememberEmotionalRecordVariables(): List<EmotionalRecordVariable> {
    val colorScheme = MaterialTheme.colorScheme
    return FeelingType.entries.map { emotion ->
        EmotionalRecordVariable(
            emotion = emotion,
            color =
                when (emotion) {
                    FeelingType.SLEEP -> colorScheme.primary
                    FeelingType.CRAVING -> colorScheme.tertiary
                    FeelingType.BOREDOM -> colorScheme.secondary
                    FeelingType.STRESS -> colorScheme.error
                    FeelingType.LONELINESS -> colorScheme.onErrorContainer
                    FeelingType.FATIGUE -> colorScheme.onSurfaceVariant
                },
            icon =
                when (emotion) {
                    FeelingType.SLEEP -> Icons.Default.Bedtime
                    FeelingType.CRAVING -> Icons.Default.Whatshot
                    FeelingType.BOREDOM -> Icons.Default.AccessTime
                    FeelingType.STRESS -> Icons.Default.Psychology
                    FeelingType.LONELINESS -> Icons.Default.FavoriteBorder
                    FeelingType.FATIGUE -> Icons.Default.BatteryAlert
                },
            labelRes =
                when (emotion) {
                    FeelingType.SLEEP -> R.string.emotional_record_var_sleep
                    FeelingType.CRAVING -> R.string.emotional_record_var_craving
                    FeelingType.BOREDOM -> R.string.emotional_record_var_boredom
                    FeelingType.STRESS -> R.string.emotional_record_var_stress
                    FeelingType.LONELINESS -> R.string.emotional_record_var_loneliness
                    FeelingType.FATIGUE -> R.string.emotional_record_var_fatigue
                },
            hintRes =
                when (emotion) {
                    FeelingType.SLEEP -> R.string.emotional_record_sleep_hint
                    FeelingType.CRAVING -> R.string.emotional_record_craving_hint
                    FeelingType.BOREDOM -> R.string.emotional_record_boredom_hint
                    FeelingType.STRESS -> R.string.emotional_record_stress_hint
                    FeelingType.LONELINESS -> R.string.emotional_record_loneliness_hint
                    FeelingType.FATIGUE -> R.string.emotional_record_fatigue_hint
                },
            lowLabelRes =
                when (emotion) {
                    FeelingType.SLEEP -> R.string.emotional_record_sleep_low
                    FeelingType.CRAVING -> R.string.emotional_record_craving_low
                    FeelingType.BOREDOM -> R.string.emotional_record_boredom_low
                    FeelingType.STRESS -> R.string.emotional_record_stress_low
                    FeelingType.LONELINESS -> R.string.emotional_record_loneliness_low
                    FeelingType.FATIGUE -> R.string.emotional_record_fatigue_low
                },
            highLabelRes =
                when (emotion) {
                    FeelingType.SLEEP -> R.string.emotional_record_sleep_high
                    FeelingType.CRAVING -> R.string.emotional_record_craving_high
                    FeelingType.BOREDOM -> R.string.emotional_record_boredom_high
                    FeelingType.STRESS -> R.string.emotional_record_stress_high
                    FeelingType.LONELINESS -> R.string.emotional_record_loneliness_high
                    FeelingType.FATIGUE -> R.string.emotional_record_fatigue_high
                },
        )
    }
}
