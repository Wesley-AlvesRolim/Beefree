package com.wesley.beefree.infrastructure.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "InterventionValueLink",
    primaryKeys = ["intervention_id", "user_core_value_id"],
    foreignKeys = [
        ForeignKey(
            entity = InterventionRecordEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("intervention_id"),
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = UserCoreValueEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_core_value_id"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["intervention_id"]),
        Index(value = ["user_core_value_id"]),
    ],
)
data class InterventionValueLinkEntity(
    @ColumnInfo(name = "intervention_id") val interventionId: Int,
    @ColumnInfo(name = "user_core_value_id") val userCoreValueId: Int,
)
