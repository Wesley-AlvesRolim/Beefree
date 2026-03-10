package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RelapseHistory")
data class RelapseHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "addiction_type_id") val addictionTypeId: Int,
    @ColumnInfo(name = "keyword_detected") val keywordDetected: String,
    @ColumnInfo(name = "detected_text") val detectedText: String? = null,
    @ColumnInfo(name = "app_package") val appPackage: String? = null,
    @ColumnInfo(name = "external_app") val externalApp: String? = null,
    @ColumnInfo(name = "was_sent_to_external") val wasSentToExternal: Boolean = false,
    @ColumnInfo(name = "relapse_at") val relapseAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
)
