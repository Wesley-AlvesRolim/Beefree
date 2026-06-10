package com.wesley.beefree.domain.treatments.strategies

import com.wesley.beefree.domain.checkin.DailyCheckInFlow
import com.wesley.beefree.domain.intervention.EmiTool
import com.wesley.beefree.domain.intervention.HelpInterventionFlow
import com.wesley.beefree.domain.treatments.checkin.TccDailyCheckInFlow
import com.wesley.beefree.domain.treatments.intervention.CommonHelpInterventionFlow
import com.wesley.beefree.domain.treatments.intervention.TccHelpInterventionFlow
import com.wesley.beefree.domain.treatments.ports.ClinicalProfileStrategy

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
    override val dailyCheckInFlow: DailyCheckInFlow = TccDailyCheckInFlow.flow
}
