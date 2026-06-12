package com.wesley.beefree.domain.mocks

import com.wesley.beefree.domain.entities.CognitiveThoughtRecord
import com.wesley.beefree.domain.entities.InterventionRecord
import com.wesley.beefree.domain.entities.InterventionValueLink
import com.wesley.beefree.domain.repository.ports.EMIRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EMIRepositoryMock : EMIRepository {
    var interventionRecords: List<InterventionRecord> = emptyList()
    var thoughtRecords: List<CognitiveThoughtRecord> = emptyList()

    val insertInterventionRecordArgs = mutableListOf<InterventionRecord>()
    val updateInterventionRecordArgs = mutableListOf<InterventionRecord>()
    val insertThoughtRecordArgs = mutableListOf<CognitiveThoughtRecord>()
    val updateThoughtRecordArgs = mutableListOf<CognitiveThoughtRecord>()
    val insertInterventionValueLinkArgs = mutableListOf<InterventionValueLink>()

    override suspend fun insertInterventionRecord(record: InterventionRecord): Long {
        insertInterventionRecordArgs += record
        return insertInterventionRecordArgs.size.toLong()
    }

    override suspend fun updateInterventionRecord(record: InterventionRecord) {
        updateInterventionRecordArgs += record
    }

    override fun getInterventionRecords(userId: Int): Flow<List<InterventionRecord>> = flowOf(interventionRecords)

    override suspend fun insertThoughtRecord(record: CognitiveThoughtRecord): Long {
        insertThoughtRecordArgs += record
        return insertThoughtRecordArgs.size.toLong()
    }

    override suspend fun updateThoughtRecord(record: CognitiveThoughtRecord) {
        updateThoughtRecordArgs += record
    }

    override fun getThoughtRecords(userId: Int): Flow<List<CognitiveThoughtRecord>> = flowOf(thoughtRecords)

    override suspend fun insertInterventionValueLink(link: InterventionValueLink) {
        insertInterventionValueLinkArgs += link
    }
}
