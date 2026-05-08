package com.wesley.beefree.domain.intervention.strategies

import com.wesley.beefree.domain.entities.CoreValueType
import com.wesley.beefree.domain.intervention.ActionSuggestion
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.domain.shared.OptionSpec

object ActHelpInterventionFlow {
    val valuesOptions: List<OptionSpec> =
        CoreValueType.entries.map { type ->
            OptionSpec(
                id = type.name,
                labelKey = "onboarding_core_value_${type.name.lowercase()}",
            )
        }

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
