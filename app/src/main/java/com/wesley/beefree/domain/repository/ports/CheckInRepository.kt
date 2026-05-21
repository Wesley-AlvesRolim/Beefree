package com.wesley.beefree.domain.repository.ports

import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.WeeklyCheckIn
import kotlinx.coroutines.flow.Flow

interface CheckInRepository {
    suspend fun insertDailyCheckIn(checkIn: DailyCheckIn): Long

    fun getDailyCheckIns(userId: Int): Flow<List<DailyCheckIn>>

    suspend fun insertWeeklyCheckIn(checkIn: WeeklyCheckIn): Long

    fun getWeeklyCheckIns(userId: Int): Flow<List<WeeklyCheckIn>>
}
