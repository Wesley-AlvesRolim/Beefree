package com.wesley.beefree.domain.risk

import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.risk.ports.RiskEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RiskEngineTest {
    private val riskThresholds = DefaultRiskThresholds()

    private companion object {
        const val USER_PROFILE_ID = 1
        const val DEFAULT_HOUR_OF_DAY = 12
        const val WEDNESDAY = 3
        const val DEFAULT_DAY_OF_WEEK = WEDNESDAY
        const val NIGHT_HOUR_START = 0
        const val NIGHT_HOUR_END = 5
        const val MIDNIGHT_HOUR = 2
        const val NO_CONTEXT_BONUS_HOUR = 6
        const val FRIDAY = 5
        const val SATURDAY = 6
        const val SUNDAY = 7
        const val MAX_FEATURE_SCORE = 10
        const val HIGH_FEATURE_SCORE = 8
        const val LOW_FEATURE_SCORE = 2
        const val ZERO = 0
        const val ONE = 1
        const val PEAK_RELAPSE_HOURS = 720
        const val FADE_OUT_RELAPSE_HOURS = 2160
        const val EXTREME_RELAPSE_HOURS = 1000
        const val MISSING_CHECKINS_EXCEEDED = 5
        const val MAX_SCORE = 1.0
        const val CLASSIFICATION_MARGIN = 0.01
        const val EPSILON = 0.0001
        const val ZERO_DOUBLE = 0.0
    }

    private val riskEngine: RiskEngine = DefaultRiskEngine()
    private val snapshot =
        RiskFeatureSnapshot(
            userProfileId = USER_PROFILE_ID,
            sleep = ZERO,
            craving = ZERO,
            boredom = ZERO,
            stress = ZERO,
            loneliness = ZERO,
            fatigue = ZERO,
            hoursSinceLastRelapse = ZERO,
            hourOfDay = DEFAULT_HOUR_OF_DAY,
            dayOfWeek = DEFAULT_DAY_OF_WEEK,
            timeSinceLastAppOpen = null,
            missingCheckins = ZERO,
            recentIntenseUsage = ZERO,
            createdAt = ZERO.toLong(),
        )

    @Test
    fun `zero emotions produces low score`() {
        val score = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = DEFAULT_DAY_OF_WEEK)
        assertEquals(RiskLevel.LOW, riskEngine.classify(score))
    }

    @Test
    fun `all risk factors at worst produces high risk`() {
        val bad =
            snapshot.copy(
                sleep = ZERO,
                craving = MAX_FEATURE_SCORE,
                boredom = MAX_FEATURE_SCORE,
                stress = MAX_FEATURE_SCORE,
                loneliness = MAX_FEATURE_SCORE,
                fatigue = MAX_FEATURE_SCORE,
            )
        val score = riskEngine.calculateScore(bad, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = DEFAULT_DAY_OF_WEEK)
        assertEquals(RiskLevel.HIGH, riskEngine.classify(score))
    }

    @Test
    fun `good sleep lowers score than poor sleep`() {
        val goodSleep = snapshot.copy(sleep = MAX_FEATURE_SCORE)
        val poorSleep = snapshot.copy(sleep = ZERO)
        val scoreGood =
            riskEngine.calculateScore(
                goodSleep,
                RiskWeights(),
                hourOfDay = DEFAULT_HOUR_OF_DAY,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        val scorePoor =
            riskEngine.calculateScore(
                poorSleep,
                RiskWeights(),
                hourOfDay = DEFAULT_HOUR_OF_DAY,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        assertTrue(scorePoor > scoreGood)
    }

    @Test
    fun `max sleep adds no risk contribution`() {
        val goodSleep = snapshot.copy(sleep = MAX_FEATURE_SCORE)
        val score = riskEngine.calculateScore(goodSleep, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = DEFAULT_DAY_OF_WEEK)
        assertEquals(ZERO_DOUBLE, score, EPSILON)
    }

    @Test
    fun `moderate emotions produce medium risk`() {
        val moderate =
            snapshot.copy(
                sleep = 5,
                craving = 5,
                boredom = 5,
                stress = 5,
                loneliness = 5,
                fatigue = 5,
            )
        val score = riskEngine.calculateScore(moderate, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = DEFAULT_DAY_OF_WEEK)
        assertEquals(RiskLevel.MEDIUM, riskEngine.classify(score))
    }

    @Test
    fun `midnight hour adds context bonus`() {
        val scoreDay = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = DEFAULT_DAY_OF_WEEK)
        val scoreMidnight = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = MIDNIGHT_HOUR, dayOfWeek = DEFAULT_DAY_OF_WEEK)
        assertTrue(scoreMidnight > scoreDay)
    }

    @Test
    fun `hour zero adds context bonus`() {
        val scoreDay = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = DEFAULT_DAY_OF_WEEK)
        val scoreHourZero =
            riskEngine.calculateScore(
                snapshot,
                RiskWeights(),
                hourOfDay = NIGHT_HOUR_START,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        assertTrue(scoreHourZero > scoreDay)
    }

    @Test
    fun `hour five adds context bonus`() {
        val scoreDay = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = DEFAULT_DAY_OF_WEEK)
        val scoreHourFive = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = NIGHT_HOUR_END, dayOfWeek = DEFAULT_DAY_OF_WEEK)
        assertTrue(scoreHourFive > scoreDay)
    }

    @Test
    fun `hour six does not add context bonus`() {
        val scoreSix =
            riskEngine.calculateScore(
                snapshot,
                RiskWeights(),
                hourOfDay = NO_CONTEXT_BONUS_HOUR,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        val scoreNoon = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = DEFAULT_DAY_OF_WEEK)
        assertEquals(scoreNoon, scoreSix, EPSILON)
    }

    @Test
    fun `weekend adds context bonus`() {
        val scoreWeekday =
            riskEngine.calculateScore(
                snapshot,
                RiskWeights(),
                hourOfDay = DEFAULT_HOUR_OF_DAY,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        val scoreWeekend = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = SUNDAY)
        assertTrue(scoreWeekend > scoreWeekday)
    }

    @Test
    fun `saturday adds context bonus`() {
        val scoreWeekday =
            riskEngine.calculateScore(
                snapshot,
                RiskWeights(),
                hourOfDay = DEFAULT_HOUR_OF_DAY,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        val scoreSaturday = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = SATURDAY)
        assertTrue(scoreSaturday > scoreWeekday)
    }

    @Test
    fun `friday does not add weekend context bonus`() {
        val scoreFriday = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = FRIDAY)
        val scoreWednesday = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = WEDNESDAY)
        assertEquals(scoreWednesday, scoreFriday, EPSILON)
    }

    @Test
    fun `missing checkins increases score`() {
        val withMissing = snapshot.copy(missingCheckins = ONE)
        val baseScore = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = DEFAULT_DAY_OF_WEEK)
        val scoreWithMissing =
            riskEngine.calculateScore(
                withMissing,
                RiskWeights(),
                hourOfDay = DEFAULT_HOUR_OF_DAY,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        assertTrue(scoreWithMissing > baseScore)
    }

    @Test
    fun `recent intense usage increases score`() {
        val withUsage = snapshot.copy(recentIntenseUsage = ONE)
        val baseScore = riskEngine.calculateScore(snapshot, RiskWeights(), hourOfDay = DEFAULT_HOUR_OF_DAY, dayOfWeek = DEFAULT_DAY_OF_WEEK)
        val scoreWithUsage =
            riskEngine.calculateScore(
                withUsage,
                RiskWeights(),
                hourOfDay = DEFAULT_HOUR_OF_DAY,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        assertTrue(scoreWithUsage > baseScore)
    }

    @Test
    fun `relapse at peak day produces higher score than day zero`() {
        val atDayZero = snapshot.copy(hoursSinceLastRelapse = ZERO)
        val atPeakDay = snapshot.copy(hoursSinceLastRelapse = PEAK_RELAPSE_HOURS)
        val scoreAtZero =
            riskEngine.calculateScore(
                atDayZero,
                RiskWeights(),
                hourOfDay = DEFAULT_HOUR_OF_DAY,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        val scoreAtPeak =
            riskEngine.calculateScore(
                atPeakDay,
                RiskWeights(),
                hourOfDay = DEFAULT_HOUR_OF_DAY,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        assertTrue(scoreAtPeak > scoreAtZero)
    }

    @Test
    fun `relapse past 90 days produces no time based contribution`() {
        val atDayZero = snapshot.copy(hoursSinceLastRelapse = ZERO)
        val pastFadeOut = snapshot.copy(hoursSinceLastRelapse = FADE_OUT_RELAPSE_HOURS)
        val scoreAtZero =
            riskEngine.calculateScore(
                atDayZero,
                RiskWeights(),
                hourOfDay = DEFAULT_HOUR_OF_DAY,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        val scorePastFadeOut =
            riskEngine.calculateScore(
                pastFadeOut,
                RiskWeights(),
                hourOfDay = DEFAULT_HOUR_OF_DAY,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        assertEquals(scoreAtZero, scorePastFadeOut, EPSILON)
    }

    @Test
    fun `relapse peak score higher than after fadeout`() {
        val atPeakDay = snapshot.copy(hoursSinceLastRelapse = PEAK_RELAPSE_HOURS)
        val afterFadeOut = snapshot.copy(hoursSinceLastRelapse = FADE_OUT_RELAPSE_HOURS)
        val scoreAtPeak =
            riskEngine.calculateScore(
                atPeakDay,
                RiskWeights(),
                hourOfDay = DEFAULT_HOUR_OF_DAY,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        val scoreAfterFadeOut =
            riskEngine.calculateScore(
                afterFadeOut,
                RiskWeights(),
                hourOfDay = DEFAULT_HOUR_OF_DAY,
                dayOfWeek = DEFAULT_DAY_OF_WEEK,
            )
        assertTrue(scoreAtPeak > scoreAfterFadeOut)
    }

    @Test
    fun `score is clamped`() {
        val extremeSnapshot =
            snapshot.copy(
                craving = MAX_FEATURE_SCORE,
                stress = MAX_FEATURE_SCORE,
                loneliness = MAX_FEATURE_SCORE,
                fatigue = MAX_FEATURE_SCORE,
                sleep = MAX_FEATURE_SCORE,
                boredom = MAX_FEATURE_SCORE,
                hoursSinceLastRelapse = EXTREME_RELAPSE_HOURS,
                missingCheckins = MISSING_CHECKINS_EXCEEDED,
                recentIntenseUsage = ONE,
            )
        val score = riskEngine.calculateScore(extremeSnapshot, RiskWeights(), hourOfDay = MIDNIGHT_HOUR, dayOfWeek = SUNDAY)
        assertTrue(score <= MAX_SCORE)
    }

    @Test
    fun `classify low`() {
        assertEquals(RiskLevel.LOW, riskEngine.classify(ZERO_DOUBLE))
        assertEquals(RiskLevel.LOW, riskEngine.classify(riskThresholds.mediumRisk / 100.0 - CLASSIFICATION_MARGIN))
    }

    @Test
    fun `classify medium`() {
        assertEquals(RiskLevel.MEDIUM, riskEngine.classify(riskThresholds.mediumRisk / 100.0))
        assertEquals(RiskLevel.MEDIUM, riskEngine.classify(riskThresholds.highRisk / 100.0 - CLASSIFICATION_MARGIN))
    }

    @Test
    fun `classify high`() {
        assertEquals(RiskLevel.HIGH, riskEngine.classify(riskThresholds.highRisk / 100.0))
        assertEquals(RiskLevel.HIGH, riskEngine.classify(MAX_SCORE))
    }

    @Test
    fun `adjust weights increases craving weight when high craving`() {
        val highCraving = snapshot.copy(craving = HIGH_FEATURE_SCORE)
        val original = RiskWeights()
        val adjusted = riskEngine.adjustWeights(highCraving, original)
        assertTrue(adjusted.craving > original.craving)
    }

    @Test
    fun `adjust weights increases stress weight when high stress`() {
        val highStress = snapshot.copy(stress = HIGH_FEATURE_SCORE)
        val original = RiskWeights()
        val adjusted = riskEngine.adjustWeights(highStress, original)
        assertTrue(adjusted.stress > original.stress)
    }

    @Test
    fun `adjust weights increases loneliness weight when high loneliness`() {
        val highLoneliness = snapshot.copy(loneliness = HIGH_FEATURE_SCORE)
        val original = RiskWeights()
        val adjusted = riskEngine.adjustWeights(highLoneliness, original)
        assertTrue(adjusted.loneliness > original.loneliness)
    }

    @Test
    fun `adjust weights increases fatigue weight when high fatigue`() {
        val highFatigue = snapshot.copy(fatigue = HIGH_FEATURE_SCORE)
        val original = RiskWeights()
        val adjusted = riskEngine.adjustWeights(highFatigue, original)
        assertTrue(adjusted.fatigue > original.fatigue)
    }

    @Test
    fun `adjust weights increases sleep weight when poor sleep`() {
        val poorSleep = snapshot.copy(sleep = LOW_FEATURE_SCORE)
        val original = RiskWeights()
        val adjusted = riskEngine.adjustWeights(poorSleep, original)
        assertTrue(adjusted.sleep > original.sleep)
    }

    @Test
    fun `adjust weights does not increase sleep weight when good sleep`() {
        val goodSleep = snapshot.copy(sleep = MAX_FEATURE_SCORE)
        val original = riskEngine.adjustWeights(goodSleep, RiskWeights())
        val baseline = RiskWeights()
        assertTrue(original.sleep <= baseline.sleep + EPSILON)
    }

    @Test
    fun `adjust weights normalizes weights to sum one`() {
        val highAll =
            snapshot.copy(
                craving = MAX_FEATURE_SCORE,
                stress = MAX_FEATURE_SCORE,
                loneliness = MAX_FEATURE_SCORE,
                fatigue = MAX_FEATURE_SCORE,
                sleep = MAX_FEATURE_SCORE,
            )
        val adjusted = riskEngine.adjustWeights(highAll, RiskWeights())
        val sum =
            adjusted.sleep + adjusted.craving + adjusted.boredom +
                adjusted.stress + adjusted.loneliness + adjusted.fatigue +
                adjusted.timeSinceRelapse + adjusted.context + adjusted.behavior
        assertEquals(MAX_SCORE, sum, EPSILON)
    }

    @Test
    fun `adjust weights with no thresholds exceeded keeps same ratios`() {
        val normal =
            snapshot.copy(
                craving = DEFAULT_DAY_OF_WEEK,
                stress = DEFAULT_DAY_OF_WEEK,
                loneliness = DEFAULT_DAY_OF_WEEK,
                fatigue = DEFAULT_DAY_OF_WEEK,
                sleep = MAX_FEATURE_SCORE,
            )
        val original = RiskWeights()
        val adjusted = riskEngine.adjustWeights(normal, original)
        assertEquals(original.craving, adjusted.craving, EPSILON)
        assertEquals(original.stress, adjusted.stress, EPSILON)
    }
}
