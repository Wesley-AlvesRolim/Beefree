package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.checkin.DailyCheckInFlow
import com.wesley.beefree.domain.intervention.ClinicalProfileStrategyFactory
import com.wesley.beefree.domain.onboarding.TreatmentProfile

class LoadDailyCheckInFlowUseCase {
    fun execute(profile: TreatmentProfile): DailyCheckInFlow = ClinicalProfileStrategyFactory.from(profile).dailyCheckInFlow
}
