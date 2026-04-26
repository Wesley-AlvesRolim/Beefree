package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.CognitiveThoughtRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CognitiveThoughtRecordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CognitiveThoughtRecordEntity): Long

    @Update
    suspend fun update(entity: CognitiveThoughtRecordEntity)

    @Query("SELECT * FROM CognitiveThoughtRecord WHERE user_profile_id = :userId ORDER BY created_at DESC")
    fun getAllByUser(userId: Int): Flow<List<CognitiveThoughtRecordEntity>>
}
