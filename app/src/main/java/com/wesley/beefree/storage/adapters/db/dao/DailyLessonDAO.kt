package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.DailyLessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyLessonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DailyLessonEntity): Long

    @Query("SELECT * FROM DailyLessons")
    fun getAll(): Flow<List<DailyLessonEntity>>

    @Query("SELECT * FROM DailyLessons WHERE target_profile = :profile")
    fun getByTargetProfile(profile: String): Flow<List<DailyLessonEntity>>
}
