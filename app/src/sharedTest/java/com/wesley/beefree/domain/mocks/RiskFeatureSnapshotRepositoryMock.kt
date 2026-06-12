package com.wesley.beefree.domain.mocks

import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.repository.ports.RiskFeatureSnapshotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RiskFeatureSnapshotRepositoryMock(
    var throwOnSave: Throwable? = null,
) : RiskFeatureSnapshotRepository {
    var allByUser: List<RiskFeatureSnapshot> = emptyList()
    var latestByUser: RiskFeatureSnapshot? = null

    var savedSnapshot: RiskFeatureSnapshot? = null
        private set
    var saveCalls: Int = 0
        private set

    val saveArgs = mutableListOf<RiskFeatureSnapshot>()

    override suspend fun save(snapshot: RiskFeatureSnapshot): Long {
        saveCalls++
        throwOnSave?.let { throw it }
        savedSnapshot = snapshot
        saveArgs += snapshot
        return saveArgs.size.toLong()
    }

    override fun getAllByUser(userId: Int): Flow<List<RiskFeatureSnapshot>> = flowOf(allByUser)

    override suspend fun getLatestByUser(userId: Int): RiskFeatureSnapshot? = latestByUser
}
