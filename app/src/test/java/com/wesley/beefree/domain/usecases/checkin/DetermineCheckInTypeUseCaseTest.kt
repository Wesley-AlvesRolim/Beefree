package com.wesley.beefree.domain.usecases.checkin

import com.wesley.beefree.domain.checkin.CheckInType
import org.junit.Assert.assertEquals
import org.junit.Test

class DetermineCheckInTypeUseCaseTest {
    private val useCase = DetermineCheckInTypeUseCase()

    @Test
    fun `always returns DAILY regardless of days elapsed`() {
        val created = 0L
        val dayMs = 24L * 60 * 60 * 1000
        listOf(0, 1, 5, 6, 7, 13, 14, 100).forEach { days ->
            assertEquals(
                "Expected DAILY on day $days",
                CheckInType.DAILY,
                useCase.execute(created, created + days * dayMs),
            )
        }
    }
}
