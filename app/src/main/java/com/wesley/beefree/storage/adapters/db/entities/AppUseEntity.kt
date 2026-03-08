package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AppUse")
data class AppUseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "enter_time") val enterTime: Long,
    @ColumnInfo(name = "exit_time") val exitTime: Long? = null,
)
