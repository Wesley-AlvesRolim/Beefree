package com.wesley.beefree.domain.repository.ports

import com.wesley.beefree.domain.entities.AppUsageSession
import kotlinx.coroutines.flow.Flow

interface AppUsageRepository {
    suspend fun insertAppUsageSession(session: AppUsageSession): Long

    suspend fun updateAppUsageSession(session: AppUsageSession)

    suspend fun getLastSessionByPackage(packageName: String): AppUsageSession?

    fun getAppUsageHistory(): Flow<List<AppUsageSession>>
}
