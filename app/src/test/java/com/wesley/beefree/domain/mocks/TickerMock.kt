package com.wesley.beefree.domain.mocks

import com.wesley.beefree.domain.shared.Ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TickerMock : Ticker {
    val flow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    override fun ticks(): Flow<Unit> = flow
}
