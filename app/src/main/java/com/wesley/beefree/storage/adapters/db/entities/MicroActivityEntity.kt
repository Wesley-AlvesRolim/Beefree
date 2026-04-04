package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MicroActivities")
data class MicroActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "addiction_type_id") val addictionTypeId: Int?,
    @ColumnInfo(name = "activity_type") val activityType: String,
    @ColumnInfo(name = "activity_name") val activityName: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
