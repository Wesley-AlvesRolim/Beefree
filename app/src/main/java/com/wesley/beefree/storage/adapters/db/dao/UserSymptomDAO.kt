package com.wesley.beefree.storage.adapters.db.dao

import androidx.room.*
import com.wesley.beefree.storage.adapters.db.entities.UserSymptomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSymptomDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserSymptomEntity): Long

    @Delete
    suspend fun delete(entity: UserSymptomEntity)

    @Query("SELECT * FROM UserSymptoms WHERE user_profile_id = :userId")
    fun getAllByUser(userId: Int): Flow<List<UserSymptomEntity>>
}
