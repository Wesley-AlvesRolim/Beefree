package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.RelapseRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RelapseRecordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RelapseRecordEntity): Long

    @Query("SELECT * FROM RelapseRecord ORDER BY created_at DESC")
    fun getAll(): Flow<List<RelapseRecordEntity>>

    @Query("SELECT * FROM RelapseRecord WHERE id = :id")
    suspend fun getById(id: Int): RelapseRecordEntity?
}
