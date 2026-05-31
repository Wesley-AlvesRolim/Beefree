package com.wesley.beefree.infrastructure.logging

data class LogEntry(
    val level: String,
    val tag: String,
    val message: String,
    val throwable: Throwable? = null,
)

object TestLogger : Logger {
    val entries: MutableList<LogEntry> = mutableListOf()

    fun clear() {
        entries.clear()
    }

    override fun info(
        tag: String,
        message: String,
    ) {
        entries += LogEntry(level = "INFO", tag = tag, message = message)
    }

    override fun d(
        tag: String,
        message: String,
    ) {
        entries += LogEntry(level = "D", tag = tag, message = message)
    }

    override fun e(
        tag: String,
        message: String,
        throwable: Throwable?,
    ) {
        entries += LogEntry(level = "E", tag = tag, message = message, throwable = throwable)
    }
}
