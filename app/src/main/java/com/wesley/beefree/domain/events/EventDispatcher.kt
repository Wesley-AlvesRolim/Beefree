package com.wesley.beefree.domain.events

interface EventDispatcher<Event> {
    fun dispatch(
        event: Event,
        vararg args: Any?,
    )
}
