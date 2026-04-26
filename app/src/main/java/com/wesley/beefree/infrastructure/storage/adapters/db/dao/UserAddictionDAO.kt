package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.UserAddictionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAddictionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserAddictionEntity): Long

    @Delete
    suspend fun delete(entity: UserAddictionEntity)

    @Query("SELECT * FROM UserAddiction WHERE user_profile_id = :userId")
    fun getByUserId(userId: Int): Flow<List<UserAddictionEntity>>
}
