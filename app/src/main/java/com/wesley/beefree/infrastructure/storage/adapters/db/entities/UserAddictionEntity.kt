package com.wesley.beefree.infrastructure.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "UserAddiction",
    primaryKeys = ["user_profile_id", "addiction_category_id"],
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_profile_id"),
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = AddictionCategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("addiction_category_id"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["user_profile_id"]),
        Index(value = ["addiction_category_id"]),
    ],
)
data class UserAddictionEntity(
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "addiction_category_id") val addictionCategoryId: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
