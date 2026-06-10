package com.wesley.beefree.domain.usecases.home

import com.wesley.beefree.domain.entities.RelapseRecord
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.TimeUnit

class ComputeRelapseSuccessRateUseCaseTest {
    private val useCase = ComputeRelapseSuccessRateUseCase()

    private fun relapseOnDay(daysAgo: Int): RelapseRecord {
        val ts = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(daysAgo.toLong())
        return RelapseRecord(addictionCategoryId = 1, keywordDetected = "test", createdAt = ts)
    }

    @Test
    fun `returns 1 when no relapses`() {
        val result = useCase.execute(emptyList())
        Assert.assertEquals(1.0f, result)
    }

    @Test
    fun `returns 0 when every day in period has a relapse`() {
        val relapses = (0 until 30).map { relapseOnDay(it) }
        val result = useCase.execute(relapses)
        Assert.assertEquals(0f, result, 0.001f)
    }

    @Test
    fun `multiple relapses on same day count as one relapse day`() {
        val relapses = List(5) { relapseOnDay(0) }
        val result = useCase.execute(relapses, periodDays = 10)
        Assert.assertEquals(0.9f, result, 0.001f)
    }

    @Test
    fun `returns partial rate for some relapse days`() {
        val relapses = (0 until 15).map { relapseOnDay(it) }
        val result = useCase.execute(relapses)
        Assert.assertEquals(0.5f, result, 0.001f)
    }
}
