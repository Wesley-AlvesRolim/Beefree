package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.BlockScreenConfig
import com.wesley.beefree.domain.entities.MotivationalMessage
import com.wesley.beefree.domain.entities.SupportContact
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.BlockScreenConfigDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.MotivationalMessageDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.SupportContactDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import com.wesley.beefree.infrastructure.storage.ports.InterventionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomInterventionRepository(
    private val contactDao: SupportContactDAO,
    private val configDao: BlockScreenConfigDAO,
    private val messageDao: MotivationalMessageDAO,
) : InterventionRepository {
    override suspend fun insertContact(contact: SupportContact): Long = contactDao.insert(contact.toEntity())

    override suspend fun updateContact(contact: SupportContact) {
        contactDao.update(contact.toEntity())
    }

    override suspend fun deleteContact(contact: SupportContact) {
        contactDao.delete(contact.toEntity())
    }

    override fun getAllContacts(): Flow<List<SupportContact>> = contactDao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getActiveContacts(): Flow<List<SupportContact>> = contactDao.getActiveContacts().map { list -> list.map { it.toDomain() } }

    override suspend fun insertBlockScreenConfig(config: BlockScreenConfig): Long = configDao.insert(config.toEntity())

    override suspend fun updateBlockScreenConfig(config: BlockScreenConfig) {
        configDao.update(config.toEntity())
    }

    override suspend fun getBlockScreenConfigByAddictionType(addictionTypeId: Int): BlockScreenConfig? =
        configDao.getByAddictionTypeId(addictionTypeId)?.toDomain()

    override suspend fun insertMessage(message: MotivationalMessage): Long = messageDao.insert(message.toEntity())

    override suspend fun updateMessage(message: MotivationalMessage) {
        messageDao.update(message.toEntity())
    }

    override suspend fun deleteMessage(message: MotivationalMessage) {
        messageDao.delete(message.toEntity())
    }

    override fun getMessagesByAddictionType(addictionTypeId: Int): Flow<List<MotivationalMessage>> =
        messageDao.getByAddictionTypeId(addictionTypeId).map { list ->
            list.map {
                it.toDomain()
            }
        }

    override fun getActiveMessages(): Flow<List<MotivationalMessage>> =
        messageDao.getActiveMessages().map { list ->
            list.map {
                it.toDomain()
            }
        }
}
