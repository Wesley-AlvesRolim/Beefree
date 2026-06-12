package com.wesley.beefree.domain.treatments.checkin

import com.wesley.beefree.domain.checkin.ActivityOption
import com.wesley.beefree.domain.checkin.ActivityType
import com.wesley.beefree.domain.checkin.BooleanBranchStep
import com.wesley.beefree.domain.checkin.DailyCheckInFlow
import com.wesley.beefree.domain.checkin.EmotionalRecordStep
import com.wesley.beefree.domain.checkin.MindfulnessStep
import com.wesley.beefree.domain.checkin.MultiSelectStep
import com.wesley.beefree.domain.checkin.RelapseRegistrationStep
import com.wesley.beefree.domain.checkin.ScaleStep
import com.wesley.beefree.domain.checkin.ScaleStepType
import com.wesley.beefree.domain.checkin.SingleSelectStep
import com.wesley.beefree.domain.checkin.SingleSelectWithContextStep
import com.wesley.beefree.domain.checkin.TextStep
import com.wesley.beefree.domain.checkin.TextWithSuggestionsStep
import com.wesley.beefree.domain.checkin.TherapeuticActivityStep
import com.wesley.beefree.domain.checkin.VideoWatchStep
import com.wesley.beefree.domain.shared.OptionSpec

object ActDailyCheckInFlow {
    const val STEP_EMOTIONAL_RECORD = "act.emotional_record"
    const val STEP_OBJECTIVE_REVIEW = "act.objective_review"
    const val STEP_THERAPEUTIC_ACTIVITY = "act.therapeutic_activity"
    const val STEP_MINDFULNESS = "act.mindfulness"
    const val STEP_VIDEO_WATCH = "act.video_watch"
    const val STEP_VIDEO_REFLECTION = "act.video_reflection"
    const val STEP_CRAVING = "act.craving"
    const val STEP_ACCEPTANCE = "act.acceptance"
    const val STEP_EMOTIONS = "act.emotions"
    const val STEP_PRESENCE = "act.presence"
    const val STEP_COMMITTED_ACTION = "act.committed_action"
    const val STEP_DIRECTION = "act.direction"
    const val STEP_GOAL_DEFINITION = "act.goal_definition"
    const val STEP_RELAPSE_CHECK = "act.relapse_check"
    const val STEP_DAY_EVAL = "act.day_eval"
    const val STEP_RELAPSE_REG = "act.relapse_reg"

    private val exerciseSubSteps =
        listOf(
            ScaleStep(
                id = STEP_CRAVING,
                type = ScaleStepType.CRAVING,
                titleKey = "daily_checkin_act_craving_title",
                subtitleKey = "daily_checkin_act_craving_subtitle",
                lowLabelKey = "daily_checkin_act_craving_low",
                highLabelKey = "daily_checkin_act_craving_high",
            ),
            ScaleStep(
                id = STEP_ACCEPTANCE,
                type = ScaleStepType.ACCEPTANCE,
                titleKey = "daily_checkin_act_acceptance_title",
                subtitleKey = "daily_checkin_act_acceptance_subtitle",
                lowLabelKey = "daily_checkin_act_acceptance_low",
                highLabelKey = "daily_checkin_act_acceptance_high",
            ),
            MultiSelectStep(
                id = STEP_EMOTIONS,
                titleKey = "daily_checkin_act_emotions_title",
                subtitleKey = "daily_checkin_act_emotions_subtitle",
                options =
                    listOf(
                        OptionSpec("anxiety", "daily_checkin_emotion_anxiety", iconId = "anxiety"),
                        OptionSpec("guilt", "daily_checkin_emotion_guilt", iconId = "guilt"),
                        OptionSpec("shame", "daily_checkin_emotion_shame", iconId = "shame"),
                        OptionSpec("sadness", "daily_checkin_emotion_sadness", iconId = "sadness"),
                        OptionSpec("anger", "daily_checkin_emotion_anger", iconId = "anger"),
                        OptionSpec("loneliness", "daily_checkin_emotion_loneliness", iconId = "loneliness"),
                        OptionSpec("numb", "daily_checkin_emotion_numb", iconId = "numb"),
                        OptionSpec("other", "daily_checkin_emotion_other", iconId = "other"),
                    ),
            ),
            ScaleStep(
                id = STEP_PRESENCE,
                type = ScaleStepType.PRESENCE,
                titleKey = "daily_checkin_act_presence_title",
                subtitleKey = "daily_checkin_act_presence_subtitle",
                lowLabelKey = "daily_checkin_act_presence_low",
                highLabelKey = "daily_checkin_act_presence_high",
            ),
            SingleSelectStep(
                id = STEP_COMMITTED_ACTION,
                titleKey = "daily_checkin_act_committed_action_title",
                subtitleKey = "daily_checkin_act_committed_action_subtitle",
                options =
                    listOf(
                        OptionSpec(
                            "yes",
                            "daily_checkin_act_committed_action_yes",
                            "daily_checkin_act_committed_action_yes_desc",
                        ),
                        OptionSpec(
                            "no",
                            "daily_checkin_act_committed_action_no",
                            "daily_checkin_act_committed_action_no_desc",
                        ),
                    ),
            ),
            TextStep(
                id = STEP_DIRECTION,
                titleKey = "daily_checkin_act_direction_title",
                subtitleKey = "daily_checkin_act_direction_subtitle",
                hintKey = "daily_checkin_act_direction_hint",
            ),
        )

