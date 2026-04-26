package com.wesley.beefree.domain.entities

data class AppUsageSession(
    val id: Int? = null,
    val packageName: String,
    val enterTime: Long,
    val exitTime: Long? = null,
)
