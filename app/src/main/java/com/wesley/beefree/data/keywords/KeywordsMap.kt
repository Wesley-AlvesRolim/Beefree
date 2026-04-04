package com.wesley.beefree.data.keywords

import com.wesley.beefree.domain.entities.AddictionType
import com.wesley.beefree.storage.ports.AddictionRepository
import kotlinx.coroutines.flow.first

suspend fun buildKeywordsMap(
    types: List<AddictionType>,
    repository: AddictionRepository,
): Map<Int, List<String>> =
    buildMap {
        types.filter { it.isMonitoringEnabled }.forEach { type ->
            val keywords = repository.getKeywordsByAddictionType(type.id!!).first()
            put(type.id, keywords.map { it.keyword })
        }
    }
