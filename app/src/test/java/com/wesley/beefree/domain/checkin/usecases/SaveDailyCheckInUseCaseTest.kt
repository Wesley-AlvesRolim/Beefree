package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.checkin.DailyCheckInAnswer
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.repository.ports.AddictionRepository
import com.wesley.beefree.domain.repository.ports.CheckInRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SaveDailyCheckInUseCaseTest {
    private val checkInRepository: CheckInRepository = mock()
    private val addictionRepository: AddictionRepository = mock()
    private val useCase = SaveDailyCheckInUseCase(checkInRepository, addictionRepository)

    @Test
    fun `saves check-in with answers and no relapse when no relapse answer present`() {
        runBlocking {
            whenever(checkInRepository.insertDailyCheckIn(any())).thenReturn(1L)

            val result =
                useCase.execute(
                    userId = 1,
                    treatmentProfile = TreatmentProfile.ACT,
                    answers =
                        mapOf(
                            "act.goal_definition" to DailyCheckInAnswer.TextWithSuggestions("Vou caminhar amanhã"),
                        ),
                )

            assertTrue(result.isSuccess)
            verify(checkInRepository).insertDailyCheckIn(any())
            verify(addictionRepository, never()).insertRelapse(any())
        }
    }

    @Test
    fun `saves check-in and relapse record when relapse answer present`() {
        runBlocking {
            whenever(checkInRepository.insertDailyCheckIn(any())).thenReturn(1L)
            whenever(addictionRepository.insertRelapse(any())).thenReturn(1L)

            val result =
                useCase.execute(
                    userId = 1,
                    treatmentProfile = TreatmentProfile.TCC,
                    answers =
                        mapOf(
                            "tcc.relapse_reg" to
                                DailyCheckInAnswer.RelapseRegistration(
                                    hour = 22,
                                    minute = 0,
                                    triggers = listOf("stress", "boredom"),
                                    context = null,
                                ),
                        ),
                    addictionTypeId = 1,
                )

            assertTrue(result.isSuccess)
            verify(checkInRepository).insertDailyCheckIn(any())
            verify(addictionRepository).insertRelapse(any())
        }
    }

    @Test
    fun `saves check-in with empty answers`() {
        runBlocking {
            whenever(checkInRepository.insertDailyCheckIn(any())).thenReturn(1L)

            val result =
                useCase.execute(
                    userId = 1,
                    treatmentProfile = TreatmentProfile.ACT,
                    answers = emptyMap(),
                )

            assertTrue(result.isSuccess)
            verify(checkInRepository).insertDailyCheckIn(any())
        }
    }

    @Test
    fun `propagates failure on repository error`() {
        runBlocking {
            whenever(checkInRepository.insertDailyCheckIn(any())).thenThrow(RuntimeException("DB error"))

            val result =
                useCase.execute(
                    userId = 1,
                    treatmentProfile = TreatmentProfile.ACT,
                    answers = emptyMap(),
                )

            assertTrue(result.isFailure)
        }
    }
}
