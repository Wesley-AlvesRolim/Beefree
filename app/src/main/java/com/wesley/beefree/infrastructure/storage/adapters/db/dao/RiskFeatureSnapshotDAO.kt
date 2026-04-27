package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.RiskFeatureSnapshotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RiskFeatureSnapshotDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RiskFeatureSnapshotEntity): Long

    @Query("SELECT * FROM RiskFeatureSnapshot WHERE user_profile_id = :userId ORDER BY created_at DESC")
    fun getAllByUser(userId: Int): Flow<List<RiskFeatureSnapshotEntity>>

    @Query("SELECT * FROM RiskFeatureSnapshot WHERE user_profile_id = :userId ORDER BY created_at DESC LIMIT 1")
    suspend fun getLatestByUser(userId: Int): RiskFeatureSnapshotEntity?
}
