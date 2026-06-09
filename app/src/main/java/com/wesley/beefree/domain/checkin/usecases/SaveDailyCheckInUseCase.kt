package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.checkin.DailyCheckInAnswer
import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.repository.ports.AddictionRepository
import com.wesley.beefree.domain.repository.ports.CheckInRepository
import com.wesley.beefree.domain.entities.DailyCheckInAnswer as EntityAnswer

class SaveDailyCheckInUseCase(
    private val checkInRepository: CheckInRepository,
    private val addictionRepository: AddictionRepository,
) {
    suspend fun execute(
        userId: Int,
        treatmentProfile: TreatmentProfile,
        answers: Map<String, DailyCheckInAnswer>,
        addictionTypeId: Int? = null,
    ): Result<Unit> =
        runCatching {
            val now = System.currentTimeMillis()
            val entityAnswers = answers.mapValues { (_, v) -> v.toEntityAnswer() }

            checkInRepository.insertDailyCheckIn(
                DailyCheckIn(
                    userProfileId = userId,
                    treatmentProfile = treatmentProfile,
                    answers = entityAnswers,
                    checkedInAt = now,
                ),
            )

            val relapseAnswer = answers.values.filterIsInstance<DailyCheckInAnswer.RelapseRegistration>().firstOrNull()
            if (relapseAnswer != null) {
                addictionRepository.insertRelapse(
                    RelapseRecord(
                        addictionCategoryId = addictionTypeId ?: 0,
                        keywordDetected = null,
                        detectedText = relapseAnswer.triggers.joinToString(","),
                        appUsageSessionId = null,
                        createdAt = now,
                    ),
                )
            }
        }
}

private fun DailyCheckInAnswer.toEntityAnswer(): EntityAnswer =
    when (this) {
        is DailyCheckInAnswer.Scale -> EntityAnswer.Scale(value)
        is DailyCheckInAnswer.DualScale -> EntityAnswer.DualScale(first, second)
        is DailyCheckInAnswer.MultiSelect -> EntityAnswer.MultiSelect(ids, context)
        is DailyCheckInAnswer.SingleSelect -> EntityAnswer.SingleSelect(id)
        is DailyCheckInAnswer.Text -> EntityAnswer.Text(value)
        is DailyCheckInAnswer.Bool -> EntityAnswer.Bool(value)
        is DailyCheckInAnswer.EmotionalRecord -> EntityAnswer.EmotionalRecord(alreadyDone)
        is DailyCheckInAnswer.SingleSelectWithContext -> EntityAnswer.SingleSelectWithContext(id, context)
        is DailyCheckInAnswer.TherapeuticActivity -> EntityAnswer.TherapeuticActivity(activityType.name)
        is DailyCheckInAnswer.TextWithSuggestions -> EntityAnswer.TextWithSuggestions(value)
        is DailyCheckInAnswer.RelapseRegistration -> EntityAnswer.RelapseRegistration(hour, minute, triggers, context)
        is DailyCheckInAnswer.VideoWatch -> EntityAnswer.Bool(true)
        is DailyCheckInAnswer.Mindfulness -> EntityAnswer.Bool(true)
    }
