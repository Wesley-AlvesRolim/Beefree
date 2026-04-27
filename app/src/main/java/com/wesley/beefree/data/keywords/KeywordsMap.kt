package com.wesley.beefree.data.keywords

import com.wesley.beefree.domain.entities.AddictionCategory

suspend fun buildKeywordsMap(
    categories: List<AddictionCategory>,
    keywords: Map<String, List<String>>,
): Map<Int, List<String>> =
    buildMap {
        categories.filter { it.isMonitoringEnabled }.forEach { category ->
            val categoryKeywords = keywords[category.name] ?: return@forEach
            put(category.id!!, categoryKeywords)
        }
    }
