package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.RiskAssessmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RiskAssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RiskAssessmentEntity): Long

    @Query("SELECT * FROM RiskAssessment WHERE user_profile_id = :userId ORDER BY created_at DESC")
    fun getAllByUser(userId: Int): Flow<List<RiskAssessmentEntity>>

    @Query("DELETE FROM RiskAssessment WHERE user_profile_id = :userId")
    suspend fun deleteAllByUser(userId: Int)
}
