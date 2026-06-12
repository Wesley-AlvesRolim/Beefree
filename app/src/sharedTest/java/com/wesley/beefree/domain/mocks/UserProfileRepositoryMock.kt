package com.wesley.beefree.domain.mocks

import com.wesley.beefree.domain.entities.UserAddiction
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserProfileRepositoryMock : UserProfileRepository {
    var profiles: List<UserProfile> = emptyList()
    var profileById: UserProfile? = null
    var addictionsByUser: List<UserAddiction> = emptyList()

    val insertProfileArgs = mutableListOf<UserProfile>()
    val updateProfileArgs = mutableListOf<UserProfile>()
    val associateAddictionArgs = mutableListOf<UserAddiction>()
    val removeAddictionArgs = mutableListOf<UserAddiction>()

    override suspend fun insertProfile(profile: UserProfile): Long {
        insertProfileArgs += profile
        return 0L
    }

    override suspend fun updateProfile(profile: UserProfile) {
        updateProfileArgs += profile
    }

    override suspend fun getProfileById(id: Int): UserProfile? = profileById

    override fun getAllProfiles(): Flow<List<UserProfile>> = flowOf(profiles)

    override suspend fun associateAddictionToProfile(userAddiction: UserAddiction): Long {
        associateAddictionArgs += userAddiction
        return 0L
    }

    override suspend fun removeAddictionFromProfile(userAddiction: UserAddiction) {
        removeAddictionArgs += userAddiction
    }

    override fun getAddictionsByUserId(userId: Int): Flow<List<UserAddiction>> = flowOf(addictionsByUser)
}
