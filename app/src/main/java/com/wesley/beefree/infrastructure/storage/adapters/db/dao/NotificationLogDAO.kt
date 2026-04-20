package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.NotificationLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: NotificationLogEntity): Long

    @Update
    suspend fun update(entity: NotificationLogEntity)

    @Query("SELECT * FROM NotificationLog WHERE user_profile_id = :userId ORDER BY sent_at DESC")
    fun getAllByUser(userId: Int): Flow<List<NotificationLogEntity>>
}
