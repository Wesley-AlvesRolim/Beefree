package com.wesley.beefree.domain.repository.ports

import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.domain.entities.UserHobby
import com.wesley.beefree.domain.entities.UserObjective
import com.wesley.beefree.domain.entities.UserOnboardingSession
import com.wesley.beefree.domain.entities.UserSymptom
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    suspend fun insertOnboardingSession(session: UserOnboardingSession): Long

    suspend fun getOnboardingSession(userId: Int): UserOnboardingSession?

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
