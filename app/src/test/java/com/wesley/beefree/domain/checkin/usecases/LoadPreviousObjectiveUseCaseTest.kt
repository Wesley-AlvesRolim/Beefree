package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.DailyCheckInAnswer
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.repository.ports.CheckInRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class LoadPreviousObjectiveUseCaseTest {
    private val checkInRepository: CheckInRepository = mock()
    private val useCase = LoadPreviousObjectiveUseCase(checkInRepository)

    private val userId = 1
    private val now = System.currentTimeMillis()
    private val dayMs = 24L * 60 * 60 * 1000

    private val yesterdayTimestamp = now - dayMs

    private fun checkIn(
        checkedInAt: Long,
        answers: Map<String, DailyCheckInAnswer> = emptyMap(),
    ) = DailyCheckIn(
        userProfileId = userId,
        treatmentProfile = TreatmentProfile.TCC,
        answers = answers,
        checkedInAt = checkedInAt,
    )

    @Test
    fun `returns null when no check-ins exist`() {
        runBlocking {
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(flowOf(emptyList()))
            assertNull(useCase.execute(userId))
        }
    }

    @Test
    fun `returns null when only today check-in exists`() {
        runBlocking {
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(
                flowOf(listOf(checkIn(now))),
            )
            assertNull(useCase.execute(userId))
        }
    }

    @Test
    fun `returns null when yesterday check-in has no goal answer`() {
        runBlocking {
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(
                flowOf(
                    listOf(
                        checkIn(
                            checkedInAt = yesterdayTimestamp,
                            answers = mapOf("act.emotional_record" to DailyCheckInAnswer.EmotionalRecord(true)),
                        ),
                    ),
                ),
            )
            assertNull(useCase.execute(userId))
        }
    }

    @Test
    fun `returns goal text when yesterday check-in has TextWithSuggestions answer`() {
        runBlocking {
            val goal = "Vou caminhar 10 minutos"
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(
                flowOf(
                    listOf(
                        checkIn(
                            checkedInAt = yesterdayTimestamp,
                            answers = mapOf("act.goal_definition" to DailyCheckInAnswer.TextWithSuggestions(goal)),
                        ),
                    ),
                ),
            )
            assertEquals(goal, useCase.execute(userId))
        }
    }

    @Test
    fun `returns goal from most recent yesterday check-in when multiple exist`() {
        runBlocking {
            val olderGoal = "Objetivo mais antigo"
            val newerGoal = "Objetivo mais recente"
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(
                flowOf(
                    listOf(
                        checkIn(
                            checkedInAt = yesterdayTimestamp - 3600_000,
                            answers = mapOf("act.goal_definition" to DailyCheckInAnswer.TextWithSuggestions(olderGoal)),
                        ),
                        checkIn(
                            checkedInAt = yesterdayTimestamp,
                            answers = mapOf("act.goal_definition" to DailyCheckInAnswer.TextWithSuggestions(newerGoal)),
                        ),
                    ),
                ),
            )
            assertEquals(newerGoal, useCase.execute(userId))
        }
    }
}
