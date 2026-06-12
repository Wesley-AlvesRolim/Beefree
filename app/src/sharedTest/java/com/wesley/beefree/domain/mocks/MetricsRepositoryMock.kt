package com.wesley.beefree.domain.mocks

import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MetricsRepositoryMock : MetricsRepository {
    var emotionRecords: List<EmotionRecord> = emptyList()
    var latestEmotionRecord: EmotionRecord? = null
    var riskFeatureSnapshots: List<RiskFeatureSnapshot> = emptyList()
    var latestRiskFeatureSnapshot: RiskFeatureSnapshot? = null
    var riskAssessments: List<RiskAssessment> = emptyList()

    var throwOnInsertEmotionRecord: Throwable? = null
    var throwOnInsertRiskAssessment: Throwable? = null
    var throwOnGetLatestRiskFeatureSnapshot: Throwable? = null

    var delayOnInsertEmotionRecord: Long = 0

    val insertEmotionRecordArgs = mutableListOf<EmotionRecord>()
    val insertRiskFeatureSnapshotArgs = mutableListOf<RiskFeatureSnapshot>()
    val insertRiskAssessmentArgs = mutableListOf<RiskAssessment>()
    val deleteEmotionRecordsByIdsArgs = mutableListOf<List<Long>>()
    val deleteAllRiskAssessmentsForUserArgs = mutableListOf<Int>()

    var insertEmotionRecordCount: Int = 0
        private set
    val insertRiskAssessmentCount get() = insertRiskAssessmentArgs.size
    val deleteAllRiskAssessmentsCalled get() = deleteAllRiskAssessmentsForUserArgs.isNotEmpty()

    override suspend fun insertEmotionRecord(record: EmotionRecord): Long {
        insertEmotionRecordCount++
        if (delayOnInsertEmotionRecord > 0) delay(delayOnInsertEmotionRecord)
        throwOnInsertEmotionRecord?.let { throw it }
        insertEmotionRecordArgs += record
        return insertEmotionRecordArgs.size.toLong()
    }

    override fun getEmotionRecords(userId: Int): Flow<List<EmotionRecord>> = flowOf(emotionRecords)

    override fun getEmotionRecordsByType(
        userId: Int,
        feelingType: FeelingType,
    ): Flow<List<EmotionRecord>> = flowOf(emotionRecords.filter { it.feelingType == feelingType })

    override suspend fun getLatestEmotionRecord(userId: Int): EmotionRecord? =
        latestEmotionRecord ?: emotionRecords.maxByOrNull { it.createdAt }

    override suspend fun insertRiskFeatureSnapshot(snapshot: RiskFeatureSnapshot): Long {
        insertRiskFeatureSnapshotArgs += snapshot
        return insertRiskFeatureSnapshotArgs.size.toLong()
    }

    override fun getRiskFeatureSnapshots(userId: Int): Flow<List<RiskFeatureSnapshot>> = flowOf(riskFeatureSnapshots)

    override suspend fun getLatestRiskFeatureSnapshot(userId: Int): RiskFeatureSnapshot? {
        throwOnGetLatestRiskFeatureSnapshot?.let { throw it }
        return latestRiskFeatureSnapshot
    }

    override suspend fun deleteEmotionRecordsByIds(ids: List<Long>) {
        deleteEmotionRecordsByIdsArgs += ids
    }

    override suspend fun insertRiskAssessment(assessment: RiskAssessment): Long {
        throwOnInsertRiskAssessment?.let { throw it }
        insertRiskAssessmentArgs += assessment
        return insertRiskAssessmentArgs.size.toLong()
    }

    override suspend fun deleteAllRiskAssessmentsForUser(userId: Int) {
        deleteAllRiskAssessmentsForUserArgs += userId
    }

    override fun getRiskAssessments(userId: Int): Flow<List<RiskAssessment>> = flowOf(riskAssessments)
}
