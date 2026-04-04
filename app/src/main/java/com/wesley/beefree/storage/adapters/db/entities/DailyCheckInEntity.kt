package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DailyCheckIn")
data class DailyCheckInEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "dopamine_level") val dopamineLevel: Int,
    @ColumnInfo(name = "mood") val mood: String,
    @ColumnInfo(name = "anxiety_level") val anxietyLevel: Int?,
    @ColumnInfo(name = "checked_in_at") val checkedInAt: Long,
)
