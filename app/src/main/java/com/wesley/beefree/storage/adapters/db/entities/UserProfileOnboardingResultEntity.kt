package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "UserProfileOnboardingResult",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_profile_id"),
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = AddictionTypeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("addiction_type_id"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["user_profile_id"]),
        Index(value = ["addiction_type_id"]),
    ],
)
data class UserProfileOnboardingResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "addiction_type_id") val addictionTypeId: Int,
    @ColumnInfo(name = "ppcs_score") val ppcsScore: Int?,
    @ColumnInfo(name = "pgsi_score") val pgsiScore: Int?,
    @ColumnInfo(name = "moral_incongruence_score") val moralIncongruenceScore: Int?,
    @ColumnInfo(name = "frequency_score") val frequencyScore: Int?,
    @ColumnInfo(name = "moral_disapproval_score") val moralDisapprovalScore: Int?,
    @ColumnInfo(name = "has_neurodivergence") val hasNeurodivergence: Boolean,
    @ColumnInfo(name = "clinical_profile") val clinicalProfile: String,
    @ColumnInfo(name = "moral_incongruence_band") val moralIncongruenceBand: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
