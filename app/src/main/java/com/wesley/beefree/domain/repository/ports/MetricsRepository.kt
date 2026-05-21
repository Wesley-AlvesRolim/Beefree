package com.wesley.beefree.domain.repository.ports

import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import kotlinx.coroutines.flow.Flow

interface MetricsRepository {
    suspend fun insertEmotionRecord(record: EmotionRecord): Long

    fun getEmotionRecords(userId: Int): Flow<List<EmotionRecord>>

    fun getEmotionRecordsByType(
        userId: Int,
        feelingType: FeelingType,
    ): Flow<List<EmotionRecord>>

    suspend fun getLatestEmotionRecord(userId: Int): EmotionRecord?

    suspend fun insertRiskFeatureSnapshot(snapshot: RiskFeatureSnapshot): Long

    fun getRiskFeatureSnapshots(userId: Int): Flow<List<RiskFeatureSnapshot>>

    suspend fun getLatestRiskFeatureSnapshot(userId: Int): RiskFeatureSnapshot?

    suspend fun insertRiskAssessment(assessment: RiskAssessment): Long

    fun getRiskAssessments(userId: Int): Flow<List<RiskAssessment>>
}
