package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.MicroActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MicroActivityDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MicroActivityEntity): Long

    @Delete
    suspend fun delete(entity: MicroActivityEntity)

    @Query("SELECT * FROM MicroActivities")
    fun getAll(): Flow<List<MicroActivityEntity>>

    @Query("SELECT * FROM MicroActivities WHERE addiction_type_id = :addictionTypeId")
    fun getAllByAddictionType(addictionTypeId: Int): Flow<List<MicroActivityEntity>>
}
