package com.wesley.beefree.domain.intervention.usecases

import com.wesley.beefree.domain.entities.CognitiveThoughtRecord
import com.wesley.beefree.domain.entities.CoreValueType
import com.wesley.beefree.domain.entities.InterventionRecord
import com.wesley.beefree.domain.entities.InterventionValueLink
import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.infrastructure.storage.ports.EMIRepository
import com.wesley.beefree.infrastructure.storage.ports.OnboardingRepository
import kotlinx.coroutines.flow.first

class SaveInterventionSessionUseCase(
    private val emiRepository: EMIRepository,
    private val onboardingRepository: OnboardingRepository,
) {
    suspend fun execute(
        interventionRecord: InterventionRecord,
        thoughtRecord: CognitiveThoughtRecord? = null,
        selectedCoreValue: CoreValueType? = null,
        userProfileId: Int? = null,
    ) {
        val interventionId = emiRepository.insertInterventionRecord(interventionRecord)

        thoughtRecord?.let {
            emiRepository.insertThoughtRecord(it)
        }

        if (selectedCoreValue != null && userProfileId != null && interventionId > 0) {
            handleCoreValueLinking(interventionId.toInt(), selectedCoreValue, userProfileId)
        }
    }

    private suspend fun handleCoreValueLinking(
        interventionId: Int,
        selectedCoreValue: CoreValueType,
        userProfileId: Int,
    ) {
        val existingValues = onboardingRepository.getCoreValues(userProfileId).first()

        val coreValueId =
            findExistingCoreValueId(existingValues, selectedCoreValue)
                ?: createNewCoreValue(userProfileId, selectedCoreValue)

        val link =
            InterventionValueLink(
                interventionId = interventionId,
                userCoreValueId = coreValueId,
            )
        emiRepository.insertInterventionValueLink(link)
    }

    private fun findExistingCoreValueId(
        existingValues: List<UserCoreValue>,
        targetValue: CoreValueType,
    ): Int? = existingValues.find { it.value == targetValue }?.id

    private suspend fun createNewCoreValue(
        userProfileId: Int,
        value: CoreValueType,
    ): Int =
        onboardingRepository
            .insertCoreValue(
                UserCoreValue(
                    userProfileId = userProfileId,
                    value = value,
                    createdAt = System.currentTimeMillis(),
                ),
            ).toInt()
}
