package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.entities.PsychoeducationContent
import com.wesley.beefree.infrastructure.storage.adapters.db.dao.PsychoeducationContentDAO
import com.wesley.beefree.infrastructure.storage.adapters.db.toDomain
import com.wesley.beefree.infrastructure.storage.adapters.db.toEntity
import com.wesley.beefree.infrastructure.storage.ports.LessonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLessonRepository(
    private val contentDao: PsychoeducationContentDAO,
) : LessonRepository {
    override suspend fun insertContent(content: PsychoeducationContent): Long = contentDao.insert(content.toEntity())

    override fun getActiveContent(): Flow<List<PsychoeducationContent>> = contentDao.getActive().map { list -> list.map { it.toDomain() } }

    override fun getContentByCategory(categoryId: Int): Flow<List<PsychoeducationContent>> =
        contentDao.getByCategory(categoryId).map { list -> list.map { it.toDomain() } }
}
