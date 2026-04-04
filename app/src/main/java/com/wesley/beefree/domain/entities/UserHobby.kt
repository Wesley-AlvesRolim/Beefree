package com.wesley.beefree.domain.entities

data class UserHobby(
    val id: Int? = null,
    val userProfileId: Int,
    val hobbyName: String,
    val createdAt: Long,
)
