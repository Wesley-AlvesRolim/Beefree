package com.wesley.beefree.domain.entities

data class SupportContact(
    val id: Int? = null,
    val phoneNumber: String,
    val isActive: Boolean = true,
    val createdAt: Long,
)
