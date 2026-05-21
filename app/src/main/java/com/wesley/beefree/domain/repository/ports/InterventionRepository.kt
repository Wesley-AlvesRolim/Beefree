package com.wesley.beefree.domain.repository.ports

import com.wesley.beefree.domain.entities.UserSupportContact
import kotlinx.coroutines.flow.Flow

interface InterventionRepository {
    suspend fun insertContact(contact: UserSupportContact): Long

    suspend fun updateContact(contact: UserSupportContact)

    suspend fun deleteContact(contact: UserSupportContact)

    fun getAllContacts(userId: Int): Flow<List<UserSupportContact>>

    fun getActiveContacts(userId: Int): Flow<List<UserSupportContact>>
}
