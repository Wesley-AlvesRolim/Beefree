package com.wesley.beefree.domain.checkin

sealed class DopamineType {
    data class Natural(
        val source: NaturalDopamineSource,
    ) : DopamineType()

    data class Artificial(
        val source: ArtificialDopamineSource,
    ) : DopamineType()
}
