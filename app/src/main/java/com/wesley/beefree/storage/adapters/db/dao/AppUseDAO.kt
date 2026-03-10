package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.AppUseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppUseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AppUseEntity): Long

    @Update
    suspend fun update(entity: AppUseEntity)

    @Query("SELECT * FROM AppUse ORDER BY enter_time DESC")
    fun getAll(): Flow<List<AppUseEntity>>

    @Query("SELECT * FROM AppUse WHERE package_name = :packageName ORDER BY enter_time DESC LIMIT 1")
    suspend fun getLastUseByPackageName(packageName: String): AppUseEntity?
}
