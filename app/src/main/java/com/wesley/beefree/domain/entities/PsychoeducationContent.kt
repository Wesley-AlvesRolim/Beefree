package com.wesley.beefree.domain.entities

data class PsychoeducationContent(
    val id: Int? = null,
    val addictionCategoryId: Int? = null,
    val contentText: String,
    val isActive: Boolean = true,
)
