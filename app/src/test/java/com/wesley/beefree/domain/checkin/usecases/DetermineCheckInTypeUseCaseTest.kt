package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.checkin.CheckInType
import com.wesley.beefree.domain.intervention.ClinicalProfileStrategyFactory
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import org.junit.Assert.assertEquals
import org.junit.Test

class DetermineCheckInTypeUseCaseTest {
    private val useCase = DetermineCheckInTypeUseCase()

    private fun nowForDay(
        createdAt: Long,
        daysElapsed: Int,
    ): Long = createdAt + daysElapsed * 24L * 60 * 60 * 1000

    @Test
    fun `day 0 returns DAILY`() {
        val created = 0L
        assertEquals(CheckInType.DAILY, useCase.execute(created, nowForDay(created, 0)))
    }

    @Test
    fun `day 1 returns DAILY`() {
        val created = 0L
        assertEquals(CheckInType.DAILY, useCase.execute(created, nowForDay(created, 1)))
    }

    @Test
    fun `day 5 returns DAILY`() {
        val created = 0L
        assertEquals(CheckInType.DAILY, useCase.execute(created, nowForDay(created, 5)))
    }

    @Test
    fun `day 6 returns WEEKLY`() {
        val created = 0L
        assertEquals(CheckInType.WEEKLY, useCase.execute(created, nowForDay(created, 6)))
    }

    @Test
    fun `day 7 returns DAILY`() {
        val created = 0L
        assertEquals(CheckInType.DAILY, useCase.execute(created, nowForDay(created, 7)))
    }

    @Test
    fun `day 12 returns DAILY`() {
        val created = 0L
        assertEquals(CheckInType.DAILY, useCase.execute(created, nowForDay(created, 12)))
    }

    @Test
    fun `day 13 returns WEEKLY`() {
        val created = 0L
        assertEquals(CheckInType.WEEKLY, useCase.execute(created, nowForDay(created, 13)))
    }

    @Test
    fun `TCC profile always returns DAILY even on weekly day`() {
        val created = 0L
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.TCC)
        assertEquals(
            CheckInType.DAILY,
            useCase.execute(created, nowForDay(created, 6), strategy),
        )
    }

    @Test
    fun `PREVENTION profile always returns DAILY even on weekly day`() {
        val created = 0L
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.PREVENTION)
        assertEquals(
            CheckInType.DAILY,
            useCase.execute(created, nowForDay(created, 6), strategy),
        )
    }

    @Test
    fun `ACT profile returns WEEKLY on weekly day`() {
        val created = 0L
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.ACT)
        assertEquals(
            CheckInType.WEEKLY,
            useCase.execute(created, nowForDay(created, 6), strategy),
        )
    }

    @Test
    fun `HYBRID profile returns WEEKLY on weekly day`() {
        val created = 0L
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.HYBRID)
        assertEquals(
            CheckInType.WEEKLY,
            useCase.execute(created, nowForDay(created, 6), strategy),
        )
    }

    @Test
    fun `ACT profile returns DAILY on non-weekly day`() {
        val created = 0L
        val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.ACT)
        assertEquals(
            CheckInType.DAILY,
            useCase.execute(created, nowForDay(created, 5), strategy),
        )
    }
}
