package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.AppRestriction
import com.wesley.beefree.domain.entities.AppUse
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.AppRestrictionDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.AppUseDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import com.wesley.beefree.infrastructure.storage.ports.AppUsageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomAppUsageRepository(
    private val appRestrictionDao: AppRestrictionDAO,
    private val appUseDao: AppUseDAO,
) : AppUsageRepository {
    override suspend fun insertRestriction(restriction: AppRestriction): Long = appRestrictionDao.insert(restriction.toEntity())

    override suspend fun updateRestriction(restriction: AppRestriction) {
        appRestrictionDao.update(restriction.toEntity())
    }

    override suspend fun deleteRestriction(restriction: AppRestriction) {
        appRestrictionDao.delete(restriction.toEntity())
    }

    override suspend fun getRestrictionById(id: Int): AppRestriction? = appRestrictionDao.getById(id)?.toDomain()

    override suspend fun getRestrictionByPackage(packageName: String): AppRestriction? =
        appRestrictionDao.getByPackageName(packageName)?.toDomain()

    override fun getAllRestrictions(): Flow<List<AppRestriction>> = appRestrictionDao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun insertAppUse(appUse: AppUse): Long = appUseDao.insert(appUse.toEntity())

    override suspend fun updateAppUse(appUse: AppUse) {
        appUseDao.update(appUse.toEntity())
    }

    override suspend fun getLastAppUse(packageName: String): AppUse? = appUseDao.getLastUseByPackageName(packageName)?.toDomain()

    override fun getAppUseHistory(): Flow<List<AppUse>> = appUseDao.getAll().map { list -> list.map { it.toDomain() } }
}
