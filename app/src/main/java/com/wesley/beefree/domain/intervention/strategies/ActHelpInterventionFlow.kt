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
            ActionSuggestion("help_intervention.act_actions.meditate", "regulation"),
            ActionSuggestion("help_intervention.act_actions.pray", "regulation"),
            ActionSuggestion("help_intervention.act_actions.acknowledge_urge", "regulation"),
            ActionSuggestion("help_intervention.act_actions.stretch", "regulation"),
            ActionSuggestion("help_intervention.act_actions.leave_environment", "regulation"),
            ActionSuggestion("help_intervention.act_actions.put_phone_away", "regulation"),
            // Context change
            ActionSuggestion("help_intervention.act_actions.go_another_room", "context"),
            ActionSuggestion("help_intervention.act_actions.take_walk", "context"),
            ActionSuggestion("help_intervention.act_actions.read_5min", "context"),
            ActionSuggestion("help_intervention.act_actions.study_10min", "context"),
            ActionSuggestion("help_intervention.act_actions.journal", "context"),
            ActionSuggestion("help_intervention.act_actions.plan_tasks", "context"),
            ActionSuggestion("help_intervention.act_actions.work_10min", "context"),
            ActionSuggestion("help_intervention.act_actions.do_productive", "context"),
            // Value connection
            ActionSuggestion("help_intervention.act_actions.call_family", "values"),
            ActionSuggestion("help_intervention.act_actions.help_at_home", "values"),
            ActionSuggestion("help_intervention.act_actions.read_scripture", "values"),
            ActionSuggestion("help_intervention.act_actions.practice_gratitude", "values"),
            ActionSuggestion("help_intervention.act_actions.message_someone", "values"),
            ActionSuggestion("help_intervention.act_actions.listen_someone", "values"),
            ActionSuggestion("help_intervention.act_actions.help_someone", "values"),
            ActionSuggestion("help_intervention.act_actions.send_kind_message", "values"),
            ActionSuggestion("help_intervention.act_actions.show_affection", "values"),
            ActionSuggestion("help_intervention.act_actions.message_loved_one", "values"),
            ActionSuggestion("help_intervention.act_actions.write_feelings", "values"),
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
