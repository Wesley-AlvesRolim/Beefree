package com.wesley.beefree.infrastructure.storage.ports

import com.wesley.beefree.domain.entities.AddictionKeyword
import com.wesley.beefree.domain.entities.AddictionType
import com.wesley.beefree.domain.entities.RelapseHistory
import kotlinx.coroutines.flow.Flow

interface AddictionRepository {
    suspend fun insertAddictionType(addictionType: AddictionType): Long

    suspend fun updateAddictionType(addictionType: AddictionType)

    suspend fun deleteAddictionType(addictionType: AddictionType)

    suspend fun getAddictionTypeById(id: Int): AddictionType?

    fun getAllAddictionTypes(): Flow<List<AddictionType>>

    suspend fun insertKeyword(keyword: AddictionKeyword): Long

    suspend fun updateKeyword(keyword: AddictionKeyword)

    suspend fun deleteKeyword(keyword: AddictionKeyword)

    fun getKeywordsByAddictionType(addictionTypeId: Int): Flow<List<AddictionKeyword>>

    suspend fun insertRelapse(relapse: RelapseHistory): Long

    fun getRelapseHistory(): Flow<List<RelapseHistory>>
}
