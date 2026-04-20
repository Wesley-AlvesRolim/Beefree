package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.UserCoreValueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCoreValueDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserCoreValueEntity): Long

    @Delete
    suspend fun delete(entity: UserCoreValueEntity)

    @Query("SELECT * FROM UserCoreValues WHERE user_profile_id = :userId")
    fun getAllByUser(userId: Int): Flow<List<UserCoreValueEntity>>
}
