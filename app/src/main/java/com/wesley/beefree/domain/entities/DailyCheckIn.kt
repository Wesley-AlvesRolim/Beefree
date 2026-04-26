package com.wesley.beefree.domain.entities

import com.wesley.beefree.domain.checkin.DailyCheckInAnswer
import com.wesley.beefree.domain.onboarding.TreatmentProfile

data class DailyCheckIn(
    val id: Int? = null,
    val userProfileId: Int,
    val treatmentProfile: TreatmentProfile,
    val answers: Map<String, DailyCheckInAnswer>,
    val checkedInAt: Long,
)
