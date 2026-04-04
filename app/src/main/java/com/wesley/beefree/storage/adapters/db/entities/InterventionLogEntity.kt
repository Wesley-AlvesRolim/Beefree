package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InterventionLogs")
data class InterventionLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "intervention_type") val interventionType: String,
    @ColumnInfo(name = "triggered_by") val triggeredBy: String,
    @ColumnInfo(name = "was_completed") val wasCompleted: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
