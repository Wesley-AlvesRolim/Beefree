package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.RelapseHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RelapseHistoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RelapseHistoryEntity): Long

    @Query("SELECT * FROM RelapseHistory ORDER BY relapse_at DESC")
    fun getAll(): Flow<List<RelapseHistoryEntity>>

    @Query("SELECT * FROM RelapseHistory WHERE id = :id")
    suspend fun getById(id: Int): RelapseHistoryEntity?
}
