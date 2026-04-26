package com.wesley.beefree.infrastructure.storage.ports

import com.wesley.beefree.domain.entities.BlockScreenConfig
import com.wesley.beefree.domain.entities.MotivationalMessage
import com.wesley.beefree.domain.entities.SupportContact
import kotlinx.coroutines.flow.Flow

interface InterventionRepository {
    suspend fun insertContact(contact: SupportContact): Long

    suspend fun updateContact(contact: SupportContact)

    suspend fun deleteContact(contact: SupportContact)

    fun getAllContacts(): Flow<List<SupportContact>>

    fun getActiveContacts(): Flow<List<SupportContact>>

    suspend fun insertBlockScreenConfig(config: BlockScreenConfig): Long

    suspend fun updateBlockScreenConfig(config: BlockScreenConfig)

    suspend fun getBlockScreenConfigByAddictionType(addictionTypeId: Int): BlockScreenConfig?

    suspend fun insertMessage(message: MotivationalMessage): Long

    suspend fun updateMessage(message: MotivationalMessage)

    suspend fun deleteMessage(message: MotivationalMessage)

    fun getMessagesByAddictionType(addictionTypeId: Int): Flow<List<MotivationalMessage>>

    fun getActiveMessages(): Flow<List<MotivationalMessage>>
}
