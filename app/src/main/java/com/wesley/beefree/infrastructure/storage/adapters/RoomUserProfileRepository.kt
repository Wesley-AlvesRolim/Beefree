package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.UserAddiction
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.UserAddictionDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.UserProfileDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import com.wesley.beefree.infrastructure.storage.ports.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomUserProfileRepository(
    private val userProfileDao: UserProfileDAO,
    private val userAddictionDao: UserAddictionDAO,
) : UserProfileRepository {
    override suspend fun insertProfile(profile: UserProfile): Long = userProfileDao.insert(profile.toEntity())

    override suspend fun updateProfile(profile: UserProfile) {
        userProfileDao.update(profile.toEntity())
    }

    override suspend fun getProfileById(id: Int): UserProfile? = userProfileDao.getById(id)?.toDomain()

    override fun getAllProfiles(): Flow<List<UserProfile>> = userProfileDao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun associateAddictionToProfile(userAddiction: UserAddiction): Long = userAddictionDao.insert(userAddiction.toEntity())

    override suspend fun removeAddictionFromProfile(userAddiction: UserAddiction) {
        userAddictionDao.delete(userAddiction.toEntity())
    }

    override fun getAddictionsByUserId(userId: Int): Flow<List<UserAddiction>> =
        userAddictionDao.getByUserId(userId).map { list -> list.map { it.toDomain() } }
}
