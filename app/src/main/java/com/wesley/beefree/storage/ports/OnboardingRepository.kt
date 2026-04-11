package com.wesley.beefree.storage.ports

import com.wesley.beefree.domain.entities.OnboardingScaleAnswer
import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.domain.entities.UserHobby
import com.wesley.beefree.domain.entities.UserObjective
import com.wesley.beefree.domain.entities.UserProfileOnboardingResult
import com.wesley.beefree.domain.entities.UserSymptom
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    suspend fun insertOnboardingResult(result: UserProfileOnboardingResult): Long

    suspend fun getOnboardingResult(userId: Int): UserProfileOnboardingResult?

    suspend fun insertScaleAnswer(answer: OnboardingScaleAnswer): Long

    fun getScaleAnswers(
        userId: Int,
        scaleType: String,
    ): Flow<List<OnboardingScaleAnswer>>

    suspend fun insertCoreValue(value: UserCoreValue): Long

    suspend fun deleteCoreValue(value: UserCoreValue)

    fun getCoreValues(userId: Int): Flow<List<UserCoreValue>>

    suspend fun insertHobby(hobby: UserHobby): Long

    suspend fun deleteHobby(hobby: UserHobby)

    fun getHobbies(userId: Int): Flow<List<UserHobby>>

    suspend fun insertObjective(objective: UserObjective): Long

    suspend fun deleteObjective(objective: UserObjective)

    fun getObjectives(userId: Int): Flow<List<UserObjective>>

    suspend fun insertSymptom(symptom: UserSymptom): Long

    suspend fun deleteSymptom(symptom: UserSymptom)

    fun getSymptoms(userId: Int): Flow<List<UserSymptom>>
}
