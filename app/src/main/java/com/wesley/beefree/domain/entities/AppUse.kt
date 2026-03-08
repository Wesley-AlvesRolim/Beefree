package com.wesley.beefree.domain.entities

data class AppUse(
    val id: Int? = null,
    val packageName: String,
    val enterTime: Long,
    val exitTime: Long? = null,
)
