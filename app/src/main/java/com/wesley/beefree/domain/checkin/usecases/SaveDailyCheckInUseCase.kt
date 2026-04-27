package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.checkin.ArtificialDopamineSource
import com.wesley.beefree.domain.checkin.DopamineType
import com.wesley.beefree.domain.checkin.NaturalDopamineSource
import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.infrastructure.storage.ports.AddictionRepository
import com.wesley.beefree.infrastructure.storage.ports.CheckInRepository
import kotlin.Int

class SaveDailyCheckInUseCase(
    private val checkInRepository: CheckInRepository,
    private val addictionRepository: AddictionRepository,
) {
    suspend fun execute(
        userId: Int,
        dopamineLevel: Int,
        mood: String,
        anxietyLevel: Int?,
        dopamineType: DopamineType,
        addictionTypeId: Int? = null,
    ): Result<Unit> =
        runCatching {
            val now = System.currentTimeMillis()

            checkInRepository.insertDailyCheckIn(
                DailyCheckIn(
                    userProfileId = userId,
                    treatmentProfile = TreatmentProfile.TCC,
                    answers = emptyMap(),
                    checkedInAt = now,
                ),
            )

            when (dopamineType) {
                is DopamineType.Natural -> saveActivityLog(userId, dopamineType.source, now)
                is DopamineType.Artificial ->
                    saveRelapsed(
                        userId,
                        dopamineType.source,
                        addictionTypeId,
                        now,
                    )
            }
        }

    private suspend fun saveActivityLog(
        userId: Int,
        source: NaturalDopamineSource,
        now: Long,
    ) {
        val activityType = source.name
    }

    private suspend fun saveRelapsed(
        userId: Int,
        source: ArtificialDopamineSource,
        addictionTypeId: Int?,
        now: Long,
    ) {
        val addictionTypeId = addictionTypeId ?: 0
        val relapseHistory =
            RelapseRecord(
                addictionCategoryId = addictionTypeId,
                keywordDetected = null,
                detectedText = null,
                appUsageSessionId = null,
                createdAt = now,
            )
        addictionRepository.insertRelapse(relapseHistory)
    }
}
