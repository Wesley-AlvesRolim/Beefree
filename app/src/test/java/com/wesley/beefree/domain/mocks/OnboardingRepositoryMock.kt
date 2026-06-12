package com.wesley.beefree.domain.mocks

import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.domain.entities.UserHobby
import com.wesley.beefree.domain.entities.UserObjective
import com.wesley.beefree.domain.entities.UserOnboardingSession
import com.wesley.beefree.domain.entities.UserSymptom
import com.wesley.beefree.domain.repository.ports.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class OnboardingRepositoryMock : OnboardingRepository {
    var session: UserOnboardingSession? = null
    var coreValues: List<UserCoreValue> = emptyList()
    var hobbies: List<UserHobby> = emptyList()
    var objectives: List<UserObjective> = emptyList()
    var symptoms: List<UserSymptom> = emptyList()

    val insertOnboardingSessionArgs = mutableListOf<UserOnboardingSession>()
    val insertCoreValueArgs = mutableListOf<UserCoreValue>()
    val insertHobbyArgs = mutableListOf<UserHobby>()
    val insertObjectiveArgs = mutableListOf<UserObjective>()
    val insertSymptomArgs = mutableListOf<UserSymptom>()

    override suspend fun insertOnboardingSession(session: UserOnboardingSession): Long {
        insertOnboardingSessionArgs += session
        return 0L
    }

    override suspend fun getOnboardingSession(userId: Int): UserOnboardingSession? = session

    override suspend fun insertCoreValue(value: UserCoreValue): Long {
        insertCoreValueArgs += value
        return 0L
    }

    override suspend fun deleteCoreValue(value: UserCoreValue) = Unit

    override fun getCoreValues(userId: Int): Flow<List<UserCoreValue>> = flowOf(coreValues)

    override suspend fun insertHobby(hobby: UserHobby): Long {
        insertHobbyArgs += hobby
        return 0L
    }

    override suspend fun deleteHobby(hobby: UserHobby) = Unit

    override fun getHobbies(userId: Int): Flow<List<UserHobby>> = flowOf(hobbies)

    override suspend fun insertObjective(objective: UserObjective): Long {
        insertObjectiveArgs += objective
        return 0L
    }

    override suspend fun deleteObjective(objective: UserObjective) = Unit

    override fun getObjectives(userId: Int): Flow<List<UserObjective>> = flowOf(objectives)

    override suspend fun insertSymptom(symptom: UserSymptom): Long {
        insertSymptomArgs += symptom
        return 0L
    }

    override suspend fun deleteSymptom(symptom: UserSymptom) = Unit

    override fun getSymptoms(userId: Int): Flow<List<UserSymptom>> = flowOf(symptoms)
}
