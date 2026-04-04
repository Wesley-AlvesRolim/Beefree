package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.OnboardingScaleAnswerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OnboardingScaleAnswerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: OnboardingScaleAnswerEntity): Long

    @Query("SELECT * FROM OnboardingScaleAnswers WHERE user_profile_id = :userId AND scale_type = :scaleType")
    fun getByUserAndScale(
        userId: Int,
        scaleType: String,
    ): Flow<List<OnboardingScaleAnswerEntity>>
}
