package com.wesley.beefree.domain.intervention

import com.wesley.beefree.domain.intervention.ports.ClinicalProfileStrategy
import com.wesley.beefree.domain.intervention.strategies.ActProfileStrategy
import com.wesley.beefree.domain.intervention.strategies.HybridProfileStrategy
import com.wesley.beefree.domain.intervention.strategies.PreventionProfileStrategy
import com.wesley.beefree.domain.intervention.strategies.TccProfileStrategy
import com.wesley.beefree.domain.onboarding.TreatmentProfile

object ClinicalProfileStrategyFactory {
    fun from(profile: TreatmentProfile): ClinicalProfileStrategy =
        when (profile) {
            TreatmentProfile.ACT -> ActProfileStrategy()
            TreatmentProfile.TCC -> TccProfileStrategy()
            TreatmentProfile.HYBRID -> HybridProfileStrategy()
            TreatmentProfile.PREVENTION -> PreventionProfileStrategy()
        }
}
