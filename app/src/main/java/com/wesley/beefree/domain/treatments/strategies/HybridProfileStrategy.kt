package com.wesley.beefree.domain.treatments.strategies

import com.wesley.beefree.domain.checkin.DailyCheckInFlow
import com.wesley.beefree.domain.intervention.EmiTool
import com.wesley.beefree.domain.intervention.HelpInterventionFlow
import com.wesley.beefree.domain.treatments.checkin.ActDailyCheckInFlow
import com.wesley.beefree.domain.treatments.intervention.CommonHelpInterventionFlow
import com.wesley.beefree.domain.treatments.intervention.HybridHelpInterventionFlow
import com.wesley.beefree.domain.treatments.ports.ClinicalProfileStrategy

class HybridProfileStrategy : ClinicalProfileStrategy {
    override val showsWeeklyCheckIn = true
    override val emiTool = EmiTool.BOTH
    override val showsCoreValuesInEmi = true
    override val helpInterventionFlow: HelpInterventionFlow =
        HelpInterventionFlow(
            commonSteps = CommonHelpInterventionFlow.commonSteps,
            clinicalSteps = HybridHelpInterventionFlow.steps,
            finalSteps = CommonHelpInterventionFlow.finalSteps,
        )
    override val dailyCheckInFlow: DailyCheckInFlow = ActDailyCheckInFlow.flow
}
