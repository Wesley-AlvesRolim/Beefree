package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.UserSupportContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSupportContactDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserSupportContactEntity): Long

    @Update
    suspend fun update(entity: UserSupportContactEntity)

    @Delete
    suspend fun delete(entity: UserSupportContactEntity)

    @Query("SELECT * FROM UserSupportContact WHERE user_profile_id = :userId")
    fun getAllByUser(userId: Int): Flow<List<UserSupportContactEntity>>

    @Query("SELECT * FROM UserSupportContact WHERE user_profile_id = :userId AND is_active = 1")
    fun getActiveByUser(userId: Int): Flow<List<UserSupportContactEntity>>
}
