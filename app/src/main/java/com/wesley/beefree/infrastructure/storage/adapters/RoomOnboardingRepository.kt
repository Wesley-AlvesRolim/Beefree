package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.domain.entities.UserHobby
import com.wesley.beefree.domain.entities.UserObjective
import com.wesley.beefree.domain.entities.UserOnboardingSession
import com.wesley.beefree.domain.entities.UserSymptom
import com.wesley.beefree.domain.repository.ports.OnboardingRepository
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.UserCoreValueDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.UserHobbyDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.UserObjectiveDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.UserOnboardingSessionDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.UserSymptomDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomOnboardingRepository(
    private val onboardingSessionDao: UserOnboardingSessionDAO,
    private val coreValueDao: UserCoreValueDAO,
    private val hobbyDao: UserHobbyDAO,
    private val objectiveDao: UserObjectiveDAO,
    private val symptomDao: UserSymptomDAO,
) : OnboardingRepository {
    override suspend fun insertOnboardingSession(session: UserOnboardingSession): Long = onboardingSessionDao.insert(session.toEntity())

    override suspend fun getOnboardingSession(userId: Int): UserOnboardingSession? = onboardingSessionDao.getByUser(userId)?.toDomain()

    override suspend fun insertCoreValue(value: UserCoreValue): Long = coreValueDao.insert(value.toEntity())

    override suspend fun deleteCoreValue(value: UserCoreValue) {
        coreValueDao.delete(value.toEntity())
    }

    override fun getCoreValues(userId: Int): Flow<List<UserCoreValue>> =
        coreValueDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertHobby(hobby: UserHobby): Long = hobbyDao.insert(hobby.toEntity())

    override suspend fun deleteHobby(hobby: UserHobby) {
        hobbyDao.delete(hobby.toEntity())
    }

    override fun getHobbies(userId: Int): Flow<List<UserHobby>> = hobbyDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertObjective(objective: UserObjective): Long = objectiveDao.insert(objective.toEntity())

    override suspend fun deleteObjective(objective: UserObjective) {
        objectiveDao.delete(objective.toEntity())
    }

    override fun getObjectives(userId: Int): Flow<List<UserObjective>> =
        objectiveDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertSymptom(symptom: UserSymptom): Long = symptomDao.insert(symptom.toEntity())

    override suspend fun deleteSymptom(symptom: UserSymptom) {
        symptomDao.delete(symptom.toEntity())
    }

    override fun getSymptoms(userId: Int): Flow<List<UserSymptom>> =
        symptomDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }
}
