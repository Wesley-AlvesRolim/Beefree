package com.wesley.beefree.domain.onboarding.usecases

import com.wesley.beefree.data.getPsychologistEncouragementPhrases
import com.wesley.beefree.domain.entities.AddictionCategory
import com.wesley.beefree.domain.entities.CoreValueType
import com.wesley.beefree.domain.onboarding.AddictionProfile
import com.wesley.beefree.domain.onboarding.ClinicalProfile
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.repository.ports.AddictionRepository
import com.wesley.beefree.domain.repository.ports.LessonRepository
import com.wesley.beefree.domain.repository.ports.OnboardingRepository
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
import com.wesley.beefree.infrastructure.storage.repositories.KeyValueStorageRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SaveOnboardingDataUseCaseTest {
    private val addictionRepository: AddictionRepository = mock()
    private val userProfileRepository: UserProfileRepository = mock()
    private val onboardingRepository: OnboardingRepository = mock()
    private val lessonRepository: LessonRepository = mock()
    private val keyValueStorageRepository: KeyValueStorageRepository = mock()
    private val computeScoreUseCase: ComputeScoreUseCase = mock()
    private val computeClinicalProfileUseCase: ComputeClinicalProfileUseCase = mock()
    private val useCase =
        SaveOnboardingDataUseCase(
            addictionRepository,
            userProfileRepository,
            onboardingRepository,
            lessonRepository,
            keyValueStorageRepository,
            computeScoreUseCase,
            computeClinicalProfileUseCase,
        )

    @Before
    fun setUp() {
        whenever(computeClinicalProfileUseCase.execute(any())).thenReturn(
            ClinicalProfile(
                incongruenceLevel = null,
                treatmentProfile = TreatmentProfile.PREVENTION,
                moralIncongruenceScore = 0,
            ),
        )
    }

    @Test
    fun `returns failure when no addiction profile selected`() =
        runBlocking {
            val answers = OnboardingAnswers(addictionProfile = null)

            val result = useCase.execute(answers)

            assertTrue(result.isFailure)
            verify(addictionRepository, never()).insertAddictionCategory(any())
            verify(keyValueStorageRepository, never()).saveOnboardingCompleted(any())
        }

    @Test
    fun `saves addiction category and marks onboarding completed for PPU profile`() =
        runBlocking {
            whenever(userProfileRepository.insertProfile(any())).thenReturn(1L)
            whenever(addictionRepository.insertAddictionCategory(any<AddictionCategory>())).thenReturn(1L)
            whenever(onboardingRepository.insertOnboardingSession(any())).thenReturn(1L)
            whenever(lessonRepository.insertContent(any())).thenReturn(1L)
            val answers =
                OnboardingAnswers(
                    addictionProfile = AddictionProfile.PPU,
                    ppcs6Answers = listOf(1, 1, 1, 1, 1, 1),
                    frequencyAnswer = 1,
                )

            val result = useCase.execute(answers)

            assertTrue(result.isSuccess)
            verify(addictionRepository, times(2)).insertAddictionCategory(any())
            verify(lessonRepository, times(getPsychologistEncouragementPhrases().size)).insertContent(any())
            verify(keyValueStorageRepository).saveOnboardingCompleted(true)
        }

    @Test
    fun `saves addiction category and marks onboarding completed for GAMBLING profile`() =
        runBlocking {
            whenever(userProfileRepository.insertProfile(any())).thenReturn(1L)
            whenever(addictionRepository.insertAddictionCategory(any<AddictionCategory>())).thenReturn(1L)
            whenever(onboardingRepository.insertOnboardingSession(any())).thenReturn(1L)
            whenever(lessonRepository.insertContent(any())).thenReturn(1L)
            val answers =
                OnboardingAnswers(
                    addictionProfile = AddictionProfile.GAMBLING,
                    pgsiAnswers = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
                )

            val result = useCase.execute(answers)

            assertTrue(result.isSuccess)
            verify(addictionRepository, times(2)).insertAddictionCategory(any())
            verify(lessonRepository, times(getPsychologistEncouragementPhrases().size)).insertContent(any())
            verify(keyValueStorageRepository).saveOnboardingCompleted(true)
        }

    @Test
    fun `saves hobbies, symptoms, goals and core values`() {
        runBlocking {
            whenever(userProfileRepository.insertProfile(any())).thenReturn(1L)
            whenever(addictionRepository.insertAddictionCategory(any<AddictionCategory>())).thenReturn(1L)
            whenever(onboardingRepository.insertOnboardingSession(any())).thenReturn(1L)
            whenever(lessonRepository.insertContent(any())).thenReturn(1L)
            val answers =
                OnboardingAnswers(
                    addictionProfile = AddictionProfile.PPU,
                    ppcs6Answers = listOf(1, 1, 1, 1, 1, 1),
                    frequencyAnswer = 1,
                    hobbies = listOf("Leitura", "Academia"),
                    symptoms = listOf("ANXIETY", "ISOLATION"),
                    goals = listOf("Ser mais presente"),
                    coreValues = listOf(CoreValueType.FAMILY.name, CoreValueType.FAITH.name),
                )

            val result = useCase.execute(answers)

            assertTrue(result.isSuccess)
            verify(onboardingRepository, times(2)).insertHobby(any())
            verify(onboardingRepository, times(2)).insertSymptom(any())
            verify(onboardingRepository, times(1)).insertObjective(any())
            verify(onboardingRepository, times(2)).insertCoreValue(any())
            verify(lessonRepository, times(getPsychologistEncouragementPhrases().size)).insertContent(any())
        }
    }

    @Test
    fun `propagates repository failure as Result failure`() =
        runBlocking {
            whenever(userProfileRepository.insertProfile(any()))
                .thenThrow(RuntimeException("DB error"))
            val answers = OnboardingAnswers(addictionProfile = AddictionProfile.PPU)

            val result = useCase.execute(answers)

            assertTrue(result.isFailure)
        }
}
