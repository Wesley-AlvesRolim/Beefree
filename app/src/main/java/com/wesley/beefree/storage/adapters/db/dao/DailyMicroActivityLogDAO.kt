package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.DailyMicroActivityLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyMicroActivityLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DailyMicroActivityLogEntity): Long

    @Query("SELECT * FROM DailyMicroActivityLog WHERE user_profile_id = :userId ORDER BY completed_at DESC")
    fun getAllByUser(userId: Int): Flow<List<DailyMicroActivityLogEntity>>

    @Query("SELECT * FROM DailyMicroActivityLog WHERE user_profile_id = :userId AND day_date = :dayDate ORDER BY completed_at DESC")
    fun getAllByUserAndDay(
        userId: Int,
        dayDate: Long,
    ): Flow<List<DailyMicroActivityLogEntity>>
}
