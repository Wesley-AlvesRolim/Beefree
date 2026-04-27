package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.UserSupportContact
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.UserSupportContactDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import com.wesley.beefree.infrastructure.storage.ports.InterventionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomInterventionRepository(
    private val contactDao: UserSupportContactDAO,
) : InterventionRepository {
    override suspend fun insertContact(contact: UserSupportContact): Long = contactDao.insert(contact.toEntity())

    override suspend fun updateContact(contact: UserSupportContact) {
        contactDao.update(contact.toEntity())
    }

    override suspend fun deleteContact(contact: UserSupportContact) {
        contactDao.delete(contact.toEntity())
    }

    override fun getAllContacts(userId: Int): Flow<List<UserSupportContact>> =
        contactDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override fun getActiveContacts(userId: Int): Flow<List<UserSupportContact>> =
        contactDao.getActiveByUser(userId).map { list -> list.map { it.toDomain() } }
}
