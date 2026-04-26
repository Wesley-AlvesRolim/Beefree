package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.InterventionLog
import com.wesley.beefree.domain.entities.ThoughtRecord
import com.wesley.beefree.domain.entities.TriggerMapping
import com.wesley.beefree.domain.entities.UrgeSurfingSession
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.InterventionLogDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.ThoughtRecordDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.TriggerMappingDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.UrgeSurfingSessionDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import com.wesley.beefree.infrastructure.storage.ports.EMIRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomEMIRepository(
    private val triggerMappingDao: TriggerMappingDAO,
    private val interventionLogDao: InterventionLogDAO,
    private val thoughtRecordDao: ThoughtRecordDAO,
    private val urgeSurfingSessionDao: UrgeSurfingSessionDAO,
) : EMIRepository {
    override suspend fun insertTrigger(trigger: TriggerMapping): Long = triggerMappingDao.insert(trigger.toEntity())

    override suspend fun updateTrigger(trigger: TriggerMapping) {
        triggerMappingDao.update(trigger.toEntity())
    }

    override fun getTriggers(userId: Int): Flow<List<TriggerMapping>> =
        triggerMappingDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertInterventionLog(log: InterventionLog): Long = interventionLogDao.insert(log.toEntity())

    override suspend fun updateInterventionLog(log: InterventionLog) {
        interventionLogDao.update(log.toEntity())
    }

    override fun getInterventionLogs(userId: Int): Flow<List<InterventionLog>> =
        interventionLogDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertThoughtRecord(record: ThoughtRecord): Long = thoughtRecordDao.insert(record.toEntity())

    override suspend fun updateThoughtRecord(record: ThoughtRecord) {
        thoughtRecordDao.update(record.toEntity())
    }

    override fun getThoughtRecords(userId: Int): Flow<List<ThoughtRecord>> =
        thoughtRecordDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertUrgeSurfingSession(session: UrgeSurfingSession): Long = urgeSurfingSessionDao.insert(session.toEntity())

    override suspend fun updateUrgeSurfingSession(session: UrgeSurfingSession) {
        urgeSurfingSessionDao.update(session.toEntity())
    }

    override fun getUrgeSurfingSessions(userId: Int): Flow<List<UrgeSurfingSession>> =
        urgeSurfingSessionDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }
}
