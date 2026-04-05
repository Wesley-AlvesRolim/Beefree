package com.wesley.beefree.domain.onboarding.usecases

import com.wesley.beefree.data.keywords.getBetsKeyWords
import com.wesley.beefree.data.keywords.getPornKeywords
import com.wesley.beefree.domain.entities.AddictionKeyword
import com.wesley.beefree.domain.entities.AddictionType
import com.wesley.beefree.domain.entities.AddictionTypeEnum
import com.wesley.beefree.domain.entities.OnboardingScaleAnswer
import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.domain.entities.UserHobby
import com.wesley.beefree.domain.entities.UserObjective
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.entities.UserProfileAddiction
import com.wesley.beefree.domain.entities.UserProfileOnboardingResult
import com.wesley.beefree.domain.entities.UserSymptom
import com.wesley.beefree.domain.onboarding.AddictionProfile
import com.wesley.beefree.domain.onboarding.ClinicalProfile
import com.wesley.beefree.domain.onboarding.NeurodivergenceAnswer
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.domain.onboarding.ScaleResult
import com.wesley.beefree.domain.onboarding.ScaleType
import com.wesley.beefree.storage.ports.AddictionRepository
import com.wesley.beefree.storage.ports.OnboardingRepository
import com.wesley.beefree.storage.ports.UserProfileRepository
import com.wesley.beefree.storage.repositories.KeyValueStorageRepository

class SaveOnboardingDataUseCase(
    private val addictionRepository: AddictionRepository,
    private val userProfileRepository: UserProfileRepository,
    private val onboardingRepository: OnboardingRepository,
    private val keyValueStorageRepository: KeyValueStorageRepository,
) {
    suspend fun execute(answers: OnboardingAnswers): Result<Unit> {
        val profile =
            answers.addictionProfile
                ?: return Result.failure(IllegalStateException("No addiction profile selected"))

        return runCatching {
            val now = System.currentTimeMillis()

            val userProfileId = insertUserProfile(answers, now)
            val selectedAddictionTypeId = insertAddictionTypes(profile, userProfileId, now)
            val scoreResult = ComputeScoreUseCase().execute(answers)
            val clinicalProfile =
                ComputeClinicalProfileUseCase().execute(answers)
                    ?: throw IllegalStateException("Clinical profile could not be computed")

            insertOnboardingResult(userProfileId, selectedAddictionTypeId, profile, scoreResult, clinicalProfile, answers, now)
            insertScaleAnswers(userProfileId, answers, now)
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

    private suspend fun insertAddictionTypes(
        profile: AddictionProfile,
        userProfileId: Int,
        now: Long,
    ): Int {
        val addictionConfigs =
            listOf(
                AddictionProfile.PPU to (AddictionTypeEnum.ADULT_CONTENT.name to getPornKeywords()),
                AddictionProfile.GAMBLING to (AddictionTypeEnum.BETS.name to getBetsKeyWords()),
            )

        var selectedAddictionTypeId = 0

        addictionConfigs.forEach { (typeProfile, data) ->
            val (name, keywords) = data
            val isEnabled = typeProfile == profile

            val addictionTypeId =
                addictionRepository
                    .insertAddictionType(
                        AddictionType(
                            name = name,
                            isMonitoringEnabled = isEnabled,
                            createdAt = now,
                            updatedAt = now,
                        ),
                    ).toInt()

            keywords.forEach { keyword ->
                addictionRepository.insertKeyword(
                    AddictionKeyword(addictionTypeId = addictionTypeId, keyword = keyword),
                )
            }

            userProfileRepository.associateAddictionToProfile(
                UserProfileAddiction(
                    userProfileId = userProfileId,
                    addictionTypeId = addictionTypeId,
                    createdAt = now,
                ),
            )

            if (isEnabled) selectedAddictionTypeId = addictionTypeId
        }

        return selectedAddictionTypeId
    }

    private suspend fun insertOnboardingResult(
        userProfileId: Int,
        addictionTypeId: Int,
        profile: AddictionProfile,
        scoreResult: ScaleResult?,
        clinicalProfile: ClinicalProfile,
        answers: OnboardingAnswers,
        now: Long,
    ) {
        onboardingRepository.insertOnboardingResult(
            UserProfileOnboardingResult(
                userProfileId = userProfileId,
                addictionTypeId = addictionTypeId,
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
                clinicalProfile = clinicalProfile.treatmentProfile.name,
                moralIncongruenceBand = clinicalProfile.incongruenceLevel?.name,
                createdAt = now,
            ),
        )
    }

    private suspend fun insertScaleAnswers(
        userProfileId: Int,
        answers: OnboardingAnswers,
        now: Long,
    ) {
        answers.ppcs6Answers.forEachIndexed { index, value ->
            onboardingRepository.insertScaleAnswer(
                OnboardingScaleAnswer(
                    userProfileId = userProfileId,
                    scaleType = ScaleType.PPCS_6.name,
                    questionIndex = index,
                    answerValue = value,
                    createdAt = now,
                ),
            )
        }
        answers.pgsiAnswers.forEachIndexed { index, value ->
            onboardingRepository.insertScaleAnswer(
                OnboardingScaleAnswer(
                    userProfileId = userProfileId,
                    scaleType = ScaleType.PGSI.name,
                    questionIndex = index,
                    answerValue = value,
                    createdAt = now,
                ),
            )
        }
        answers.emaAnswers.forEachIndexed { index, value ->
            onboardingRepository.insertScaleAnswer(
                OnboardingScaleAnswer(
                    userProfileId = userProfileId,
                    scaleType = ScaleType.EMA.name,
                    questionIndex = index,
                    answerValue = value,
                    createdAt = now,
                ),
            )
        }
        if (answers.frequencyAnswer > 0) {
            onboardingRepository.insertScaleAnswer(
                OnboardingScaleAnswer(
                    userProfileId = userProfileId,
                    scaleType = ScaleType.FREQUENCY.name,
                    questionIndex = 0,
                    answerValue = answers.frequencyAnswer,
                    createdAt = now,
                ),
            )
        }
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
