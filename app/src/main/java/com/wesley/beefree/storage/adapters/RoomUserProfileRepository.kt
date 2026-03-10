package com.wesley.beefree.storage.adapters

import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.entities.UserProfileAddiction
import com.wesley.beefree.storage.adapters.db.dao.UserProfileAddictionDAO
import com.wesley.beefree.storage.adapters.db.dao.UserProfileDAO
import com.wesley.beefree.storage.adapters.db.toDomain
import com.wesley.beefree.storage.adapters.db.toEntity
import com.wesley.beefree.storage.ports.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomUserProfileRepository(
    private val userProfileDao: UserProfileDAO,
    private val userProfileAddictionDao: UserProfileAddictionDAO,
) : UserProfileRepository {
    override suspend fun insertProfile(profile: UserProfile): Long = userProfileDao.insert(profile.toEntity())

    override suspend fun updateProfile(profile: UserProfile) {
        userProfileDao.update(profile.toEntity())
    }

    override suspend fun getProfileById(id: Int): UserProfile? = userProfileDao.getById(id)?.toDomain()

    override fun getAllProfiles(): Flow<List<UserProfile>> = userProfileDao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun associateAddictionToProfile(userProfileAddiction: UserProfileAddiction): Long =
        userProfileAddictionDao.insert(userProfileAddiction.toEntity())

    override suspend fun removeAddictionFromProfile(userProfileAddiction: UserProfileAddiction) {
        userProfileAddictionDao.delete(userProfileAddiction.toEntity())
    }

    override fun getAddictionsByUserId(userId: Int): Flow<List<UserProfileAddiction>> =
        userProfileAddictionDao.getByUserId(userId).map { list ->
            list.map {
                it.toDomain()
            }
        }
}
