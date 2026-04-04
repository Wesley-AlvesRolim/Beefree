package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.UrgeSurfingSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UrgeSurfingSessionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UrgeSurfingSessionEntity): Long

    @Update
    suspend fun update(entity: UrgeSurfingSessionEntity)

    @Query("SELECT * FROM UrgeSurfingSession WHERE user_profile_id = :userId ORDER BY logged_at DESC")
    fun getAllByUser(userId: Int): Flow<List<UrgeSurfingSessionEntity>>
}
