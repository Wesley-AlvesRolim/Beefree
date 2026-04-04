package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserHobbies")
data class UserHobbyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "hobby_name") val hobbyName: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
