package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.checkin.ArtificialDopamineSource
import com.wesley.beefree.domain.checkin.DopamineType
import com.wesley.beefree.domain.checkin.NaturalDopamineSource
import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.DailyMicroActivityLog
import com.wesley.beefree.domain.entities.MicroActivity
import com.wesley.beefree.domain.entities.RelapseHistory
import com.wesley.beefree.storage.ports.ActivityRepository
import com.wesley.beefree.storage.ports.AddictionRepository
import com.wesley.beefree.storage.ports.CheckInRepository
import kotlinx.coroutines.flow.first

class SaveDailyCheckInUseCase(
    private val checkInRepository: CheckInRepository,
    private val addictionRepository: AddictionRepository,
    private val activityRepository: ActivityRepository,
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
                    dopamineLevel = dopamineLevel,
                    mood = mood,
                    anxietyLevel = anxietyLevel,
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
        val activities = activityRepository.getAllMicroActivities().first()
        val activity =
            activities.firstOrNull { it.activityType == activityType }
                ?: run {
                    val id =
                        activityRepository.insertMicroActivity(
                            MicroActivity(
                                addictionTypeId = null,
                                activityType = activityType,
                                activityName =
                                    source.name
                                        .lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                createdAt = now,
                            ),
                        )
                    MicroActivity(
                        id = id.toInt(),
                        addictionTypeId = null,
                        activityType = activityType,
                        activityName = source.name,
                        createdAt = now,
                    )
                }

        val activityId = checkNotNull(activity.id) { "MicroActivity id must not be null" }
        activityRepository.insertDailyLog(
            DailyMicroActivityLog(
                userProfileId = userId,
                activityId = activityId,
                dayDate = now,
                completedAt = now,
            ),
        )
    }

    private suspend fun saveRelapsed(
        userId: Int,
        source: ArtificialDopamineSource,
        addictionTypeId: Int?,
        now: Long,
    ) {
        val addictionTypeId = addictionTypeId ?: 0
        val relapseHistory =
            RelapseHistory(
                addictionTypeId = addictionTypeId,
                keywordDetected = null,
                detectedText = null,
                appPackage = null,
                externalApp = null,
                wasSentToExternal = false,
                relapseAt = now,
                updatedAt = now,
            )
        addictionRepository.insertRelapse(relapseHistory)
    }
}
