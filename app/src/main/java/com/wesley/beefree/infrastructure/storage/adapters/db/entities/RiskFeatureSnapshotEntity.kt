package com.wesley.beefree.infrastructure.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "RiskFeatureSnapshot",
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
data class RiskFeatureSnapshotEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "previous_snapshot_id") val previousSnapshotId: Int? = null,
    val humor: Int? = null,
    val stress: Int? = null,
    val anxiety: Int? = null,
    val urge: Int? = null,
    @ColumnInfo(name = "hours_since_last_relapse") val hoursSinceLastRelapse: Int? = null,
    @ColumnInfo(name = "hour_of_day") val hourOfDay: Int? = null,
    @ColumnInfo(name = "day_of_week") val dayOfWeek: Int? = null,
    @ColumnInfo(name = "time_since_last_app_open") val timeSinceLastAppOpen: Int? = null,
    @ColumnInfo(name = "missing_checkins") val missingCheckins: Int? = null,
    @ColumnInfo(name = "recent_intense_usage") val recentIntenseUsage: Int? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
