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
