package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DailyMicroActivityLog")
data class DailyMicroActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "activity_id") val activityId: Int,
    @ColumnInfo(name = "day_date") val dayDate: Long,
    @ColumnInfo(name = "completed_at") val completedAt: Long,
)
