package com.wesley.beefree.infrastructure.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "RelapseRecord",
    foreignKeys = [
        ForeignKey(
            entity = AddictionCategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("addiction_category_id"),
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = AppUsageSessionEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("app_usage_session_id"),
            onDelete = ForeignKey.NO_ACTION,
        ),
    ],
    indices = [
        Index(value = ["addiction_category_id"]),
        Index(value = ["app_usage_session_id"]),
    ],
)
data class RelapseRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "addiction_category_id") val addictionCategoryId: Int,
    @ColumnInfo(name = "keyword_detected") val keywordDetected: String? = null,
    @ColumnInfo(name = "detected_text") val detectedText: String? = null,
    @ColumnInfo(name = "app_usage_session_id") val appUsageSessionId: Int? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
