package com.wesley.beefree.domain.repository.ports

import com.wesley.beefree.domain.entities.CognitiveThoughtRecord
import com.wesley.beefree.domain.entities.InterventionRecord
import com.wesley.beefree.domain.entities.InterventionValueLink
import kotlinx.coroutines.flow.Flow

interface EMIRepository {
    suspend fun insertInterventionRecord(record: InterventionRecord): Long

    suspend fun updateInterventionRecord(record: InterventionRecord)

    fun getInterventionRecords(userId: Int): Flow<List<InterventionRecord>>

    suspend fun insertThoughtRecord(record: CognitiveThoughtRecord): Long

    suspend fun updateThoughtRecord(record: CognitiveThoughtRecord)

    fun getThoughtRecords(userId: Int): Flow<List<CognitiveThoughtRecord>>

    suspend fun insertInterventionValueLink(link: InterventionValueLink)
}
