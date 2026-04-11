package com.wesley.beefree.infrastructure.logging

fun interface Logger {
    fun d(
        tag: String,
        message: String,
    )
}
