package com.wesley.beefree.infrastructure.storage.ports

import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.entities.UserProfileAddiction
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    suspend fun insertProfile(profile: UserProfile): Long

    suspend fun updateProfile(profile: UserProfile)

    suspend fun getProfileById(id: Int): UserProfile?

    fun getAllProfiles(): Flow<List<UserProfile>>

    suspend fun associateAddictionToProfile(userProfileAddiction: UserProfileAddiction): Long

    suspend fun removeAddictionFromProfile(userProfileAddiction: UserProfileAddiction)

    fun getAddictionsByUserId(userId: Int): Flow<List<UserProfileAddiction>>
}
