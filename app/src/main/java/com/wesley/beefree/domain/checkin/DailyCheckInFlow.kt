package com.wesley.beefree.domain.checkin

data class DailyCheckInFlow(
    val trackLabelKey: String,
    val pathLabelKey: String,
    val steps: List<DailyCheckInStep>,
)
