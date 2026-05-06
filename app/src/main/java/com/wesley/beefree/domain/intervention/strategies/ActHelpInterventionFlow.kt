package com.wesley.beefree.domain.intervention.strategies

import com.wesley.beefree.domain.checkin.OptionSpec
import com.wesley.beefree.domain.intervention.ActionSuggestion
import com.wesley.beefree.domain.intervention.HelpInterventionStep

object ActHelpInterventionFlow {
    val valuesOptions =
        listOf(
            OptionSpec("disciplined", "help_intervention.act_values.disciplined"),
            OptionSpec("present", "help_intervention.act_values.present"),
            OptionSpec("honest", "help_intervention.act_values.honest"),
            OptionSpec("healthy", "help_intervention.act_values.healthy"),
            OptionSpec("responsible", "help_intervention.act_values.responsible"),
            OptionSpec("calm", "help_intervention.act_values.calm"),
            OptionSpec("focused", "help_intervention.act_values.focused"),
            OptionSpec("resilient", "help_intervention.act_values.resilient"),
            OptionSpec("selfcontrolled", "help_intervention.act_values.selfcontrolled"),
            OptionSpec("kind_to_self", "help_intervention.act_values.kind_to_self"),
        )

    val directionOptions =
        listOf(
            OptionSpec("approximates", "help_intervention.act_direction.approximates"),
            OptionSpec("distances", "help_intervention.act_direction.distances"),
            OptionSpec("uncertain", "help_intervention.act_direction.uncertain"),
        )

    val actionSuggestions =
        listOf(
            // Regulation
            ActionSuggestion("help_intervention.act_actions.breathe_2min", "regulation"),
            ActionSuggestion("help_intervention.act_actions.drink_water", "regulation"),
            ActionSuggestion("help_intervention.act_actions.leave_environment", "regulation"),
            // Context change
            ActionSuggestion("help_intervention.act_actions.block_app", "context"),
            ActionSuggestion("help_intervention.act_actions.go_another_room", "context"),
            ActionSuggestion("help_intervention.act_actions.take_walk", "context"),
            // Value connection
            ActionSuggestion("help_intervention.act_actions.message_someone", "values"),
            ActionSuggestion("help_intervention.act_actions.work_10min", "values"),
            ActionSuggestion("help_intervention.act_actions.do_productive", "values"),
        )

    val steps: List<HelpInterventionStep> =
        listOf(
            HelpInterventionStep.ActValuesStep(
                titleKey = "help_intervention.act_values.title",
                predefinedOptions = valuesOptions,
            ),
            HelpInterventionStep.ActDirectionStep(
                titleKey = "help_intervention.act_direction.title",
                options = directionOptions,
            ),
            HelpInterventionStep.ActCommittedActionStep(
                titleKey = "help_intervention.act_committed_action.title",
                suggestions = actionSuggestions,
            ),
        )
}
