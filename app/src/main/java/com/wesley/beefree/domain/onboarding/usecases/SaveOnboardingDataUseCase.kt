package com.wesley.beefree.domain.onboarding.usecases

import com.wesley.beefree.data.keywords.getBetsKeyWords
import com.wesley.beefree.data.keywords.getPornKeywords
import com.wesley.beefree.domain.entities.AddictionKeyword
import com.wesley.beefree.domain.entities.AddictionType
import com.wesley.beefree.domain.entities.AddictionTypeEnum
import com.wesley.beefree.domain.onboarding.AddictionProfile
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.storage.ports.AddictionRepository
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository

class SaveOnboardingDataUseCase(
    private val addictionRepository: AddictionRepository,
    private val keyValueStorageRepository: KeyValueStorageRepository,
) {
    suspend fun execute(answers: OnboardingAnswers): Result<Unit> {
        val profile =
            answers.addictionProfile
                ?: return Result.failure(IllegalStateException("No addiction profile selected"))

        return runCatching {
            val now = System.currentTimeMillis()
            val (name, keywords) =
                when (profile) {
                    AddictionProfile.PPU -> AddictionTypeEnum.ADULT_CONTENT.name to getPornKeywords()
                    AddictionProfile.GAMBLING -> AddictionTypeEnum.BETS.name to getBetsKeyWords()
                }

            val addictionTypeId =
                addictionRepository.insertAddictionType(
                    AddictionType(
                        name = name,
                        isMonitoringEnabled = true,
                        createdAt = now,
                        updatedAt = now,
                    ),
                )

            keywords.forEach { keyword ->
                addictionRepository.insertKeyword(
                    AddictionKeyword(
                        addictionTypeId = addictionTypeId.toInt(),
                        keyword = keyword,
                    ),
                )
            }

            keyValueStorageRepository.saveOnboardingCompleted(true)
        }
    }
}
