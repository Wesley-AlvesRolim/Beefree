package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.TriggerMappingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TriggerMappingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TriggerMappingEntity): Long

    @Update
    suspend fun update(entity: TriggerMappingEntity)

    @Query("SELECT * FROM TriggerMapping WHERE user_profile_id = :userId ORDER BY logged_at DESC")
    fun getAllByUser(userId: Int): Flow<List<TriggerMappingEntity>>
}
