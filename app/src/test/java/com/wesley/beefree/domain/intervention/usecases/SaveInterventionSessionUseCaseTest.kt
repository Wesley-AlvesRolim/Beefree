package com.wesley.beefree.domain.intervention.usecases

import com.wesley.beefree.domain.entities.CognitiveThoughtRecord
import com.wesley.beefree.domain.entities.CoreValueType
import com.wesley.beefree.domain.entities.InterventionRecord
import com.wesley.beefree.domain.entities.InterventionValueLink
import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.infrastructure.storage.ports.EMIRepository
import com.wesley.beefree.infrastructure.storage.ports.OnboardingRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SaveInterventionSessionUseCaseTest {
    private val emiRepository: EMIRepository = mock()
    private val onboardingRepository: OnboardingRepository = mock()
    private val useCase = SaveInterventionSessionUseCase(emiRepository, onboardingRepository)

    private val userProfileId = 1
    private val now = System.currentTimeMillis()

    private val interventionRecord =
        InterventionRecord(
            userProfileId = userProfileId,
            interventionType = "emi",
            triggerType = "WIDGET",
            wasCompleted = true,
            createdAt = now,
        )

    private val thoughtRecord =
        CognitiveThoughtRecord(
            userProfileId = userProfileId,
            situation = "trigger",
            automaticThought = "I need it",
            feeling = "anxious",
            consequence = "relapse",
            alternativeThought = "I can wait",
            cognitiveDistortions = listOf("catastrophizing"),
            createdAt = now,
        )

    @Test
    fun `saves intervention without linking when no core value is provided`() =
        runTest {
            whenever(emiRepository.insertInterventionRecord(interventionRecord)).thenReturn(10L)

            useCase.execute(interventionRecord)

            verify(emiRepository).insertInterventionRecord(interventionRecord)
            verify(emiRepository, never()).insertThoughtRecord(any())
            verify(emiRepository, never()).insertInterventionValueLink(any())
            verify(onboardingRepository, never()).getCoreValues(any())
            verify(onboardingRepository, never()).insertCoreValue(any())
        }

    @Test
    fun `saves thought record when provided`() =
        runTest {
            whenever(emiRepository.insertInterventionRecord(interventionRecord)).thenReturn(11L)
            whenever(emiRepository.insertThoughtRecord(thoughtRecord)).thenReturn(22L)

            useCase.execute(interventionRecord, thoughtRecord)

            verify(emiRepository).insertInterventionRecord(interventionRecord)
            verify(emiRepository).insertThoughtRecord(thoughtRecord)
            verify(emiRepository, never()).insertInterventionValueLink(any())
        }

    @Test
    fun `links to an existing core value`() =
        runTest {
            val interventionId = 12L
            val faithCoreValueId = 3
            whenever(emiRepository.insertInterventionRecord(interventionRecord)).thenReturn(interventionId)
            whenever(onboardingRepository.getCoreValues(userProfileId)).thenReturn(
                flowOf(
                    listOf(
                        UserCoreValue(
                            id = 2,
                            userProfileId = userProfileId,
                            value = CoreValueType.FAMILY,
                            createdAt = now,
                        ),
                        UserCoreValue(
                            id = faithCoreValueId,
                            userProfileId = userProfileId,
                            value = CoreValueType.FAITH,
                            createdAt = now,
                        ),
                    ),
                ),
            )

            useCase.execute(
                interventionRecord = interventionRecord,
                selectedCoreValue = CoreValueType.FAITH,
                userProfileId = userProfileId,
            )

            val linkCaptor = argumentCaptor<InterventionValueLink>()
            verify(emiRepository).insertInterventionValueLink(linkCaptor.capture())
            assertTrue(linkCaptor.firstValue.interventionId == interventionId.toInt())
            assertTrue(linkCaptor.firstValue.userCoreValueId == faithCoreValueId)
            verify(onboardingRepository, never()).insertCoreValue(any())
        }

    @Test
    fun `creates and links a new core value when none exists`() =
        runTest {
            val interventionId = 13L
            val coreValueId = 4L
            whenever(emiRepository.insertInterventionRecord(interventionRecord)).thenReturn(interventionId)
            whenever(onboardingRepository.getCoreValues(userProfileId)).thenReturn(flowOf(emptyList()))
            whenever(onboardingRepository.insertCoreValue(any())).thenReturn(coreValueId)

            useCase.execute(
                interventionRecord = interventionRecord,
                selectedCoreValue = CoreValueType.HEALTH,
                userProfileId = userProfileId,
            )

            val coreValueCaptor = argumentCaptor<UserCoreValue>()
            verify(onboardingRepository).insertCoreValue(coreValueCaptor.capture())
            assertTrue(coreValueCaptor.firstValue.userProfileId == userProfileId)
            assertTrue(coreValueCaptor.firstValue.value == CoreValueType.HEALTH)
            assertTrue(coreValueCaptor.firstValue.createdAt > 0)

            val linkCaptor = argumentCaptor<InterventionValueLink>()
            verify(emiRepository).insertInterventionValueLink(linkCaptor.capture())
            assertTrue(linkCaptor.firstValue.interventionId == interventionId.toInt())
            assertTrue(linkCaptor.firstValue.userCoreValueId == coreValueId.toInt())
        }

    @Test
    fun `does not link when intervention insert does not return a positive id`() =
        runTest {
            whenever(emiRepository.insertInterventionRecord(interventionRecord)).thenReturn(0L)

            useCase.execute(
                interventionRecord = interventionRecord,
                selectedCoreValue = CoreValueType.LOVE,
                userProfileId = userProfileId,
            )

            verify(onboardingRepository, never()).getCoreValues(any())
            verify(onboardingRepository, never()).insertCoreValue(any())
            verify(emiRepository, never()).insertInterventionValueLink(any())
        }
}
