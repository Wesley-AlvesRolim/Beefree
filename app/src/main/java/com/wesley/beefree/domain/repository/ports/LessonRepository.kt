package com.wesley.beefree.domain.repository.ports

import com.wesley.beefree.domain.entities.PsychoeducationContent
import kotlinx.coroutines.flow.Flow

interface LessonRepository {
    suspend fun insertContent(content: PsychoeducationContent): Long

    fun getActiveContent(): Flow<List<PsychoeducationContent>>

    fun getContentByCategory(categoryId: Int): Flow<List<PsychoeducationContent>>
}
