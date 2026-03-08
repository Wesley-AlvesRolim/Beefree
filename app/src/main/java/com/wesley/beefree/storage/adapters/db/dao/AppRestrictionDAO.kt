package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.AppRestrictionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppRestrictionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AppRestrictionEntity): Long

    @Update
    suspend fun update(entity: AppRestrictionEntity)

    @Delete
    suspend fun delete(entity: AppRestrictionEntity)

    @Query("SELECT * FROM AppRestriction WHERE id = :id")
    suspend fun getById(id: Int): AppRestrictionEntity?

    @Query("SELECT * FROM AppRestriction")
    fun getAll(): Flow<List<AppRestrictionEntity>>

    @Query("SELECT * FROM AppRestriction WHERE app_package = :packageName")
    suspend fun getByPackageName(packageName: String): AppRestrictionEntity?
}
