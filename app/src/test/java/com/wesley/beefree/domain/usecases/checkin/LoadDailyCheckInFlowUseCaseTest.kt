package com.wesley.beefree.domain.usecases.checkin

import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.treatments.checkin.ActDailyCheckInFlow
import com.wesley.beefree.domain.treatments.checkin.TccDailyCheckInFlow
import org.junit.Assert.assertEquals
import org.junit.Test

class LoadDailyCheckInFlowUseCaseTest {
    private val useCase = LoadDailyCheckInFlowUseCase()

    @Test
    fun `tcc profile loads tcc flow`() {
        val flow = useCase.execute(TreatmentProfile.TCC)
        assertEquals(TccDailyCheckInFlow.flow, flow)
    }

    @Test
    fun `act profile loads act flow`() {
        val flow = useCase.execute(TreatmentProfile.ACT)
        assertEquals(ActDailyCheckInFlow.flow, flow)
    }

    @Test
    fun `hybrid profile loads act flow`() {
        val flow = useCase.execute(TreatmentProfile.HYBRID)
        assertEquals(ActDailyCheckInFlow.flow, flow)
    }

    @Test
    fun `prevention profile falls back to tcc flow`() {
        val flow = useCase.execute(TreatmentProfile.PREVENTION)
        assertEquals(TccDailyCheckInFlow.flow, flow)
    }
}
