package com.wesley.beefree.domain.intervention.strategies

import com.wesley.beefree.domain.intervention.ActionSuggestion
import com.wesley.beefree.domain.intervention.HelpInterventionStep

object TccHelpInterventionFlow {
    val automaticThoughtSuggestions =
        listOf(
            "help_intervention.tcc_auto_thought.just_once",
            "help_intervention.tcc_auto_thought.wont_resist",
            "help_intervention.tcc_auto_thought.no_difference",
            "help_intervention.tcc_auto_thought.already_ruined",
            "help_intervention.tcc_auto_thought.deserve_it",
        )

    val evidenceAgainstSuggestions =
        listOf(
            "help_intervention.tcc_evidence_against.resisted_before",
            "help_intervention.tcc_evidence_against.it_passes",
            "help_intervention.tcc_evidence_against.no_need_act",
            "help_intervention.tcc_evidence_against.will_worsen",
        )

    val alternativeThoughtSuggestions =
        listOf(
            "help_intervention.tcc_alternative.just_impulse",
            "help_intervention.tcc_alternative.dont_need_act",
            "help_intervention.tcc_alternative.can_choose",
            "help_intervention.tcc_alternative.not_who_i_am",
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
            HelpInterventionStep.TccAutomaticThoughtStep(
                titleKey = "help_intervention.tcc_auto_thought.title",
                suggestions = automaticThoughtSuggestions,
            ),
            HelpInterventionStep.TccEvidenceForStep(
                titleKey = "help_intervention.tcc_evidence_for.title",
            ),
            HelpInterventionStep.TccEvidenceAgainstStep(
                titleKey = "help_intervention.tcc_evidence_against.title",
                suggestions = evidenceAgainstSuggestions,
            ),
            HelpInterventionStep.TccAlternativeThoughtStep(
                titleKey = "help_intervention.tcc_alternative_thought.title",
                suggestions = alternativeThoughtSuggestions,
            ),
            HelpInterventionStep.TccActionStep(
                titleKey = "help_intervention.tcc_action.title",
                suggestions = actionSuggestions,
            ),
        )
}
