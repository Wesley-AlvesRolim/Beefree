package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.MotivationalMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MotivationalMessageDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MotivationalMessageEntity): Long

    @Update
    suspend fun update(entity: MotivationalMessageEntity)

    @Delete
    suspend fun delete(entity: MotivationalMessageEntity)

    @Query("SELECT * FROM MotivationalMessages WHERE addiction_type_id = :addictionTypeId")
    fun getByAddictionTypeId(addictionTypeId: Int): Flow<List<MotivationalMessageEntity>>

    @Query("SELECT * FROM MotivationalMessages WHERE is_active = 1")
    fun getActiveMessages(): Flow<List<MotivationalMessageEntity>>
}
