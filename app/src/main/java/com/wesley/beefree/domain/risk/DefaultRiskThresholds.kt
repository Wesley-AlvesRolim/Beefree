package com.wesley.beefree.domain.risk

import com.wesley.beefree.domain.risk.ports.RiskThresholds

class DefaultRiskThresholds : RiskThresholds {
    override val mediumRisk: Int = 30
    override val highRisk: Int = 60
}
