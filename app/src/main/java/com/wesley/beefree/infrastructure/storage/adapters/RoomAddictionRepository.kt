package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.AddictionCategory
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.AddictionCategoryDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.RelapseRecordDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import com.wesley.beefree.infrastructure.storage.ports.AddictionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomAddictionRepository(
    private val addictionCategoryDao: AddictionCategoryDAO,
    private val relapseRecordDao: RelapseRecordDAO,
) : AddictionRepository {
    override suspend fun insertAddictionCategory(category: AddictionCategory): Long = addictionCategoryDao.insert(category.toEntity())

    override suspend fun updateAddictionCategory(category: AddictionCategory) {
        addictionCategoryDao.update(category.toEntity())
    }

    override suspend fun deleteAddictionCategory(category: AddictionCategory) {
        addictionCategoryDao.delete(category.toEntity())
    }

    override suspend fun getAddictionCategoryById(id: Int): AddictionCategory? = addictionCategoryDao.getById(id)?.toDomain()

    override fun getAllAddictionCategories(): Flow<List<AddictionCategory>> =
        addictionCategoryDao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun insertRelapse(relapse: RelapseRecord): Long = relapseRecordDao.insert(relapse.toEntity())

    override fun getRelapseHistory(): Flow<List<RelapseRecord>> = relapseRecordDao.getAll().map { list -> list.map { it.toDomain() } }
}
