package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ThoughtRecords")
data class ThoughtRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "trigger_id") val triggerId: Int?,
    @ColumnInfo(name = "automatic_thought") val automaticThought: String,
    @ColumnInfo(name = "rational_response") val rationalResponse: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
