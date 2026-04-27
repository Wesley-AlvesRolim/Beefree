package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.InterventionRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InterventionRecordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: InterventionRecordEntity): Long

    @Update
    suspend fun update(entity: InterventionRecordEntity)

    @Query("SELECT * FROM InterventionRecord WHERE user_profile_id = :userId ORDER BY created_at DESC")
    fun getAllByUser(userId: Int): Flow<List<InterventionRecordEntity>>
}
