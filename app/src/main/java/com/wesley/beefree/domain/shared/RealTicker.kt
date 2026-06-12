package com.wesley.beefree.domain.shared

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
