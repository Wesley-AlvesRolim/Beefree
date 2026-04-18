package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.entities.WeeklyCheckIn
import com.wesley.beefree.storage.ports.CheckInRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SaveWeeklyCheckInUseCaseTest {
    private val checkInRepository: CheckInRepository = mock()
    private val useCase = SaveWeeklyCheckInUseCase(checkInRepository)

    @Test
    fun `saves weekly check-in with provided values`() =
        runBlocking {
            whenever(checkInRepository.insertWeeklyCheckIn(any())).thenReturn(1L)

            val result =
                useCase.execute(
                    userId = 1,
                    valuesAlignmentText = "Fui com a família ao parque",
                    realConnectionEnergy = 4,
                )

            assertTrue(result.isSuccess)
            val captor = argumentCaptor<WeeklyCheckIn>()
            verify(checkInRepository).insertWeeklyCheckIn(captor.capture())
            val saved = captor.firstValue
            assertTrue(saved.userProfileId == 1)
            assertTrue(saved.valuesAlignmentText == "Fui com a família ao parque")
            assertTrue(saved.realConnectionEnergy == 4)
        }

    @Test
    fun `propagates failure on repository error`() =
        runBlocking {
            whenever(checkInRepository.insertWeeklyCheckIn(any())).thenThrow(RuntimeException("DB error"))

            val result = useCase.execute(userId = 1, valuesAlignmentText = null, realConnectionEnergy = null)

            assertTrue(result.isFailure)
        }
}
