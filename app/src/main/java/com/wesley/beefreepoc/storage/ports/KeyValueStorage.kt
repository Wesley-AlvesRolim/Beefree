package com.wesley.beefreepoc.storage.ports

interface KeyValueStorage {
    fun <T> set(
        key: String,
        value: T,
    )

    fun <T> get(
        key: String,
        defaultValue: T,
    ): T

    fun delete(key: String)

    fun clear()
}
