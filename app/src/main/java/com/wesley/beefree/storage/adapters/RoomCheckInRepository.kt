package com.wesley.beefree.storage.adapters

import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.WeeklyCheckIn
import com.wesley.beefree.storage.adapters.db.dao.DailyCheckInDAO
import com.wesley.beefree.storage.adapters.db.dao.WeeklyCheckInDAO
import com.wesley.beefree.storage.adapters.db.toDomain
import com.wesley.beefree.storage.adapters.db.toEntity
import com.wesley.beefree.storage.ports.CheckInRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomCheckInRepository(
    private val dailyCheckInDao: DailyCheckInDAO,
    private val weeklyCheckInDao: WeeklyCheckInDAO,
) : CheckInRepository {
    override suspend fun insertDailyCheckIn(checkIn: DailyCheckIn): Long = dailyCheckInDao.insert(checkIn.toEntity())

    override fun getDailyCheckIns(userId: Int): Flow<List<DailyCheckIn>> =
        dailyCheckInDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertWeeklyCheckIn(checkIn: WeeklyCheckIn): Long = weeklyCheckInDao.insert(checkIn.toEntity())

    override fun getWeeklyCheckIns(userId: Int): Flow<List<WeeklyCheckIn>> =
        weeklyCheckInDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }
}
