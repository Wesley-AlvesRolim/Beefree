package com.wesley.beefree.infrastructure.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "DailyCheckIn",
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
data class DailyCheckInEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "treatment_profile") val treatmentProfile: String,
    @ColumnInfo(name = "answers_json") val answersJson: String,
    @ColumnInfo(name = "checked_in_at") val checkedInAt: Long,
)
