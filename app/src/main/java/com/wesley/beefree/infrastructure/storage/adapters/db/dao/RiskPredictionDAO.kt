package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.RiskPredictionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RiskPredictionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RiskPredictionEntity): Long

    @Update
    suspend fun update(entity: RiskPredictionEntity)

    @Query("SELECT * FROM RiskPrediction WHERE user_profile_id = :userId ORDER BY predicted_at DESC")
    fun getAllByUser(userId: Int): Flow<List<RiskPredictionEntity>>
}
