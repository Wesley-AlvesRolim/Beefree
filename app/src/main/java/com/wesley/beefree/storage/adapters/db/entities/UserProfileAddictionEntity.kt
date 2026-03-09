package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "UserProfileAddiction",
    primaryKeys = ["user_profile_id", "addiction_type_id"],
)
data class UserProfileAddictionEntity(
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "addiction_type_id") val addictionTypeId: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
