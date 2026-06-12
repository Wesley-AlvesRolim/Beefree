package com.wesley.beefree.domain.usecases.risk

import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.repository.ports.RiskFeatureSnapshotRepository
import java.util.Calendar

class SaveRiskFeatureSnapshotUseCase(
    private val riskFeatureSnapshotRepository: RiskFeatureSnapshotRepository,
) {
    suspend fun execute(
        userId: Int,
        sleep: Int,
        craving: Int,
        boredom: Int,
        stress: Int,
        loneliness: Int,
        fatigue: Int,
    ): Result<Unit> =
        runCatching {
            val now = System.currentTimeMillis()
            val calendar = Calendar.getInstance().apply { timeInMillis = now }

            val snapshot =
                RiskFeatureSnapshot(
                    userProfileId = userId,
                    sleep = sleep,
                    craving = craving,
                    boredom = boredom,
                    stress = stress,
                    loneliness = loneliness,
                    fatigue = fatigue,
                    hourOfDay = calendar.get(Calendar.HOUR_OF_DAY),
                    dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK),
                    createdAt = now,
                )

            riskFeatureSnapshotRepository.save(snapshot)
        }
}
