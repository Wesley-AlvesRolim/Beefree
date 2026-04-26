package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.UserHobbyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserHobbyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserHobbyEntity): Long

    @Delete
    suspend fun delete(entity: UserHobbyEntity)

    @Query("SELECT * FROM UserHobbies WHERE user_profile_id = :userId")
    fun getAllByUser(userId: Int): Flow<List<UserHobbyEntity>>
}
