package com.wesley.beefree.domain.shared

import kotlinx.coroutines.flow.Flow

interface Ticker {
    fun ticks(): Flow<Unit>
}
