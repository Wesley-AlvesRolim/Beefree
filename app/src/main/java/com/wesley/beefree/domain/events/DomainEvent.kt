package com.wesley.beefree.domain.events

sealed class DomainEvent {
    val timestamp: Long = System.currentTimeMillis()
}

data class ScreenContentCaptured(
    val texts: List<String>,
    val packageName: String?,
) : DomainEvent()

data class InterventionTriggered(
    val reason: String,
) : DomainEvent()
