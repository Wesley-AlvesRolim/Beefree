package com.wesley.beefree.domain.repository.ports

import com.wesley.beefree.domain.entities.UserAddiction
import com.wesley.beefree.domain.entities.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    suspend fun insertProfile(profile: UserProfile): Long

    suspend fun updateProfile(profile: UserProfile)

    suspend fun getProfileById(id: Int): UserProfile?

    fun getAllProfiles(): Flow<List<UserProfile>>

    suspend fun associateAddictionToProfile(userAddiction: UserAddiction): Long

    suspend fun removeAddictionFromProfile(userAddiction: UserAddiction)

    fun getAddictionsByUserId(userId: Int): Flow<List<UserAddiction>>
}
