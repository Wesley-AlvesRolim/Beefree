package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "MotivationalMessages",
    foreignKeys = [
        ForeignKey(
            entity = AddictionTypeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("addiction_type_id"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["addiction_type_id"])],
)
data class MotivationalMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "addiction_type_id") val addictionTypeId: Int? = null,
    @ColumnInfo(name = "message_text") val messageText: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true,
)
