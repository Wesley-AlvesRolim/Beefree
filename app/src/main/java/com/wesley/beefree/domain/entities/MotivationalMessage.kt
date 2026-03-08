package com.wesley.beefree.domain.entities

data class MotivationalMessage(
    val id: Int? = null,
    val addictionTypeId: Int? = null,
    val messageText: String,
    val isActive: Boolean = true,
)
