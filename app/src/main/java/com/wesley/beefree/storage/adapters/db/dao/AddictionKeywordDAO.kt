package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.AddictionKeywordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddictionKeywordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AddictionKeywordEntity): Long

    @Update
    suspend fun update(entity: AddictionKeywordEntity)

    @Delete
    suspend fun delete(entity: AddictionKeywordEntity)

    @Query("SELECT * FROM AddictionKeywords WHERE id = :id")
    suspend fun getById(id: Int): AddictionKeywordEntity?

    @Query("SELECT * FROM AddictionKeywords WHERE addiction_type_id = :addictionTypeId")
    fun getByAddictionTypeId(addictionTypeId: Int): Flow<List<AddictionKeywordEntity>>
}
