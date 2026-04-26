package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.DailyCheckInEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyCheckInDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DailyCheckInEntity): Long

    @Query("SELECT * FROM DailyCheckIn WHERE user_profile_id = :userId ORDER BY checked_in_at DESC")
    fun getAllByUser(userId: Int): Flow<List<DailyCheckInEntity>>
}
