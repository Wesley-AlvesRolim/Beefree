package com.wesley.beefree.domain.intervention.ports

import kotlinx.coroutines.flow.Flow

interface Ticker {
    fun ticks(): Flow<Unit>
}
