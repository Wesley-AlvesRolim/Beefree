package com.wesley.beefree.domain.treatments.intervention

import com.wesley.beefree.domain.intervention.HelpInterventionStep

object HybridHelpInterventionFlow {
    val steps: List<HelpInterventionStep> =
        listOf(
            HelpInterventionStep.ActValuesStep(
                titleKey = "help_intervention.act_values.title",
                predefinedOptions = ActHelpInterventionFlow.valuesOptions,
            ),
            HelpInterventionStep.ActDirectionStep(
                titleKey = "help_intervention.act_direction.title",
                options = ActHelpInterventionFlow.directionOptions,
            ),
            // TCC automatic thought and restructuring
            HelpInterventionStep.TccAutomaticThoughtStep(
                titleKey = "help_intervention.tcc_auto_thought.title",
                suggestions = TccHelpInterventionFlow.automaticThoughtSuggestions,
            ),
            HelpInterventionStep.TccAlternativeThoughtStep(
                titleKey = "help_intervention.tcc_restructuring.title",
                suggestions = TccHelpInterventionFlow.alternativeThoughtSuggestions,
            ),
            // ACT committed action
            HelpInterventionStep.ActCommittedActionStep(
                titleKey = "help_intervention.act_committed_action.title",
                suggestions = ActHelpInterventionFlow.actionSuggestions,
            ),
        )
}
