package com.wesley.beefree.domain.treatments.factories

import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.treatments.ports.ClinicalProfileStrategy
import com.wesley.beefree.domain.treatments.strategies.ActProfileStrategy
import com.wesley.beefree.domain.treatments.strategies.HybridProfileStrategy
import com.wesley.beefree.domain.treatments.strategies.PreventionProfileStrategy
import com.wesley.beefree.domain.treatments.strategies.TccProfileStrategy

object ClinicalProfileStrategyFactory {
    fun from(profile: TreatmentProfile): ClinicalProfileStrategy =
        when (profile) {
            TreatmentProfile.ACT -> ActProfileStrategy()
            TreatmentProfile.TCC -> TccProfileStrategy()
            TreatmentProfile.HYBRID -> HybridProfileStrategy()
            TreatmentProfile.PREVENTION -> PreventionProfileStrategy()
        }
}
