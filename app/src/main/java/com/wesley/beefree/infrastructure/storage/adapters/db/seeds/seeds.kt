package com.wesley.beefree.infrastructure.storage.adapters.db.seeds

import android.os.Build
import com.wesley.beefree.data.getPsychologistEncouragementPhrases
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.AddictionCategoryEntity
import com.wesley.beefree.infrastructure.storage.adapters.db.entities.PsychoeducationContentEntity
import kotlinx.coroutines.flow.first

private const val CATEGORY_ADULT_CONTENT = 1
private const val CATEGORY_BETS = 2
private val NOW = System.currentTimeMillis()

suspend fun seed(database: AppDatabase) {
    val isProbablyAnEmulator =
        Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("unknown") ||
            Build.MODEL.contains("sdk_gphone") ||
            (Build.BRAND == "google" && Build.DEVICE.startsWith("emu64")) ||
            Build.PRODUCT == "sdk_gphone64_x86_64"
    if (!isProbablyAnEmulator) return

    val isNotCreatedTheUser =
        database
            .userProfileDao()
            .getAll()
            .first()
            .isEmpty()
    if (isNotCreatedTheUser) return

    seedAddictionCategories(database)
    seedPsychoeducationContent(database)
}

private suspend fun seedAddictionCategories(database: AppDatabase) {
    val dao = database.addictionCategoryDao()

    dao.insert(
        AddictionCategoryEntity(
            id = CATEGORY_ADULT_CONTENT,
            name = "ADULT_CONTENT",
            isMonitoringEnabled = true,
            createdAt = NOW,
            updatedAt = NOW,
        ),
    )
    dao.insert(
        AddictionCategoryEntity(
            id = CATEGORY_BETS,
            name = "BETS",
            isMonitoringEnabled = true,
            createdAt = NOW,
            updatedAt = NOW,
        ),
    )
}

private suspend fun seedPsychoeducationContent(database: AppDatabase) {
    val dao = database.psychoeducationContentDao()

    val genericContent = getPsychologistEncouragementPhrases()

    genericContent.forEach { text ->
        dao.insert(PsychoeducationContentEntity(contentText = text))
    }
}
