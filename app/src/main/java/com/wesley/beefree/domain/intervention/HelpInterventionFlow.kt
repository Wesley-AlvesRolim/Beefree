package com.wesley.beefree.domain.intervention

import com.wesley.beefree.domain.checkin.OptionSpec

sealed interface HelpInterventionStep {
    data class IntensityStep(
        val titleKey: String,
        val subtitleKey: String,
        val id: String,
    ) : HelpInterventionStep

    data class BodyLocationStep(
        val titleKey: String,
        val options: List<OptionSpec>,
    ) : HelpInterventionStep

    data class UrgeSurfingStep(
        val meditationStepKeys: List<String>,
        val intervalMs: Long = 2000L,
        val minimumCycles: Int = 2,
    ) : HelpInterventionStep

    data class PostSurfIntensityStep(
        val titleKey: String,
        val loopThreshold: Int = 7,
    ) : HelpInterventionStep

    data class ActValuesStep(
        val titleKey: String,
        val predefinedOptions: List<OptionSpec>,
    ) : HelpInterventionStep

    data class ActDirectionStep(
        val titleKey: String,
        val options: List<OptionSpec>,
    ) : HelpInterventionStep

    data class ActCommittedActionStep(
        val titleKey: String,
        val suggestions: List<ActionSuggestion>,
    ) : HelpInterventionStep

    data class TccAutomaticThoughtStep(
        val titleKey: String,
        val suggestions: List<String>,
    ) : HelpInterventionStep

    data class TccEvidenceForStep(
        val titleKey: String,
    ) : HelpInterventionStep

    data class TccEvidenceAgainstStep(
        val titleKey: String,
        val suggestions: List<String>,
    ) : HelpInterventionStep

    data class TccAlternativeThoughtStep(
        val titleKey: String,
        val suggestions: List<String>,
    ) : HelpInterventionStep

    data class TccActionStep(
        val titleKey: String,
        val suggestions: List<ActionSuggestion>,
    ) : HelpInterventionStep

    data class TimerStep(
        val durationSeconds: Int = 120,
    ) : HelpInterventionStep

    data class ReflectionStep(
        val questionKey: String,
    ) : HelpInterventionStep
}

data class ActionSuggestion(
    val labelKey: String,
    val category: String,
)

data class HelpInterventionFlow(
    val commonSteps: List<HelpInterventionStep>,
    val clinicalSteps: List<HelpInterventionStep>,
    val finalSteps: List<HelpInterventionStep>,
) {
    val allSteps: List<HelpInterventionStep>
        get() = commonSteps + clinicalSteps + finalSteps
}
