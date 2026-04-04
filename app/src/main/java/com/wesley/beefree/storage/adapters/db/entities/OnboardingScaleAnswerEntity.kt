package com.wesley.beefree.storage.adapters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "OnboardingScaleAnswers")
data class OnboardingScaleAnswerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_profile_id") val userProfileId: Int,
    @ColumnInfo(name = "scale_type") val scaleType: String,
    @ColumnInfo(name = "question_index") val questionIndex: Int,
    @ColumnInfo(name = "answer_value") val answerValue: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
