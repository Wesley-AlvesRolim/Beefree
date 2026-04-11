package com.wesley.beefree.storage.adapters

import com.wesley.beefree.domain.entities.DailyLesson
import com.wesley.beefree.domain.entities.UserLessonProgress
import com.wesley.beefree.storage.adapters.db.dao.DailyLessonDAO
import com.wesley.beefree.storage.adapters.db.dao.UserLessonProgressDAO
import com.wesley.beefree.storage.adapters.db.toDomain
import com.wesley.beefree.storage.adapters.db.toEntity
import com.wesley.beefree.storage.ports.LessonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLessonRepository(
    private val dailyLessonDao: DailyLessonDAO,
    private val userLessonProgressDao: UserLessonProgressDAO,
) : LessonRepository {
    override suspend fun insertLesson(lesson: DailyLesson): Long = dailyLessonDao.insert(lesson.toEntity())

    override fun getAllLessons(): Flow<List<DailyLesson>> = dailyLessonDao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getLessonsByProfile(profile: String): Flow<List<DailyLesson>> =
        dailyLessonDao.getByTargetProfile(profile).map { list -> list.map { it.toDomain() } }

    override suspend fun insertProgress(progress: UserLessonProgress): Long = userLessonProgressDao.insert(progress.toEntity())

    override fun getProgress(userId: Int): Flow<List<UserLessonProgress>> =
        userLessonProgressDao.getAllByUser(userId).map { list -> list.map { it.toDomain() } }
}
