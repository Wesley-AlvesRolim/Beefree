package com.wesley.beefree.domain.entities

data class AppRestriction(
    val id: Int? = null,
    val addictionTypeId: Int,
    val appPackage: String,
    val screenTimeLimitMillis: Long,
)
