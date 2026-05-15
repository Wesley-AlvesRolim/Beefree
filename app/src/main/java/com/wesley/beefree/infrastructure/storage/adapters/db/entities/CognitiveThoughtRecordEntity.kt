package com.wesley.beefree.infrastructure.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "CognitiveThoughtRecord",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_profile_id"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["user_profile_id"])],
)
data class CognitiveThoughtRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "situation") val situation: String = "",
    @ColumnInfo(name = "automatic_thought") val automaticThought: String,
    @ColumnInfo(name = "feeling") val feeling: String = "",
    @ColumnInfo(name = "consequence") val consequence: String = "",
    @ColumnInfo(name = "alternative_thought") val alternativeThought: String = "",
    @ColumnInfo(name = "cognitive_distortions") val cognitiveDistortions: String = "[]",
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
