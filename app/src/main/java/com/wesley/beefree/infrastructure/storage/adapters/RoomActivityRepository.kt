package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.DailyMicroActivityLog
import com.wesley.beefree.domain.entities.MicroActivity
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.DailyMicroActivityLogDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.MicroActivityDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import com.wesley.beefree.infrastructure.storage.ports.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomActivityRepository(
    private val microActivityDao: MicroActivityDAO,
    private val dailyMicroActivityLogDao: DailyMicroActivityLogDAO,
) : ActivityRepository {
    override suspend fun insertMicroActivity(activity: MicroActivity): Long = microActivityDao.insert(activity.toEntity())

    override suspend fun deleteMicroActivity(activity: MicroActivity) {
        microActivityDao.delete(activity.toEntity())
    }

    override fun getAllMicroActivities(): Flow<List<MicroActivity>> = microActivityDao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getMicroActivitiesByAddictionType(addictionTypeId: Int): Flow<List<MicroActivity>> =
        microActivityDao.getAllByAddictionType(addictionTypeId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertDailyLog(log: DailyMicroActivityLog): Long = dailyMicroActivityLogDao.insert(log.toEntity())

    override fun getDailyLogs(userId: Int): Flow<List<DailyMicroActivityLog>> =
        dailyMicroActivityLogDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override fun getDailyLogsByDay(
        userId: Int,
        dayDate: Long,
    ): Flow<List<DailyMicroActivityLog>> =
        dailyMicroActivityLogDao.getAllByUserAndDay(userId, dayDate).map { list -> list.map { it.toDomain() } }
}
