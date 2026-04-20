package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.InterventionLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InterventionLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: InterventionLogEntity): Long

    @Update
    suspend fun update(entity: InterventionLogEntity)

    @Query("SELECT * FROM InterventionLogs WHERE user_profile_id = :userId ORDER BY created_at DESC")
    fun getAllByUser(userId: Int): Flow<List<InterventionLogEntity>>
}
