package com.wesley.beefree.domain.intervention.strategies

import com.wesley.beefree.domain.intervention.EmiTool
import com.wesley.beefree.domain.intervention.HelpInterventionFlow
import com.wesley.beefree.domain.intervention.ports.ClinicalProfileStrategy

class PreventionProfileStrategy : ClinicalProfileStrategy {
    override val showsWeeklyCheckIn = false
    override val emiTool = EmiTool.THOUGHT_RECORD
    override val showsCoreValuesInEmi = false
    override val helpInterventionFlow: HelpInterventionFlow =
        HelpInterventionFlow(
            commonSteps = CommonHelpInterventionFlow.commonSteps,
            clinicalSteps = TccHelpInterventionFlow.steps,
            finalSteps = CommonHelpInterventionFlow.finalSteps,
        )
}
