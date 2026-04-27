package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.AddictionCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddictionCategoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AddictionCategoryEntity): Long

    @Update
    suspend fun update(entity: AddictionCategoryEntity)

    @Delete
    suspend fun delete(entity: AddictionCategoryEntity)

    @Query("SELECT * FROM AddictionCategory WHERE id = :id")
    suspend fun getById(id: Int): AddictionCategoryEntity?

    @Query("SELECT * FROM AddictionCategory")
    fun getAll(): Flow<List<AddictionCategoryEntity>>
}
