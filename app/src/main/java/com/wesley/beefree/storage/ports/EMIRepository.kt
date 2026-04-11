package com.wesley.beefree.storage.ports

import com.wesley.beefree.domain.entities.InterventionLog
import com.wesley.beefree.domain.entities.ThoughtRecord
import com.wesley.beefree.domain.entities.TriggerMapping
import com.wesley.beefree.domain.entities.UrgeSurfingSession
import kotlinx.coroutines.flow.Flow

interface EMIRepository {
    suspend fun insertTrigger(trigger: TriggerMapping): Long

    suspend fun updateTrigger(trigger: TriggerMapping)

    fun getTriggers(userId: Int): Flow<List<TriggerMapping>>

    suspend fun insertInterventionLog(log: InterventionLog): Long

    suspend fun updateInterventionLog(log: InterventionLog)

    fun getInterventionLogs(userId: Int): Flow<List<InterventionLog>>

    suspend fun insertThoughtRecord(record: ThoughtRecord): Long

    suspend fun updateThoughtRecord(record: ThoughtRecord)

    fun getThoughtRecords(userId: Int): Flow<List<ThoughtRecord>>

    suspend fun insertUrgeSurfingSession(session: UrgeSurfingSession): Long

    suspend fun updateUrgeSurfingSession(session: UrgeSurfingSession)

    fun getUrgeSurfingSessions(userId: Int): Flow<List<UrgeSurfingSession>>
}
