package com.wesley.beefree.domain.shared

data class OptionSpec(
    val id: String,
    val labelKey: String,
    val descriptionKey: String? = null,
    val iconId: String? = null,
)
