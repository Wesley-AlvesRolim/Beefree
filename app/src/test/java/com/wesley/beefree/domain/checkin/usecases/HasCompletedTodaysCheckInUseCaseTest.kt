package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.WeeklyCheckIn
import com.wesley.beefree.storage.ports.CheckInRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class HasCompletedTodaysCheckInUseCaseTest {
    private val checkInRepository: CheckInRepository = mock()
    private val useCase = HasCompletedTodaysCheckInUseCase(checkInRepository, DetermineCheckInTypeUseCase())

    private val userId = 1
    private val now = System.currentTimeMillis()
    private val dayMs = 24L * 60 * 60 * 1000

    private val dailyCreatedAt = now

    private val weeklyCreatedAt = now - 6 * dayMs

    @Test
    fun `returns true when daily check-in exists for today`() {
        runBlocking {
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(
                flowOf(
                    listOf(DailyCheckIn(userProfileId = userId, dopamineLevel = 3, mood = "ok", anxietyLevel = null, checkedInAt = now)),
                ),
            )

            assertTrue(useCase.execute(userId, dailyCreatedAt, now))
        }
    }

    @Test
    fun `returns false when no daily check-in exists for today`() {
        runBlocking {
            val yesterday = now - dayMs
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(
                flowOf(
                    listOf(
                        DailyCheckIn(userProfileId = userId, dopamineLevel = 3, mood = "ok", anxietyLevel = null, checkedInAt = yesterday),
                    ),
                ),
            )

            assertFalse(useCase.execute(userId, dailyCreatedAt, now))
        }
    }

    @Test
    fun `returns false when no check-ins at all`() {
        runBlocking {
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(flowOf(emptyList()))

            assertFalse(useCase.execute(userId, dailyCreatedAt, now))
        }
    }

    @Test
    fun `returns true when weekly check-in exists for current week`() {
        runBlocking {
            val weekStart = HasCompletedTodaysCheckInUseCase.currentWeekStart(now)
            whenever(checkInRepository.getWeeklyCheckIns(userId)).thenReturn(
                flowOf(
                    listOf(
                        WeeklyCheckIn(
                            userProfileId = userId,
                            weekStartDate = weekStart,
                            valuesAlignmentText = null,
                            realConnectionEnergy = null,
                            createdAt = now,
                        ),
                    ),
                ),
            )

            assertTrue(useCase.execute(userId, weeklyCreatedAt, now))
        }
    }

    @Test
    fun `returns false when weekly check-in is from a different week`() {
        runBlocking {
            val lastWeekStart = HasCompletedTodaysCheckInUseCase.currentWeekStart(now) - (7 * dayMs)
            whenever(checkInRepository.getWeeklyCheckIns(userId)).thenReturn(
                flowOf(
                    listOf(
                        WeeklyCheckIn(
                            userProfileId = userId,
                            weekStartDate = lastWeekStart,
                            valuesAlignmentText = null,
                            realConnectionEnergy = null,
                            createdAt = now,
                        ),
                    ),
                ),
            )

            assertFalse(useCase.execute(userId, weeklyCreatedAt, now))
        }
    }
}
