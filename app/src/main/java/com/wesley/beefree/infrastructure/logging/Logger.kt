package com.wesley.beefree.infrastructure.logging

interface Logger {
    fun d(
        tag: String,
        message: String,
    )

    fun e(
        tag: String,
        message: String,
        throwable: Throwable? = null,
    ) = d(tag, message)
}
