package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NotificationLog")
data class NotificationLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "notification_type") val notificationType: String,
    @ColumnInfo(name = "content_ref_id") val contentRefId: Int?,
    @ColumnInfo(name = "sent_at") val sentAt: Long,
    @ColumnInfo(name = "opened_at") val openedAt: Long?,
)
