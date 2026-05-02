package com.wesley.beefree.domain.home.usecases

import com.wesley.beefree.domain.entities.RelapseRecord
import java.text.SimpleDateFormat
import java.util.Locale

class ComputeRelapseSuccessRateUseCase {
    fun execute(
        relapses: List<RelapseRecord>,
        periodDays: Int = 30,
    ): Float {
        if (periodDays == 0) return 0f
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val distinctRelapseDays = relapses.map { sdf.format(it.createdAt) }.toSet().size
        val cleanDays = (periodDays - distinctRelapseDays).coerceAtLeast(0)
        return cleanDays.toFloat() / periodDays
    }
}
