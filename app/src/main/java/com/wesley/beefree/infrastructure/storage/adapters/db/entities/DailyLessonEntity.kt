package com.wesley.beefree.infrastructure.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DailyLessons")
data class DailyLessonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content_body") val contentBody: String,
    @ColumnInfo(name = "target_profile") val targetProfile: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
