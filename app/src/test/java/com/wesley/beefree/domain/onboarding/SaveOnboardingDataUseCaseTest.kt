package com.wesley.beefree.domain.onboarding

import com.wesley.beefree.domain.entities.AddictionType
import com.wesley.beefree.storage.ports.AddictionRepository
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SaveOnboardingDataUseCaseTest {
    private val addictionRepository: AddictionRepository = mock()
    private val keyValueStorageRepository: KeyValueStorageRepository = mock()
    private val useCase = SaveOnboardingDataUseCase(addictionRepository, keyValueStorageRepository)

    @Test
    fun `returns failure when no addiction profile selected`() =
        runBlocking {
            val answers = OnboardingAnswers(addictionProfile = null)

            val result = useCase.execute(answers)

            assertTrue(result.isFailure)
            verify(addictionRepository, never()).insertAddictionType(any())
            verify(keyValueStorageRepository, never()).saveOnboardingCompleted(any())
        }

    @Test
    fun `saves addiction type and marks onboarding completed for PPU profile`() =
        runBlocking {
            whenever(addictionRepository.insertAddictionType(any<AddictionType>())).thenReturn(1L)
            whenever(addictionRepository.insertKeyword(any())).thenReturn(1L)
            val answers = OnboardingAnswers(addictionProfile = AddictionProfile.PPU)

            val result = useCase.execute(answers)

            assertTrue(result.isSuccess)
            verify(addictionRepository).insertAddictionType(any())
            verify(keyValueStorageRepository).saveOnboardingCompleted(true)
        }

    @Test
    fun `saves addiction type and marks onboarding completed for GAMBLING profile`() =
        runBlocking {
            whenever(addictionRepository.insertAddictionType(any<AddictionType>())).thenReturn(1L)
            whenever(addictionRepository.insertKeyword(any())).thenReturn(1L)
            val answers = OnboardingAnswers(addictionProfile = AddictionProfile.GAMBLING)

            val result = useCase.execute(answers)

            assertTrue(result.isSuccess)
            verify(addictionRepository).insertAddictionType(any())
            verify(keyValueStorageRepository).saveOnboardingCompleted(true)
        }

    @Test
    fun `propagates repository failure as Result failure`() =
        runBlocking {
            whenever(addictionRepository.insertAddictionType(any<AddictionType>()))
                .thenThrow(RuntimeException("DB error"))
            val answers = OnboardingAnswers(addictionProfile = AddictionProfile.PPU)

            val result = useCase.execute(answers)

            assertTrue(result.isFailure)
        }
}
