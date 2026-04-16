package com.wesley.beefree.domain.intervention.strategies

import com.wesley.beefree.domain.intervention.EmiTool
import com.wesley.beefree.domain.intervention.ports.ClinicalProfileStrategy

class ActProfileStrategy : ClinicalProfileStrategy {
    override val showsWeeklyCheckIn = true
    override val emiTool = EmiTool.URGE_SURFING
    override val showsCoreValuesInEmi = true
}
