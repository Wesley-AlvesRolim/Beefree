package com.wesley.beefree.domain.entities

import androidx.annotation.StringRes
import com.wesley.beefree.R

enum class BreathingPhaseEnum(
    val durationSeconds: Int,
    @StringRes val labelRes: Int,
) {
    INHALE(4, R.string.urge_surfing_phase_inhale),
    HOLD(4, R.string.urge_surfing_phase_hold),
    EXHALE(6, R.string.urge_surfing_phase_exhale),
}
