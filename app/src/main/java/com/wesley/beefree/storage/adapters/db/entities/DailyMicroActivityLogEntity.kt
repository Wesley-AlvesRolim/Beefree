package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "DailyMicroActivityLog",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_profile_id"),
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = MicroActivityEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("activity_id"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["user_profile_id"]),
        Index(value = ["activity_id"]),
    ],
)
data class DailyMicroActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "activity_id") val activityId: Int,
    @ColumnInfo(name = "day_date") val dayDate: Long,
    @ColumnInfo(name = "completed_at") val completedAt: Long,
)
