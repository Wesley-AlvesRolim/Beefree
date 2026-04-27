package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.PsychoeducationContentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PsychoeducationContentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PsychoeducationContentEntity): Long

    @Query("SELECT * FROM PsychoeducationContent WHERE is_active = 1")
    fun getActive(): Flow<List<PsychoeducationContentEntity>>

    @Query(
        "SELECT * FROM PsychoeducationContent WHERE is_active = 1 AND (addiction_category_id IS NULL OR addiction_category_id = :categoryId)",
    )
    fun getByCategory(categoryId: Int): Flow<List<PsychoeducationContentEntity>>
}
