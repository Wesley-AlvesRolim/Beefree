package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.UserObjectiveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserObjectiveDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserObjectiveEntity): Long

    @Delete
    suspend fun delete(entity: UserObjectiveEntity)

    @Query("SELECT * FROM UserObjectives WHERE user_profile_id = :userId")
    fun getAllByUser(userId: Int): Flow<List<UserObjectiveEntity>>
}
