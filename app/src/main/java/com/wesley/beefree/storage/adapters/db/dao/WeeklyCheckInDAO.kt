package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.WeeklyCheckInEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeeklyCheckInDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WeeklyCheckInEntity): Long

    @Query("SELECT * FROM WeeklyCheckIn WHERE user_profile_id = :userId ORDER BY week_start_date DESC")
    fun getAllByUser(userId: Int): Flow<List<WeeklyCheckInEntity>>
}
