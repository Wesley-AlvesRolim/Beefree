package com.wesley.beefree.infrastructure.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "UserOnboardingSession",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_profile_id"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["user_profile_id"])],
)
data class UserOnboardingSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "clinical_approach") val clinicalApproach: String,
    @ColumnInfo(name = "ppcs_score") val ppcsScore: Int?,
    @ColumnInfo(name = "pgsi_score") val pgsiScore: Int?,
    @ColumnInfo(name = "moral_incongruence_score") val moralIncongruenceScore: Int?,
    @ColumnInfo(name = "frequency_score") val frequencyScore: Int?,
    @ColumnInfo(name = "moral_disapproval_score") val moralDisapprovalScore: Int?,
    @ColumnInfo(name = "has_neurodivergence") val hasNeurodivergence: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
