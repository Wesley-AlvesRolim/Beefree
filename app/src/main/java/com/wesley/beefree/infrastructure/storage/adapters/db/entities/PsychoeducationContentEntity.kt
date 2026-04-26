package com.wesley.beefree.infrastructure.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "PsychoeducationContent",
    foreignKeys = [
        ForeignKey(
            entity = AddictionCategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("addiction_category_id"),
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index(value = ["addiction_category_id"])],
)
data class PsychoeducationContentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "addiction_category_id") val addictionCategoryId: Int? = null,
    @ColumnInfo(name = "content_text") val contentText: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true,
)
