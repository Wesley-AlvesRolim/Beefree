package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.AppUsageSession
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.AppUsageSessionDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import com.wesley.beefree.infrastructure.storage.ports.AppUsageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomAppUsageRepository(
    private val appUsageSessionDao: AppUsageSessionDAO,
) : AppUsageRepository {
    override suspend fun insertAppUsageSession(session: AppUsageSession): Long = appUsageSessionDao.insert(session.toEntity())

    override suspend fun updateAppUsageSession(session: AppUsageSession) {
        appUsageSessionDao.update(session.toEntity())
    }

    override suspend fun getLastSessionByPackage(packageName: String): AppUsageSession? =
        appUsageSessionDao.getLastSessionByPackage(packageName)?.toDomain()

    override fun getAppUsageHistory(): Flow<List<AppUsageSession>> = appUsageSessionDao.getAll().map { list -> list.map { it.toDomain() } }
}
