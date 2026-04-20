package com.wesley.beefree.infrastructure.storage.ports

import com.wesley.beefree.domain.entities.DailyLesson
import com.wesley.beefree.domain.entities.UserLessonProgress
import kotlinx.coroutines.flow.Flow

interface LessonRepository {
    suspend fun insertLesson(lesson: DailyLesson): Long

    fun getAllLessons(): Flow<List<DailyLesson>>

    fun getLessonsByProfile(profile: String): Flow<List<DailyLesson>>

    suspend fun insertProgress(progress: UserLessonProgress): Long

    fun getProgress(userId: Int): Flow<List<UserLessonProgress>>
}
