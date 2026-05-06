package com.wesley.beefree.domain.intervention.usecases

import com.wesley.beefree.domain.entities.CognitiveThoughtRecord
import com.wesley.beefree.domain.entities.HelpInterventionSession
import com.wesley.beefree.domain.entities.InterventionRecord
import com.wesley.beefree.infrastructure.storage.ports.EMIRepository

class SaveInterventionSessionUseCase(
    private val emiRepository: EMIRepository,
) {
    suspend fun execute(session: HelpInterventionSession) {
        val interventionRecord =
            InterventionRecord(
                userProfileId = session.userProfileId,
                interventionType = "EMI_${session.clinicalBranch.name}",
                triggerType = "SOS",
                wasCompleted = session.wasImpulseStillStrong.not(),
                createdAt = session.createdAt,
            )
        emiRepository.insertInterventionRecord(interventionRecord)

        if (session.automaticThought
                .isNullOrEmpty()
                .not() ||
            session.alternativeThought.isNullOrEmpty().not()
        ) {
            val thoughtRecord =
                CognitiveThoughtRecord(
                    userProfileId = session.userProfileId,
                    situation =
                        "Impulse intensity: ${session.intensityBefore} → ${session.intensityAfter}. " +
                            "Locations: ${session.bodyLocations.joinToString()}",
                    automaticThought = session.automaticThought ?: "",
                    feeling = "Urge",
                    consequence = session.committedAction ?: "N/A",
                    alternativeThought = session.alternativeThought ?: "",
                    cognitiveDistortions = emptyList(),
                    createdAt = session.createdAt,
                )
            emiRepository.insertThoughtRecord(thoughtRecord)
        }
    }
}
