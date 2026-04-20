package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserProfileEntity): Long

    @Update
    suspend fun update(entity: UserProfileEntity)

    @Query("SELECT * FROM UserProfile WHERE id = :id")
    suspend fun getById(id: Int): UserProfileEntity?

    @Query("SELECT * FROM UserProfile")
    fun getAll(): Flow<List<UserProfileEntity>>
}
