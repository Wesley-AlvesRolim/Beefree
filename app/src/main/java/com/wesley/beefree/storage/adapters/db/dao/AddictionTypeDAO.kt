package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.AddictionTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddictionTypeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AddictionTypeEntity): Long

    @Update
    suspend fun update(entity: AddictionTypeEntity)

    @Delete
    suspend fun delete(entity: AddictionTypeEntity)

    @Query("SELECT * FROM AddictionTypes WHERE id = :id")
    suspend fun getById(id: Int): AddictionTypeEntity?

    @Query("SELECT * FROM AddictionTypes")
    fun getAll(): Flow<List<AddictionTypeEntity>>
}
