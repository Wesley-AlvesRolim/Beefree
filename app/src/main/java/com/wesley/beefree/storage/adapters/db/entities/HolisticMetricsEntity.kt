package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HolisticMetrics")
data class HolisticMetricsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "anxiety_level") val anxietyLevel: Int?,
    @ColumnInfo(name = "emotional_satisfaction") val emotionalSatisfaction: Int?,
    @ColumnInfo(name = "mood") val mood: String?,
    @ColumnInfo(name = "logged_at") val loggedAt: Long,
)
