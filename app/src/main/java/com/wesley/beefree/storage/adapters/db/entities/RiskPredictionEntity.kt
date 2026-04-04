package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RiskPrediction")
data class RiskPredictionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "risk_score") val riskScore: Int,
    @ColumnInfo(name = "risk_factors") val riskFactors: String?,
    @ColumnInfo(name = "notification_sent") val notificationSent: Boolean,
    @ColumnInfo(name = "predicted_at") val predictedAt: Long,
)
