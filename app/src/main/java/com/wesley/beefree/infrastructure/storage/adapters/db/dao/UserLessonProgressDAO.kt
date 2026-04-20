package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.UserLessonProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserLessonProgressDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserLessonProgressEntity): Long

    @Query("SELECT * FROM UserLessonProgress WHERE user_profile_id = :userId ORDER BY completed_at DESC")
    fun getAllByUser(userId: Int): Flow<List<UserLessonProgressEntity>>
}
