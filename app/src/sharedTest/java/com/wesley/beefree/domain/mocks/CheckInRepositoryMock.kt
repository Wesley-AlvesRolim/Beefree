package com.wesley.beefree.domain.mocks

import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.WeeklyCheckIn
import com.wesley.beefree.domain.repository.ports.CheckInRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CheckInRepositoryMock : CheckInRepository {
    var dailyCheckIns: List<DailyCheckIn> = emptyList()
    var weeklyCheckIns: List<WeeklyCheckIn> = emptyList()
    var insertDailyCheckInReturn: Long = 0L
    var insertWeeklyCheckInReturn: Long = 0L

    val insertDailyCheckInArgs = mutableListOf<DailyCheckIn>()
    val insertWeeklyCheckInArgs = mutableListOf<WeeklyCheckIn>()

    val insertDailyCheckInCount get() = insertDailyCheckInArgs.size
    val insertWeeklyCheckInCount get() = insertWeeklyCheckInArgs.size

    override suspend fun insertDailyCheckIn(checkIn: DailyCheckIn): Long {
        insertDailyCheckInArgs += checkIn
        return insertDailyCheckInReturn
    }

    override fun getDailyCheckIns(userId: Int): Flow<List<DailyCheckIn>> = flowOf(dailyCheckIns)

    override suspend fun insertWeeklyCheckIn(checkIn: WeeklyCheckIn): Long {
        insertWeeklyCheckInArgs += checkIn
        return insertWeeklyCheckInReturn
    }

    override fun getWeeklyCheckIns(userId: Int): Flow<List<WeeklyCheckIn>> = flowOf(weeklyCheckIns)
}
