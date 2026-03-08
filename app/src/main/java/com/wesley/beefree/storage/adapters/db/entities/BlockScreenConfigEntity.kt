package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BlockScreenConfig")
data class BlockScreenConfigEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "addiction_type_id") val addictionTypeId: Int? = null,
    @ColumnInfo(name = "title_text") val titleText: String? = null,
    @ColumnInfo(name = "subtitle_text") val subtitleText: String? = null,
    @ColumnInfo(name = "image_uri") val imageUri: String? = null,
    @ColumnInfo(name = "background_color") val backgroundColor: Int = -1,
    @ColumnInfo(name = "text_color") val textColor: Int = -16777216,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
)
