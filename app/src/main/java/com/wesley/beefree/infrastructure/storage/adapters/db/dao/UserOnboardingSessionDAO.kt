package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.UserOnboardingSessionEntity

@Dao
interface UserOnboardingSessionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserOnboardingSessionEntity): Long

    @Query("SELECT * FROM UserOnboardingSession WHERE user_profile_id = :userId ORDER BY created_at DESC LIMIT 1")
    suspend fun getByUser(userId: Int): UserOnboardingSessionEntity?
}
