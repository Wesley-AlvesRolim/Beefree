package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.repository.ports.RiskFeatureSnapshotRepository
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.RiskFeatureSnapshotDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.RiskFeatureSnapshotEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRiskFeatureSnapshotRepository(
    private val dao: RiskFeatureSnapshotDAO,
) : RiskFeatureSnapshotRepository {
    override suspend fun save(snapshot: RiskFeatureSnapshot): Long =
        dao.insert(
            RiskFeatureSnapshotEntity(
                userProfileId = snapshot.userProfileId,
                previousSnapshotId = snapshot.previousSnapshotId,
                sleep = snapshot.sleep,
                craving = snapshot.craving,
                boredom = snapshot.boredom,
                stress = snapshot.stress,
                loneliness = snapshot.loneliness,
                fatigue = snapshot.fatigue,
                hoursSinceLastRelapse = snapshot.hoursSinceLastRelapse,
                hourOfDay = snapshot.hourOfDay,
                dayOfWeek = snapshot.dayOfWeek,
                timeSinceLastAppOpen = snapshot.timeSinceLastAppOpen,
                missingCheckins = snapshot.missingCheckins,
                recentIntenseUsage = snapshot.recentIntenseUsage,
                createdAt = snapshot.createdAt,
            ),
        )

    override fun getAllByUser(userId: Int): Flow<List<RiskFeatureSnapshot>> =
        dao.getAllByUser(userId).map { entities ->
            entities.map { entity ->
                RiskFeatureSnapshot(
                    id = entity.id,
                    userProfileId = entity.userProfileId,
                    previousSnapshotId = entity.previousSnapshotId,
                    sleep = entity.sleep,
                    craving = entity.craving,
                    boredom = entity.boredom,
                    stress = entity.stress,
                    loneliness = entity.loneliness,
                    fatigue = entity.fatigue,
                    hoursSinceLastRelapse = entity.hoursSinceLastRelapse,
                    hourOfDay = entity.hourOfDay,
                    dayOfWeek = entity.dayOfWeek,
                    timeSinceLastAppOpen = entity.timeSinceLastAppOpen,
                    missingCheckins = entity.missingCheckins,
                    recentIntenseUsage = entity.recentIntenseUsage,
                    createdAt = entity.createdAt,
                )
            }
        }

    override suspend fun getLatestByUser(userId: Int): RiskFeatureSnapshot? =
        dao.getLatestByUser(userId)?.let { entity ->
            RiskFeatureSnapshot(
                id = entity.id,
                userProfileId = entity.userProfileId,
                previousSnapshotId = entity.previousSnapshotId,
                sleep = entity.sleep,
                craving = entity.craving,
                boredom = entity.boredom,
                stress = entity.stress,
                loneliness = entity.loneliness,
                fatigue = entity.fatigue,
                hoursSinceLastRelapse = entity.hoursSinceLastRelapse,
                hourOfDay = entity.hourOfDay,
                dayOfWeek = entity.dayOfWeek,
                timeSinceLastAppOpen = entity.timeSinceLastAppOpen,
                missingCheckins = entity.missingCheckins,
                recentIntenseUsage = entity.recentIntenseUsage,
                createdAt = entity.createdAt,
            )
        }
}
