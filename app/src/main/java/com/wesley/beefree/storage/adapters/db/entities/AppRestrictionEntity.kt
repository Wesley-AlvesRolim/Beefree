package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AppRestriction")
data class AppRestrictionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "addiction_type_id") val addictionTypeId: Int,
    @ColumnInfo(name = "app_package") val appPackage: String,
    @ColumnInfo(name = "screen_time_limit_millis") val screenTimeLimitMillis: Long,
)
