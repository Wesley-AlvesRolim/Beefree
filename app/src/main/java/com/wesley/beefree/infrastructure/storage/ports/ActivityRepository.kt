package com.wesley.beefree.infrastructure.storage.ports

import com.wesley.beefree.domain.entities.DailyMicroActivityLog
import com.wesley.beefree.domain.entities.MicroActivity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    suspend fun insertMicroActivity(activity: MicroActivity): Long

    suspend fun deleteMicroActivity(activity: MicroActivity)

    fun getAllMicroActivities(): Flow<List<MicroActivity>>

    fun getMicroActivitiesByAddictionType(addictionTypeId: Int): Flow<List<MicroActivity>>

    suspend fun insertDailyLog(log: DailyMicroActivityLog): Long

    fun getDailyLogs(userId: Int): Flow<List<DailyMicroActivityLog>>

    fun getDailyLogsByDay(
        userId: Int,
        dayDate: Long,
    ): Flow<List<DailyMicroActivityLog>>
}
