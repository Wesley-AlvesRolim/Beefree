package com.wesley.beefree.infrastructure.storage.repositories

import com.wesley.beefree.domain.repository.ports.KeyValueStorage

class KeyValueStorageRepository(
    private val keyValueStorage: KeyValueStorage,
) {
    fun saveOnboardingCompleted(completed: Boolean) {
        keyValueStorage.set("onboarding_completed", completed)
    }

    fun isOnboardingCompleted(): Boolean = keyValueStorage.get("onboarding_completed", false)

    fun saveTheScreenReaderStatus(isEnabled: Boolean) {
        keyValueStorage.set("ScreenReaderStatus", isEnabled)
    }

    fun getTheScreenReaderStatus(): Boolean = keyValueStorage.get("ScreenReaderStatus", false)

    fun clearAllData() {
        keyValueStorage.clear()
    }
}
