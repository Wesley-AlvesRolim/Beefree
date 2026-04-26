package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.HolisticMetrics
import com.wesley.beefree.domain.entities.NotificationLog
import com.wesley.beefree.domain.entities.RiskPrediction
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.HolisticMetricsDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.NotificationLogDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.RiskPredictionDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import com.wesley.beefree.infrastructure.storage.ports.MetricsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomMetricsRepository(
    private val holisticMetricsDao: HolisticMetricsDAO,
    private val riskPredictionDao: RiskPredictionDAO,
    private val notificationLogDao: NotificationLogDAO,
) : MetricsRepository {
    override suspend fun insertHolisticMetrics(metrics: HolisticMetrics): Long = holisticMetricsDao.insert(metrics.toEntity())

    override fun getHolisticMetrics(userId: Int): Flow<List<HolisticMetrics>> =
        holisticMetricsDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertRiskPrediction(prediction: RiskPrediction): Long = riskPredictionDao.insert(prediction.toEntity())

    override suspend fun updateRiskPrediction(prediction: RiskPrediction) {
        riskPredictionDao.update(prediction.toEntity())
    }

    override fun getRiskPredictions(userId: Int): Flow<List<RiskPrediction>> =
        riskPredictionDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertNotificationLog(log: NotificationLog): Long = notificationLogDao.insert(log.toEntity())

    override suspend fun updateNotificationLog(log: NotificationLog) {
        notificationLogDao.update(log.toEntity())
    }

    override fun getNotificationLogs(userId: Int): Flow<List<NotificationLog>> =
        notificationLogDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }
}
