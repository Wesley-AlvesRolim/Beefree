package com.wesley.beefree.domain.entities

data class UserCoreValue(
    val id: Int? = null,
    val userProfileId: Int,
    val value: CoreValueType,
    val createdAt: Long,
)

enum class CoreValueType {
    FAMILY,
    FAITH,
    HONESTY,
    HEALTH,
    RELATIONSHIPS,
    GROWTH,
    WORK,
    COMMUNITY,
    LOVE,
    FREEDOM,
}
