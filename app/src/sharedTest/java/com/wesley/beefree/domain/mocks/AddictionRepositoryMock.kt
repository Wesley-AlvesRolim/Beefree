package com.wesley.beefree.domain.mocks

import com.wesley.beefree.domain.entities.AddictionCategory
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.repository.ports.AddictionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class AddictionRepositoryMock : AddictionRepository {
    val categories = MutableStateFlow<List<AddictionCategory>>(emptyList())
    var relapseHistory: List<RelapseRecord> = emptyList()
    var categoryById: AddictionCategory? = null

    val insertAddictionCategoryArgs = mutableListOf<AddictionCategory>()
    val updateAddictionCategoryArgs = mutableListOf<AddictionCategory>()
    val deleteAddictionCategoryArgs = mutableListOf<AddictionCategory>()
    val insertRelapseArgs = mutableListOf<RelapseRecord>()

    val updateCalls get() = updateAddictionCategoryArgs.size

    override suspend fun insertAddictionCategory(category: AddictionCategory): Long {
        insertAddictionCategoryArgs += category
        return 0L
    }

    override suspend fun updateAddictionCategory(category: AddictionCategory) {
        updateAddictionCategoryArgs += category
        categories.value = categories.value.map { if (it.name == category.name) category else it }
    }

    override suspend fun deleteAddictionCategory(category: AddictionCategory) {
        deleteAddictionCategoryArgs += category
    }

    override suspend fun getAddictionCategoryById(id: Int): AddictionCategory? = categoryById

    override fun getAllAddictionCategories(): Flow<List<AddictionCategory>> = categories

    override suspend fun insertRelapse(relapse: RelapseRecord): Long {
        insertRelapseArgs += relapse
        return 0L
    }

    override fun getRelapseHistory(): Flow<List<RelapseRecord>> = flowOf(relapseHistory)
}
