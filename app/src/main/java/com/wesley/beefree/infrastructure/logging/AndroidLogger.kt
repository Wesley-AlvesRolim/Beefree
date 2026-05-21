package com.wesley.beefree.infrastructure.logging

import android.util.Log

object AndroidLogger : Logger {
    override fun d(
        tag: String,
        message: String,
    ) {
        Log.d(tag, message)
    }

    override fun e(
        tag: String,
        message: String,
        throwable: Throwable?,
    ) {
        Log.e(tag, message, throwable)
    }
}
