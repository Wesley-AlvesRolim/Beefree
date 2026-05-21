package com.wesley.beefree.domain.repository.ports

import com.wesley.beefree.domain.entities.AddictionCategory
import com.wesley.beefree.domain.entities.RelapseRecord
import kotlinx.coroutines.flow.Flow

interface AddictionRepository {
    suspend fun insertAddictionCategory(category: AddictionCategory): Long

    suspend fun updateAddictionCategory(category: AddictionCategory)

    suspend fun deleteAddictionCategory(category: AddictionCategory)

    suspend fun getAddictionCategoryById(id: Int): AddictionCategory?

    fun getAllAddictionCategories(): Flow<List<AddictionCategory>>

    suspend fun insertRelapse(relapse: RelapseRecord): Long

    fun getRelapseHistory(): Flow<List<RelapseRecord>>
}
