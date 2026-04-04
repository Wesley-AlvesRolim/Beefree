package com.wesley.beefree.storage.ports

import com.wesley.beefree.domain.entities.HolisticMetrics
import com.wesley.beefree.domain.entities.NotificationLog
import com.wesley.beefree.domain.entities.RiskPrediction
import kotlinx.coroutines.flow.Flow

interface MetricsRepository {
    suspend fun insertHolisticMetrics(metrics: HolisticMetrics): Long

    fun getHolisticMetrics(userId: Int): Flow<List<HolisticMetrics>>

    suspend fun insertRiskPrediction(prediction: RiskPrediction): Long

    suspend fun updateRiskPrediction(prediction: RiskPrediction)

    fun getRiskPredictions(userId: Int): Flow<List<RiskPrediction>>

    suspend fun insertNotificationLog(log: NotificationLog): Long

    suspend fun updateNotificationLog(log: NotificationLog)

    fun getNotificationLogs(userId: Int): Flow<List<NotificationLog>>
}
