package com.wesley.beefree.infrastructure.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ThoughtRecords",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_profile_id"),
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = TriggerMappingEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("trigger_id"),
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [
        Index(value = ["user_profile_id"]),
        Index(value = ["trigger_id"]),
    ],
)
data class ThoughtRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "trigger_id") val triggerId: Int?,
    @ColumnInfo(name = "automatic_thought") val automaticThought: String,
    @ColumnInfo(name = "rational_response") val rationalResponse: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
