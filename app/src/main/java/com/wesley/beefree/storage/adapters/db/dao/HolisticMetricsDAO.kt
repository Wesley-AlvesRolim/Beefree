package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.HolisticMetricsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HolisticMetricsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: HolisticMetricsEntity): Long

    @Query("SELECT * FROM HolisticMetrics WHERE user_profile_id = :userId ORDER BY logged_at DESC")
    fun getAllByUser(userId: Int): Flow<List<HolisticMetricsEntity>>
}
