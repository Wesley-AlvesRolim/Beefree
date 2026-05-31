package com.wesley.beefree.domain.repository.ports

import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import kotlinx.coroutines.flow.Flow

interface RiskFeatureSnapshotRepository {
    suspend fun save(snapshot: RiskFeatureSnapshot): Long

    fun getAllByUser(userId: Int): Flow<List<RiskFeatureSnapshot>>

    suspend fun getLatestByUser(userId: Int): RiskFeatureSnapshot?
}
