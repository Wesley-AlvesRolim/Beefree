package com.wesley.beefreepoc.exceptions

class BlockedFlagDetectedException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
