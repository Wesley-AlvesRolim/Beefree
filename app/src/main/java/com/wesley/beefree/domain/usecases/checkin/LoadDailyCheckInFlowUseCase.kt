package com.wesley.beefree.domain.usecases.checkin

import com.wesley.beefree.domain.checkin.DailyCheckInFlow
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.treatments.factories.ClinicalProfileStrategyFactory

class LoadDailyCheckInFlowUseCase {
    fun execute(profile: TreatmentProfile): DailyCheckInFlow = ClinicalProfileStrategyFactory.from(profile).dailyCheckInFlow
}
