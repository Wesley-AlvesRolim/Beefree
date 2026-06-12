package com.wesley.beefree.domain.treatments.ports

import com.wesley.beefree.domain.checkin.DailyCheckInFlow
import com.wesley.beefree.domain.intervention.EmiTool
import com.wesley.beefree.domain.intervention.HelpInterventionFlow

interface ClinicalProfileStrategy {
    val showsWeeklyCheckIn: Boolean
    val emiTool: EmiTool
    val showsCoreValuesInEmi: Boolean
    val helpInterventionFlow: HelpInterventionFlow
    val dailyCheckInFlow: DailyCheckInFlow
}
