package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.AppUsageSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppUsageSessionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AppUsageSessionEntity): Long

    @Update
    suspend fun update(entity: AppUsageSessionEntity)

    @Query("SELECT * FROM AppUsageSession WHERE package_name = :packageName ORDER BY enter_time DESC LIMIT 1")
    suspend fun getLastSessionByPackage(packageName: String): AppUsageSessionEntity?

    @Query("SELECT * FROM AppUsageSession ORDER BY enter_time DESC")
    fun getAll(): Flow<List<AppUsageSessionEntity>>
}
