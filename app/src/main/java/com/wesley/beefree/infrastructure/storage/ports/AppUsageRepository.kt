package com.wesley.beefree.infrastructure.storage.ports

import com.wesley.beefree.domain.entities.AppRestriction
import com.wesley.beefree.domain.entities.AppUse
import kotlinx.coroutines.flow.Flow

interface AppUsageRepository {
    suspend fun insertRestriction(restriction: AppRestriction): Long

    suspend fun updateRestriction(restriction: AppRestriction)

    suspend fun deleteRestriction(restriction: AppRestriction)

    suspend fun getRestrictionById(id: Int): AppRestriction?

    suspend fun getRestrictionByPackage(packageName: String): AppRestriction?

    fun getAllRestrictions(): Flow<List<AppRestriction>>

    suspend fun insertAppUse(appUse: AppUse): Long

    suspend fun updateAppUse(appUse: AppUse)

    suspend fun getLastAppUse(packageName: String): AppUse?

    fun getAppUseHistory(): Flow<List<AppUse>>
}
