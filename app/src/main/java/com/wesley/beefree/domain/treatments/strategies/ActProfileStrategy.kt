package com.wesley.beefree.domain.treatments.strategies

import com.wesley.beefree.domain.checkin.DailyCheckInFlow
import com.wesley.beefree.domain.intervention.EmiTool
import com.wesley.beefree.domain.intervention.HelpInterventionFlow
import com.wesley.beefree.domain.treatments.checkin.ActDailyCheckInFlow
import com.wesley.beefree.domain.treatments.intervention.ActHelpInterventionFlow
import com.wesley.beefree.domain.treatments.intervention.CommonHelpInterventionFlow
import com.wesley.beefree.domain.treatments.ports.ClinicalProfileStrategy

class ActProfileStrategy : ClinicalProfileStrategy {
    override val showsWeeklyCheckIn = true
    override val emiTool = EmiTool.URGE_SURFING
    override val showsCoreValuesInEmi = true
    override val helpInterventionFlow: HelpInterventionFlow =
        HelpInterventionFlow(
            commonSteps = CommonHelpInterventionFlow.commonSteps,
            clinicalSteps = ActHelpInterventionFlow.steps,
            finalSteps = CommonHelpInterventionFlow.finalSteps,
        )
    override val dailyCheckInFlow: DailyCheckInFlow = ActDailyCheckInFlow.flow
}
