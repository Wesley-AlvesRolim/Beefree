package com.wesley.beefree.domain.checkin

import com.wesley.beefree.domain.shared.OptionSpec

sealed interface DailyCheckInStep {
    val id: String
}

enum class ActivityType { MINDFULNESS, PROFILE_EXERCISE, VIDEO }

enum class ScaleStepType { CRAVING, ACCEPTANCE, PRESENCE }

data class ActivityOption(
    val type: ActivityType,
    val titleKey: String,
    val descriptionKey: String,
    val subSteps: List<DailyCheckInStep> = emptyList(),
)

data class ScaleSpec(
    val key: String,
    val titleKey: String,
    val lowLabelKey: String,
    val highLabelKey: String,
)

data class ScaleStep(
    override val id: String,
    val type: ScaleStepType,
    val titleKey: String,
    val subtitleKey: String,
    val lowLabelKey: String,
    val highLabelKey: String,
) : DailyCheckInStep

data class DualScaleStep(
    override val id: String,
    val titleKey: String,
    val subtitleKey: String,
    val first: ScaleSpec,
    val second: ScaleSpec,
) : DailyCheckInStep

data class MultiSelectStep(
    override val id: String,
    val titleKey: String,
    val subtitleKey: String,
    val options: List<OptionSpec>,
    val withContext: Boolean = false,
    val contextHintKey: String? = null,
) : DailyCheckInStep

data class SingleSelectStep(
    override val id: String,
    val titleKey: String,
    val subtitleKey: String,
    val options: List<OptionSpec>,
) : DailyCheckInStep

data class TextStep(
    override val id: String,
    val titleKey: String,
    val subtitleKey: String,
    val hintKey: String,
) : DailyCheckInStep

data class BooleanBranchStep(
    override val id: String,
    val titleKey: String,
    val subtitleKey: String,
    val yesLabelKey: String,
    val noLabelKey: String,
    val onYes: DailyCheckInStep,
    val onNo: DailyCheckInStep,
) : DailyCheckInStep

data class EmotionalRecordStep(
    override val id: String,
    val titleKey: String,
    val subtitleKey: String,
) : DailyCheckInStep

data class SingleSelectWithContextStep(
    override val id: String,
    val titleKey: String,
    val subtitleKey: String,
    val options: List<OptionSpec>,
    val contextHintKey: String? = null,
    val isObjectiveReview: Boolean = false,
) : DailyCheckInStep

data class TherapeuticActivityStep(
    override val id: String,
    val titleKey: String,
    val subtitleKey: String,
    val activityOptions: List<ActivityOption>,
) : DailyCheckInStep

data class TextWithSuggestionsStep(
    override val id: String,
    val titleKey: String,
    val subtitleKey: String,
    val hintKey: String,
    val suggestionKeys: List<String> = emptyList(),
) : DailyCheckInStep

data class RelapseRegistrationStep(
    override val id: String,
    val titleKey: String,
    val subtitleKey: String,
    val triggerOptions: List<OptionSpec>,
) : DailyCheckInStep

data class VideoWatchStep(
    override val id: String,
    val titleKey: String,
    val subtitleKey: String,
    val videoUrl: String,
) : DailyCheckInStep

data class MindfulnessStep(
    override val id: String,
    val cycles: Int,
) : DailyCheckInStep
