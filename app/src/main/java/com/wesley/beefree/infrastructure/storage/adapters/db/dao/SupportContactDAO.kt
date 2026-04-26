package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.SupportContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupportContactDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SupportContactEntity): Long

    @Update
    suspend fun update(entity: SupportContactEntity)

    @Delete
    suspend fun delete(entity: SupportContactEntity)

    @Query("SELECT * FROM SupportContacts")
    fun getAll(): Flow<List<SupportContactEntity>>

    @Query("SELECT * FROM SupportContacts WHERE is_active = 1")
    fun getActiveContacts(): Flow<List<SupportContactEntity>>
}
