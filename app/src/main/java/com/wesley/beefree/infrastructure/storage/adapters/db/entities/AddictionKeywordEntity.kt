package com.wesley.beefree.infrastructure.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "AddictionKeywords",
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
data class AddictionKeywordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "addiction_type_id") val addictionTypeId: Int,
    val keyword: String,
)
