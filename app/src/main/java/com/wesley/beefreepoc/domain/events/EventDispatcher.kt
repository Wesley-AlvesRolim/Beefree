package com.wesley.beefreepoc.domain.events

interface EventDispatcher<Event> {
    fun dispatch(
        event: Event,
        vararg args: Any?,
    )
}
