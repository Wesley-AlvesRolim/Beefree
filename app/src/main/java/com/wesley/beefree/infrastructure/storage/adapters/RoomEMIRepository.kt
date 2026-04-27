package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.CognitiveThoughtRecord
import com.wesley.beefree.domain.entities.InterventionRecord
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.CognitiveThoughtRecordDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.InterventionRecordDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import com.wesley.beefree.infrastructure.storage.ports.EMIRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomEMIRepository(
    private val interventionRecordDao: InterventionRecordDAO,
    private val thoughtRecordDao: CognitiveThoughtRecordDAO,
) : EMIRepository {
    override suspend fun insertInterventionRecord(record: InterventionRecord): Long = interventionRecordDao.insert(record.toEntity())

    override suspend fun updateInterventionRecord(record: InterventionRecord) {
        interventionRecordDao.update(record.toEntity())
    }

    override fun getInterventionRecords(userId: Int): Flow<List<InterventionRecord>> =
        interventionRecordDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertThoughtRecord(record: CognitiveThoughtRecord): Long = thoughtRecordDao.insert(record.toEntity())

    override suspend fun updateThoughtRecord(record: CognitiveThoughtRecord) {
        thoughtRecordDao.update(record.toEntity())
    }

    override fun getThoughtRecords(userId: Int): Flow<List<CognitiveThoughtRecord>> =
        thoughtRecordDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }
}
