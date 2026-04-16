package com.wesley.beefree.domain.intervention.ports

import com.wesley.beefree.domain.intervention.EmiTool

interface ClinicalProfileStrategy {
    val showsWeeklyCheckIn: Boolean
    val emiTool: EmiTool
    val showsCoreValuesInEmi: Boolean
}
