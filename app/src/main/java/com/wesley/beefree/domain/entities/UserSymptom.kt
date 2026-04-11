package com.wesley.beefree.domain.entities

data class UserSymptom(
    val id: Int? = null,
    val userProfileId: Int,
    val symptomCode: String,
    val createdAt: Long,
)
