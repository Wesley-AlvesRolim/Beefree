package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.checkin.ArtificialDopamineSource
import com.wesley.beefree.domain.checkin.DopamineType
import com.wesley.beefree.domain.checkin.NaturalDopamineSource
import com.wesley.beefree.domain.entities.MicroActivity
import com.wesley.beefree.infrastructure.storage.ports.ActivityRepository
import com.wesley.beefree.infrastructure.storage.ports.AddictionRepository
import com.wesley.beefree.infrastructure.storage.ports.CheckInRepository
import kotlinx.coroutines.flow.flowOf
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
    private val activityRepository: ActivityRepository = mock()

    private val useCase = SaveDailyCheckInUseCase(checkInRepository, addictionRepository, activityRepository)

    @Test
    fun `natural dopamine saves check-in and micro-activity log, no relapse`() {
        val activity = MicroActivity(id = 1, addictionTypeId = null, activityType = "HOBBY", activityName = "Hobby", createdAt = 0L)
        runBlocking {
            whenever(checkInRepository.insertDailyCheckIn(any())).thenReturn(1L)
            whenever(activityRepository.getAllMicroActivities()).thenReturn(flowOf(listOf(activity)))
            whenever(activityRepository.insertDailyLog(any())).thenReturn(1L)

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
            verify(activityRepository).insertDailyLog(any())
            verify(addictionRepository, never()).insertRelapse(any())
        }
    }

    @Test
    fun `natural dopamine with no existing activity creates micro-activity first`() {
        runBlocking {
            whenever(checkInRepository.insertDailyCheckIn(any())).thenReturn(1L)
            whenever(activityRepository.getAllMicroActivities()).thenReturn(flowOf(emptyList()))
            whenever(activityRepository.insertMicroActivity(any())).thenReturn(1L)
            whenever(activityRepository.insertDailyLog(any())).thenReturn(1L)

            val result =
                useCase.execute(
                    userId = 1,
                    dopamineLevel = 3,
                    mood = "Ok",
                    anxietyLevel = null,
                    dopamineType = DopamineType.Natural(NaturalDopamineSource.EXERCISE),
                )

            assertTrue(result.isSuccess)
            verify(activityRepository).insertMicroActivity(any())
            verify(activityRepository).insertDailyLog(any())
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
            verify(activityRepository, never()).insertDailyLog(any())
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
