package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.ThoughtRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThoughtRecordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ThoughtRecordEntity): Long

    @Update
    suspend fun update(entity: ThoughtRecordEntity)

    @Query("SELECT * FROM ThoughtRecords WHERE user_profile_id = :userId ORDER BY created_at DESC")
    fun getAllByUser(userId: Int): Flow<List<ThoughtRecordEntity>>
}
