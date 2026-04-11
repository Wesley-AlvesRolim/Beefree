package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wesley.beefree.storage.adapters.db.entities.UserProfileEntity

@Entity(
    tableName = "TriggerMapping",
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
data class TriggerMappingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "app_package") val appPackage: String?,
    @ColumnInfo(name = "trigger_context") val triggerContext: String,
    @ColumnInfo(name = "craving_intensity") val cravingIntensity: Int,
    @ColumnInfo(name = "did_relapse") val didRelapse: Boolean,
    @ColumnInfo(name = "logged_at") val loggedAt: Long,
)
