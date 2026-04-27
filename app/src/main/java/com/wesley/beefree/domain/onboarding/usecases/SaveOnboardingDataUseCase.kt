package com.wesley.beefree.domain.onboarding.usecases

import com.wesley.beefree.domain.entities.AddictionCategory
import com.wesley.beefree.domain.entities.AddictionCategoryEnum
import com.wesley.beefree.domain.entities.UserAddiction
import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.domain.entities.UserHobby
import com.wesley.beefree.domain.entities.UserObjective
import com.wesley.beefree.domain.entities.UserOnboardingSession
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.entities.UserSymptom
import com.wesley.beefree.domain.onboarding.AddictionProfile
import com.wesley.beefree.domain.onboarding.ClinicalProfile
import com.wesley.beefree.domain.onboarding.NeurodivergenceAnswer
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.ScaleResult
import com.wesley.beefree.infrastructure.storage.ports.AddictionRepository
import com.wesley.beefree.infrastructure.storage.ports.OnboardingRepository
import com.wesley.beefree.infrastructure.storage.ports.UserProfileRepository
import com.wesley.beefree.infrastructure.storage.repositories.KeyValueStorageRepository

class SaveOnboardingDataUseCase(
    private val addictionRepository: AddictionRepository,
    private val userProfileRepository: UserProfileRepository,
    private val onboardingRepository: OnboardingRepository,
    private val keyValueStorageRepository: KeyValueStorageRepository,
    private val computeScoreUseCase: ComputeScoreUseCase,
    private val computeClinicalProfileUseCase: ComputeClinicalProfileUseCase,
) {
    suspend fun execute(answers: OnboardingAnswers): Result<Unit> {
        val profile =
            answers.addictionProfile
                ?: return Result.failure(IllegalStateException("No addiction profile selected"))

        return runCatching {
            val now = System.currentTimeMillis()

            val userProfileId = insertUserProfile(answers, now)
            val selectedCategoryId = insertAddictionCategories(profile, userProfileId, now)
            val scoreResult = computeScoreUseCase.execute(answers)
            val clinicalProfile =
                computeClinicalProfileUseCase.execute(answers)
                    ?: throw IllegalStateException("Clinical profile could not be computed")

            insertOnboardingSession(userProfileId, selectedCategoryId, profile, scoreResult, clinicalProfile, answers, now)
            insertUserChoices(userProfileId, answers, now)

            keyValueStorageRepository.saveOnboardingCompleted(true)
            keyValueStorageRepository.saveTheScreenReaderStatus(true)
        }
    }

    private suspend fun insertUserProfile(
        answers: OnboardingAnswers,
        now: Long,
    ): Int =
        userProfileRepository
            .insertProfile(
                UserProfile(
                    profileName = answers.userName,
                    gender = answers.gender,
                    createdAt = now,
                    updatedAt = now,
                ),
            ).toInt()

    private suspend fun insertAddictionCategories(
        profile: AddictionProfile,
        userProfileId: Int,
        now: Long,
    ): Int {
        val categoryConfigs =
            listOf(
                AddictionProfile.PPU to AddictionCategoryEnum.ADULT_CONTENT.name,
                AddictionProfile.GAMBLING to AddictionCategoryEnum.BETS.name,
            )

        var selectedCategoryId = 0

        categoryConfigs.forEach { (typeProfile, name) ->
            val isEnabled = typeProfile == profile

            val categoryId =
                addictionRepository
                    .insertAddictionCategory(
                        AddictionCategory(
                            name = name,
                            isMonitoringEnabled = isEnabled,
                            createdAt = now,
                            updatedAt = now,
                        ),
                    ).toInt()

            userProfileRepository.associateAddictionToProfile(
                UserAddiction(
                    userProfileId = userProfileId,
                    addictionCategoryId = categoryId,
                    createdAt = now,
                ),
            )

            if (isEnabled) selectedCategoryId = categoryId
        }

        return selectedCategoryId
    }

    private suspend fun insertOnboardingSession(
        userProfileId: Int,
        addictionCategoryId: Int,
        profile: AddictionProfile,
        scoreResult: ScaleResult?,
        clinicalProfile: ClinicalProfile,
        answers: OnboardingAnswers,
        now: Long,
    ) {
        onboardingRepository.insertOnboardingSession(
            UserOnboardingSession(
                userProfileId = userProfileId,
                clinicalApproach = clinicalProfile.treatmentProfile.name,
                ppcsScore = if (profile == AddictionProfile.PPU) scoreResult?.raw else null,
                pgsiScore = if (profile == AddictionProfile.GAMBLING) scoreResult?.raw else null,
                moralIncongruenceScore = clinicalProfile.moralIncongruenceScore,
                frequencyScore = if (profile == AddictionProfile.PPU) answers.frequencyAnswer else null,
                moralDisapprovalScore =
                    if (profile == AddictionProfile.PPU) {
                        (answers.emaAnswers.firstOrNull() ?: 0) + 1
                    } else {
                        null
                    },
                hasNeurodivergence = answers.neurodivergenceAnswer == NeurodivergenceAnswer.YES,
                createdAt = now,
            ),
        )
    }

    private suspend fun insertUserChoices(
        userProfileId: Int,
        answers: OnboardingAnswers,
        now: Long,
    ) {
        answers.hobbies.forEach { hobby ->
            onboardingRepository.insertHobby(
                UserHobby(userProfileId = userProfileId, hobbyName = hobby, createdAt = now),
            )
        }
        answers.goals.forEach { goal ->
            onboardingRepository.insertObjective(
                UserObjective(userProfileId = userProfileId, objectiveText = goal, createdAt = now),
            )
        }
        answers.symptoms.forEach { symptom ->
            onboardingRepository.insertSymptom(
                UserSymptom(userProfileId = userProfileId, symptomCode = symptom, createdAt = now),
            )
        }
        answers.coreValues.forEach { value ->
            onboardingRepository.insertCoreValue(
                UserCoreValue(userProfileId = userProfileId, valueName = value, createdAt = now),
            )
        }
    }
}
