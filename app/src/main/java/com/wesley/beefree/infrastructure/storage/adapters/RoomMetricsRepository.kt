package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.EmotionRecordDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.RiskAssessmentDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.RiskFeatureSnapshotDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomMetricsRepository(
    private val emotionRecordDao: EmotionRecordDAO,
    private val riskFeatureSnapshotDao: RiskFeatureSnapshotDAO,
    private val riskAssessmentDao: RiskAssessmentDAO,
) : MetricsRepository {
    override suspend fun insertEmotionRecord(record: EmotionRecord): Long = emotionRecordDao.insert(record.toEntity())

    override suspend fun deleteEmotionRecordsByIds(ids: List<Long>) = emotionRecordDao.deleteByIds(ids)

    override fun getEmotionRecords(userId: Int): Flow<List<EmotionRecord>> =
        emotionRecordDao.getAllByUser(userId).map { list -> list.mapNotNull { it.toDomain() } }

    override fun getEmotionRecordsByType(
        userId: Int,
        feelingType: FeelingType,
    ): Flow<List<EmotionRecord>> =
        emotionRecordDao.getByUserAndType(userId, feelingType.name).map { list ->
            list.mapNotNull { it.toDomain() }
        }

    override suspend fun getLatestEmotionRecord(userId: Int): EmotionRecord? = emotionRecordDao.getLatestByUser(userId)?.toDomain()

    override suspend fun insertRiskFeatureSnapshot(snapshot: RiskFeatureSnapshot): Long = riskFeatureSnapshotDao.insert(snapshot.toEntity())

    override fun getRiskFeatureSnapshots(userId: Int): Flow<List<RiskFeatureSnapshot>> =
        riskFeatureSnapshotDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun getLatestRiskFeatureSnapshot(userId: Int): RiskFeatureSnapshot? =
        riskFeatureSnapshotDao.getLatestByUser(userId)?.toDomain()

    override suspend fun insertRiskAssessment(assessment: RiskAssessment): Long = riskAssessmentDao.insert(assessment.toEntity())

    override suspend fun deleteAllRiskAssessmentsForUser(userId: Int) = riskAssessmentDao.deleteAllByUser(userId)

    override fun getRiskAssessments(userId: Int): Flow<List<RiskAssessment>> =
        riskAssessmentDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }
}
