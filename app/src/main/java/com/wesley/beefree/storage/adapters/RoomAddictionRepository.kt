package com.wesley.beefree.storage.adapters

import com.wesley.beefree.domain.entities.AddictionKeyword
import com.wesley.beefree.domain.entities.AddictionType
import com.wesley.beefree.domain.entities.RelapseHistory
import com.wesley.beefree.storage.adapters.db.dao.*
import com.wesley.beefree.storage.adapters.db.toDomain
import com.wesley.beefree.storage.adapters.db.toEntity
import com.wesley.beefree.storage.ports.AddictionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomAddictionRepository(
    private val addictionTypeDao: AddictionTypeDAO,
    private val keywordDao: AddictionKeywordDAO,
    private val relapseHistoryDao: RelapseHistoryDAO,
) : AddictionRepository {
    override suspend fun insertAddictionType(addictionType: AddictionType): Long = addictionTypeDao.insert(addictionType.toEntity())

    override suspend fun updateAddictionType(addictionType: AddictionType) {
        addictionTypeDao.update(addictionType.toEntity())
    }

    override suspend fun deleteAddictionType(addictionType: AddictionType) {
        addictionTypeDao.delete(addictionType.toEntity())
    }

    override suspend fun getAddictionTypeById(id: Int): AddictionType? = addictionTypeDao.getById(id)?.toDomain()

    override fun getAllAddictionTypes(): Flow<List<AddictionType>> = addictionTypeDao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun insertKeyword(keyword: AddictionKeyword): Long = keywordDao.insert(keyword.toEntity())

    override suspend fun updateKeyword(keyword: AddictionKeyword) {
        keywordDao.update(keyword.toEntity())
    }

    override suspend fun deleteKeyword(keyword: AddictionKeyword) {
        keywordDao.delete(keyword.toEntity())
    }

    override fun getKeywordsByAddictionType(addictionTypeId: Int): Flow<List<AddictionKeyword>> =
        keywordDao.getByAddictionTypeId(addictionTypeId).map { list ->
            list.map {
                it.toDomain()
            }
        }

    override suspend fun insertRelapse(relapse: RelapseHistory): Long = relapseHistoryDao.insert(relapse.toEntity())

    override fun getRelapseHistory(): Flow<List<RelapseHistory>> = relapseHistoryDao.getAll().map { list -> list.map { it.toDomain() } }
}
