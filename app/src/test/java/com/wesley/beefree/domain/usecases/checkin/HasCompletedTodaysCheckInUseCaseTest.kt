package com.wesley.beefree.domain.usecases.checkin

import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.repository.ports.CheckInRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class HasCompletedTodaysCheckInUseCaseTest {
    private val checkInRepository: CheckInRepository = mock()
    private val useCase = HasCompletedTodaysCheckInUseCase(checkInRepository)

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
                    listOf(
                        DailyCheckIn(
                            userProfileId = userId,
                            treatmentProfile = TreatmentProfile.TCC,
                            answers = emptyMap(),
                            checkedInAt = now,
                        ),
                    ),
                ),
            )

            assertTrue(useCase.execute(userId, now))
        }
    }

    @Test
    fun `returns false when no daily check-in exists for today`() {
        runBlocking {
            val yesterday = now - dayMs
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(
                flowOf(
                    listOf(
                        DailyCheckIn(
                            userProfileId = userId,
                            treatmentProfile = TreatmentProfile.TCC,
                            answers = emptyMap(),
                            checkedInAt = yesterday,
                        ),
                    ),
                ),
            )

            assertFalse(useCase.execute(userId, now))
        }
    }

    @Test
    fun `returns false when no check-ins at all`() {
        runBlocking {
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(flowOf(emptyList()))

            assertFalse(useCase.execute(userId, now))
        }
    }

    @Test
    fun `returns true when daily check-in exists today for long-running user`() {
        runBlocking {
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(
                flowOf(
                    listOf(
                        DailyCheckIn(
                            userProfileId = userId,
                            treatmentProfile = TreatmentProfile.ACT,
                            answers = emptyMap(),
                            checkedInAt = now,
                        ),
                    ),
                ),
            )

            assertTrue(useCase.execute(userId, now))
        }
    }

    @Test
    fun `returns false when no daily check-in for long-running user`() {
        runBlocking {
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(flowOf(emptyList()))

            assertFalse(useCase.execute(userId, now))
        }
    }
}
