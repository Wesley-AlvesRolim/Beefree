package com.wesley.beefree.domain.entities

data class BlockScreenConfig(
    val id: Int? = null,
    val addictionTypeId: Int? = null,
    val titleText: String? = null,
    val subtitleText: String? = null,
    val imageUri: String? = null,
    val backgroundColor: Int = -1,
    val textColor: Int = -16777216,
    val updatedAt: Long,
)
