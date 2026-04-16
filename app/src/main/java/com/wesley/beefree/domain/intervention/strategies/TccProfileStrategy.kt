package com.wesley.beefree.domain.intervention.strategies

import com.wesley.beefree.domain.intervention.EmiTool
import com.wesley.beefree.domain.intervention.ports.ClinicalProfileStrategy

class TccProfileStrategy : ClinicalProfileStrategy {
    override val showsWeeklyCheckIn = false
    override val emiTool = EmiTool.THOUGHT_RECORD
    override val showsCoreValuesInEmi = false
}
