package com.wesley.beefree.infrastructure.storage.adapters

import com.wesley.beefree.domain.repository.ports.KeyValueStorage
import com.wesley.beefree.domain.repository.ports.RiskWeightsRepository
import com.wesley.beefree.domain.risk.RiskWeights

class KeyValueRiskWeightsRepository(
    private val storage: KeyValueStorage,
) : RiskWeightsRepository {
    override fun getWeights(userId: Int): RiskWeights {
        val defaults = RiskWeights()
        return RiskWeights(
            sleep = storage.get(key(userId, SLEEP), defaults.sleep.toFloat()).toDouble(),
            craving = storage.get(key(userId, CRAVING), defaults.craving.toFloat()).toDouble(),
            boredom = storage.get(key(userId, BOREDOM), defaults.boredom.toFloat()).toDouble(),
            stress = storage.get(key(userId, STRESS), defaults.stress.toFloat()).toDouble(),
            loneliness = storage.get(key(userId, LONELINESS), defaults.loneliness.toFloat()).toDouble(),
            fatigue = storage.get(key(userId, FATIGUE), defaults.fatigue.toFloat()).toDouble(),
            timeSinceRelapse = storage.get(key(userId, TIME_SINCE_RELAPSE), defaults.timeSinceRelapse.toFloat()).toDouble(),
            context = storage.get(key(userId, CONTEXT), defaults.context.toFloat()).toDouble(),
            behavior = storage.get(key(userId, BEHAVIOR), defaults.behavior.toFloat()).toDouble(),
        )
    }

    override fun saveWeights(
        userId: Int,
        weights: RiskWeights,
    ) {
        storage.set(key(userId, SLEEP), weights.sleep.toFloat())
        storage.set(key(userId, CRAVING), weights.craving.toFloat())
        storage.set(key(userId, BOREDOM), weights.boredom.toFloat())
        storage.set(key(userId, STRESS), weights.stress.toFloat())
        storage.set(key(userId, LONELINESS), weights.loneliness.toFloat())
        storage.set(key(userId, FATIGUE), weights.fatigue.toFloat())
        storage.set(key(userId, TIME_SINCE_RELAPSE), weights.timeSinceRelapse.toFloat())
        storage.set(key(userId, CONTEXT), weights.context.toFloat())
        storage.set(key(userId, BEHAVIOR), weights.behavior.toFloat())
    }

    private fun key(
        userId: Int,
        field: String,
    ) = "risk_weights_${userId}_$field"

    companion object {
        private const val SLEEP = "sleep"
        private const val CRAVING = "craving"
        private const val BOREDOM = "boredom"
        private const val STRESS = "stress"
        private const val LONELINESS = "loneliness"
        private const val FATIGUE = "fatigue"
        private const val TIME_SINCE_RELAPSE = "time_since_relapse"
        private const val CONTEXT = "context"
        private const val BEHAVIOR = "behavior"
    }
}
