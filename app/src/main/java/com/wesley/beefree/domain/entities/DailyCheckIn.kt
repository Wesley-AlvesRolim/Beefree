package com.wesley.beefree.domain.entities

import com.wesley.beefree.domain.onboarding.TreatmentProfile
import kotlinx.serialization.Serializable

data class DailyCheckIn(
    val id: Int? = null,
    val userProfileId: Int,
    val treatmentProfile: TreatmentProfile,
    val answers: Map<String, DailyCheckInAnswer>,
    val checkedInAt: Long,
)

@Serializable
sealed interface DailyCheckInAnswer {
    @Serializable
    data class Scale(
        val value: Int,
    ) : DailyCheckInAnswer

    @Serializable
    data class DualScale(
        val first: Int,
        val second: Int,
    ) : DailyCheckInAnswer

    @Serializable
    data class MultiSelect(
        val ids: List<String>,
        val context: String? = null,
    ) : DailyCheckInAnswer

    @Serializable
    data class SingleSelect(
        val id: String,
    ) : DailyCheckInAnswer

    @Serializable
    data class Text(
        val value: String,
    ) : DailyCheckInAnswer

    @Serializable
    data class Bool(
        val value: Boolean,
    ) : DailyCheckInAnswer
}
