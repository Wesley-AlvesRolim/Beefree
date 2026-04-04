package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UrgeSurfingSession")
data class UrgeSurfingSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "intervention_id") val interventionId: Int?,
    @ColumnInfo(name = "peak_intensity") val peakIntensity: Int?,
    @ColumnInfo(name = "duration_seconds") val durationSeconds: Int?,
    @ColumnInfo(name = "completed") val completed: Boolean,
    @ColumnInfo(name = "logged_at") val loggedAt: Long,
)
