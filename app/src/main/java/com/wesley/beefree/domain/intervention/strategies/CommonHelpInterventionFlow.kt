package com.wesley.beefree.domain.intervention.strategies

import com.wesley.beefree.domain.checkin.OptionSpec
import com.wesley.beefree.domain.intervention.HelpInterventionStep

object CommonHelpInterventionFlow {
    val MEDITATION_STEP_KEYS =
        listOf(
            "help_intervention.meditation.observe",
            "help_intervention.meditation.breathing",
            "help_intervention.meditation.mind_wander",
            "help_intervention.meditation.return_attention",
            "help_intervention.meditation.stay_still",
            "help_intervention.meditation.observe_breath",
            "help_intervention.meditation.observer",
            "help_intervention.meditation.no_judgment",
            "help_intervention.meditation.clouds",
            "help_intervention.meditation.accept",
            "help_intervention.meditation.no_resist",
            "help_intervention.meditation.present_moment",
        )

    private val bodyLocationOptions =
        listOf(
            OptionSpec("chest", "help_intervention.body_location.chest"),
            OptionSpec("throat", "help_intervention.body_location.throat"),
            OptionSpec("belly", "help_intervention.body_location.belly"),
            OptionSpec("hands", "help_intervention.body_location.hands"),
            OptionSpec("head", "help_intervention.body_location.head"),
            OptionSpec("other", "help_intervention.body_location.other"),
        )

    val commonSteps: List<HelpInterventionStep> =
        listOf(
            HelpInterventionStep.IntensityStep(
                titleKey = "help_intervention.initial_intensity.title",
                subtitleKey = "help_intervention.initial_intensity.subtitle",
                id = "initial_intensity",
            ),
            HelpInterventionStep.BodyLocationStep(
                titleKey = "help_intervention.body_location.title",
                options = bodyLocationOptions,
            ),
            HelpInterventionStep.UrgeSurfingStep(
                meditationStepKeys = MEDITATION_STEP_KEYS,
                intervalMs = 2000L,
            ),
            HelpInterventionStep.PostSurfIntensityStep(
                titleKey = "help_intervention.post_surf_intensity.title",
                loopThreshold = 7,
            ),
        )

    val finalSteps: List<HelpInterventionStep> =
        listOf(
            HelpInterventionStep.TimerStep(durationSeconds = 120),
            HelpInterventionStep.ReflectionStep(
                questionKey = "help_intervention.reflection.was_impulse_strong",
            ),
        )
}
