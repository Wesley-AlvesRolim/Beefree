package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.UserProfileAddictionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileAddictionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserProfileAddictionEntity): Long

    @Delete
    suspend fun delete(entity: UserProfileAddictionEntity)

    @Query("SELECT * FROM UserProfileAddiction WHERE user_profile_id = :userId")
    fun getByUserId(userId: Int): Flow<List<UserProfileAddictionEntity>>
}
