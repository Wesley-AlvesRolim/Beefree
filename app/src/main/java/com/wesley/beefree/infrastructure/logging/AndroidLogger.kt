package com.wesley.beefree.infrastructure.logging

import android.util.Log

object AndroidLogger : Logger {
    override fun d(
        tag: String,
        message: String,
    ) {
        Log.d(tag, message)
    }
}
