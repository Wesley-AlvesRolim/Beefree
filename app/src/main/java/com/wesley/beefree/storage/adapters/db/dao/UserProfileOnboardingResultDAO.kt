package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.UserProfileOnboardingResultEntity

@Dao
interface UserProfileOnboardingResultDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserProfileOnboardingResultEntity): Long

    @Query("SELECT * FROM UserProfileOnboardingResult WHERE user_profile_id = :userId LIMIT 1")
    suspend fun getByUser(userId: Int): UserProfileOnboardingResultEntity?
}
