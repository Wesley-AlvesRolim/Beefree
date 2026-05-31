package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.EmotionRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmotionRecordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: EmotionRecordEntity): Long

    @Query("SELECT * FROM EmotionRecord WHERE user_profile_id = :userId ORDER BY created_at DESC")
    fun getAllByUser(userId: Int): Flow<List<EmotionRecordEntity>>

    @Query("SELECT * FROM EmotionRecord WHERE user_profile_id = :userId ORDER BY created_at DESC LIMIT 1")
    suspend fun getLatestByUser(userId: Int): EmotionRecordEntity?

    @Query("SELECT * FROM EmotionRecord WHERE user_profile_id = :userId AND feeling_type = :type ORDER BY created_at DESC")
    fun getByUserAndType(
        userId: Int,
        type: String,
    ): Flow<List<EmotionRecordEntity>>

    @Query("DELETE FROM EmotionRecord WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>)
}
