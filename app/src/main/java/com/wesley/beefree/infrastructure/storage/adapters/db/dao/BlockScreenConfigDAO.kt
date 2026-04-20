package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.BlockScreenConfigEntity

@Dao
interface BlockScreenConfigDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BlockScreenConfigEntity): Long

    @Update
    suspend fun update(entity: BlockScreenConfigEntity)

    @Query("SELECT * FROM BlockScreenConfig WHERE addiction_type_id = :addictionTypeId LIMIT 1")
    suspend fun getByAddictionTypeId(addictionTypeId: Int): BlockScreenConfigEntity?

    @Query("SELECT * FROM BlockScreenConfig WHERE id = :id")
    suspend fun getById(id: Int): BlockScreenConfigEntity?
}
