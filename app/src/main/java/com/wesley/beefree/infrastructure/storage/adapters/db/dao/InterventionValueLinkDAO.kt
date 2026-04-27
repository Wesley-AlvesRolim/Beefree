package com.wesley.beefree.infrastructure.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.InterventionValueLinkEntity

@Dao
interface InterventionValueLinkDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: InterventionValueLinkEntity): Long

    @Query("SELECT * FROM InterventionValueLink WHERE intervention_id = :interventionId")
    suspend fun getByIntervention(interventionId: Int): List<InterventionValueLinkEntity>
}
