package com.wesley.beefree.domain.intervention.strategies

import com.wesley.beefree.domain.intervention.EmiTool
import com.wesley.beefree.domain.intervention.ports.ClinicalProfileStrategy

class HybridProfileStrategy : ClinicalProfileStrategy {
    override val showsWeeklyCheckIn = true
    override val emiTool = EmiTool.BOTH
    override val showsCoreValuesInEmi = true
}
