package com.wesley.beefree.infrastructure.storage.adapters

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.wesley.beefree.domain.repository.ports.KeyValueStorage

class SharedPreferencesKeyValueStorage(
    context: Context,
) : KeyValueStorage {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("beefree_storage.infrastructure", Context.MODE_PRIVATE)

    override fun <T> set(
        key: String,
        value: T,
    ) {
        sharedPreferences.edit {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                else -> throw IllegalArgumentException("Unsupported type: ${'$'}{value!!::class.java.name}")
            }
        }
    }

    override fun <T> get(
        key: String,
        defaultValue: T,
    ): T =
        when (defaultValue) {
            is String -> sharedPreferences.getString(key, defaultValue) as T
            is Int -> sharedPreferences.getInt(key, defaultValue) as T
            is Long -> sharedPreferences.getLong(key, defaultValue) as T
            is Float -> sharedPreferences.getFloat(key, defaultValue) as T
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue) as T
            else -> throw IllegalArgumentException("Unsupported type: ${'$'}{defaultValue!!::class.java.name}")
        }

    override fun delete(key: String) {
        sharedPreferences.edit { remove(key) }
    }

    override fun clear() {
        sharedPreferences.edit { clear() }
    }
}
