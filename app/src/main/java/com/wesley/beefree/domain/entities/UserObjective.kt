package com.wesley.beefree.domain.entities

data class UserObjective(
    val id: Int? = null,
    val userProfileId: Int,
    val objectiveText: String,
    val createdAt: Long,
)
