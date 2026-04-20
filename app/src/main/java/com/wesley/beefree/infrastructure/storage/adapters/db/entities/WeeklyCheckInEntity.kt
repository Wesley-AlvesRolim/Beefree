package com.wesley.beefree.infrastructure.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "WeeklyCheckIn",
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
data class WeeklyCheckInEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "week_start_date") val weekStartDate: Long,
    @ColumnInfo(name = "values_alignment_text") val valuesAlignmentText: String?,
    @ColumnInfo(name = "real_connection_energy") val realConnectionEnergy: Int?,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
