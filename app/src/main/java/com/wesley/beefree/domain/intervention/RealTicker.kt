package com.wesley.beefree.domain.intervention

import com.wesley.beefree.domain.intervention.ports.Ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class RealTicker : Ticker {
    override fun ticks() =
        flow {
            while (true) {
                delay(1000)
                emit(Unit)
            }
        }
}