    private val activityOptions =
        listOf(
            ActivityOption(
                type = ActivityType.MINDFULNESS,
                titleKey = "daily_checkin_activity_mindfulness_title",
                descriptionKey = "daily_checkin_activity_mindfulness_desc",
                subSteps = listOf(MindfulnessStep(id = STEP_MINDFULNESS, cycles = 3)),
            ),
            ActivityOption(
                type = ActivityType.PROFILE_EXERCISE,
                titleKey = "daily_checkin_activity_exercise_act_title",
                descriptionKey = "daily_checkin_activity_exercise_act_desc",
                subSteps = exerciseSubSteps,
            ),
            ActivityOption(
                type = ActivityType.VIDEO,
                titleKey = "daily_checkin_activity_video_title",
                descriptionKey = "daily_checkin_activity_video_desc",
                subSteps =
                    listOf(
                        VideoWatchStep(
                            id = STEP_VIDEO_WATCH,
                            titleKey = "daily_checkin_video_watch_title",
                            subtitleKey = "daily_checkin_video_watch_subtitle",
                            videoUrl = "https://www.youtube.com/watch?v=wSF82AwSDiU",
                        ),
                        TextStep(
                            id = STEP_VIDEO_REFLECTION,
                            titleKey = "daily_checkin_video_reflection_title",
                            subtitleKey = "daily_checkin_video_reflection_subtitle",
                            hintKey = "daily_checkin_video_reflection_hint",
                        ),
                    ),
            ),
        )

    val flow: DailyCheckInFlow =
        DailyCheckInFlow(
            trackLabelKey = "daily_checkin_track_act",
            pathLabelKey = "daily_checkin_act_path",
            steps =
                listOf(
                    EmotionalRecordStep(
                        id = STEP_EMOTIONAL_RECORD,
                        titleKey = "daily_checkin_emotional_record_title",
                        subtitleKey = "daily_checkin_emotional_record_subtitle",
                    ),
                    SingleSelectWithContextStep(
                        id = STEP_OBJECTIVE_REVIEW,
                        titleKey = "daily_checkin_objective_review_title",
                        subtitleKey = "daily_checkin_objective_review_subtitle",
                        options =
                            listOf(
                                OptionSpec("yes", "daily_checkin_objective_review_yes"),
                                OptionSpec("partially", "daily_checkin_objective_review_partially"),
                                OptionSpec("no", "daily_checkin_objective_review_no"),
                            ),
                        contextHintKey = "daily_checkin_objective_review_context_hint",
                        isObjectiveReview = true,
                    ),
                    TherapeuticActivityStep(
                        id = STEP_THERAPEUTIC_ACTIVITY,
                        titleKey = "daily_checkin_activity_selector_title",
                        subtitleKey = "daily_checkin_activity_selector_subtitle",
                        activityOptions = activityOptions,
                    ),
                    TextWithSuggestionsStep(
                        id = STEP_GOAL_DEFINITION,
                        titleKey = "daily_checkin_goal_definition_title",
                        subtitleKey = "daily_checkin_goal_definition_subtitle",
                        hintKey = "daily_checkin_goal_definition_hint",
                        suggestionKeys =
                            listOf(
                                "onboarding_hobby_exercise",
                                "onboarding_hobby_reading",
                                "onboarding_hobby_music",
                                "onboarding_hobby_meditation",
                                "onboarding_hobby_cooking",
                                "onboarding_hobby_hiking",
                                "onboarding_hobby_photography",
                                "onboarding_hobby_team_sports",
                                "onboarding_hobby_art",
                                "onboarding_hobby_videogames",
                                "onboarding_hobby_volunteering",
                            ),
                    ),
                    BooleanBranchStep(
                        id = STEP_RELAPSE_CHECK,
                        titleKey = "daily_checkin_relapse_check_title",
                        subtitleKey = "daily_checkin_relapse_check_subtitle",
                        yesLabelKey = "daily_checkin_relapse_check_yes",
                        noLabelKey = "daily_checkin_relapse_check_no",
                        onNo =
                            SingleSelectWithContextStep(
                                id = STEP_DAY_EVAL,
                                titleKey = "daily_checkin_day_eval_title",
                                subtitleKey = "daily_checkin_day_eval_subtitle",
                                options =
                                    listOf(
                                        OptionSpec("calm", "daily_checkin_day_eval_calm"),
                                        OptionSpec("balanced", "daily_checkin_day_eval_balanced"),
                                        OptionSpec("difficult", "daily_checkin_day_eval_difficult"),
                                    ),
                                contextHintKey = "daily_checkin_day_eval_context_hint",
                            ),
                        onYes =
                            RelapseRegistrationStep(
                                id = STEP_RELAPSE_REG,
                                titleKey = "daily_checkin_relapse_reg_title",
                                subtitleKey = "daily_checkin_relapse_reg_subtitle",
                                triggerOptions =
                                    listOf(
                                        OptionSpec("sleep", "daily_checkin_trigger_sleep"),
                                        OptionSpec("craving", "daily_checkin_trigger_craving"),
                                        OptionSpec("boredom", "daily_checkin_trigger_boredom"),
                                        OptionSpec("stress", "daily_checkin_trigger_stress"),
                                        OptionSpec("loneliness", "daily_checkin_trigger_loneliness"),
                                        OptionSpec("fatigue", "daily_checkin_trigger_fatigue"),
                                        OptionSpec("other", "daily_checkin_trigger_other"),
                                    ),
                            ),
                    ),
                ),
        )
}
