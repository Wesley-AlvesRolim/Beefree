package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.checkin.ArtificialDopamineSource
import com.wesley.beefree.domain.checkin.DopamineType
import com.wesley.beefree.domain.checkin.NaturalDopamineSource
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
    fun `natural dopamine saves check-in and micro-activity log, no relapse`() {
        runBlocking {
            whenever(checkInRepository.insertDailyCheckIn(any())).thenReturn(1L)
            val result =
                useCase.execute(
                    userId = 1,
                    dopamineLevel = 4,
                    mood = "Feliz",
                    anxietyLevel = 2,
                    dopamineType = DopamineType.Natural(NaturalDopamineSource.HOBBY),
                )

            assertTrue(result.isSuccess)
            verify(checkInRepository).insertDailyCheckIn(any())
            verify(addictionRepository, never()).insertRelapse(any())
        }
    }

    @Test
    fun `natural dopamine with no existing activity creates micro-activity first`() {
        runBlocking {
            whenever(checkInRepository.insertDailyCheckIn(any())).thenReturn(1L)

            val result =
                useCase.execute(
                    userId = 1,
                    dopamineLevel = 3,
                    mood = "Ok",
                    anxietyLevel = null,
                    dopamineType = DopamineType.Natural(NaturalDopamineSource.EXERCISE),
                )

            assertTrue(result.isSuccess)
            verify(addictionRepository, never()).insertRelapse(any())
        }
    }

    @Test
    fun `artificial dopamine saves check-in and relapse, no activity log`() {
        runBlocking {
            whenever(checkInRepository.insertDailyCheckIn(any())).thenReturn(1L)
            whenever(addictionRepository.insertRelapse(any())).thenReturn(1L)

            val result =
                useCase.execute(
                    userId = 1,
                    dopamineLevel = 5,
                    mood = "Ansioso",
                    anxietyLevel = 4,
                    dopamineType = DopamineType.Artificial(ArtificialDopamineSource.ADULT_CONTENT),
                    addictionTypeId = 1,
                )

            assertTrue(result.isSuccess)
            verify(checkInRepository).insertDailyCheckIn(any())
            verify(addictionRepository).insertRelapse(any())
        }
    }

    @Test
    fun `propagates failure on repository error`() =
        runBlocking {
            whenever(checkInRepository.insertDailyCheckIn(any())).thenThrow(RuntimeException("DB error"))

            val result =
                useCase.execute(
                    userId = 1,
                    dopamineLevel = 3,
                    mood = "Ok",
                    anxietyLevel = null,
                    dopamineType = DopamineType.Natural(NaturalDopamineSource.SOCIAL),
                )

            assertTrue(result.isFailure)
        }
}
