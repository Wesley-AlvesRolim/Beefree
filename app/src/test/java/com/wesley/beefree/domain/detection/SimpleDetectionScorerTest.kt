package com.wesley.beefree.domain.detection

import com.wesley.beefree.domain.entities.AddictionTypeEnum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SimpleDetectionScorerTest {
    private lateinit var scorer: SimpleDetectionScorer

    @Before
    fun setUp() {
        scorer = SimpleDetectionScorer()
    }

    @Test
    fun `should trigger intervention after three matches for ADULT_CONTENT`() {
        val addictionId = AddictionTypeEnum.ADULT_CONTENT.ordinal

        assertFalse(scorer.addMatch("Porn content 1", "porn1", addictionId, "com.test.app"))
        assertFalse(scorer.addMatch("Porn content 2", "porn2", addictionId, "com.test.app"))
        val triggered =
            scorer.addMatch(
                reason = "Porn content 3",
                keyword = "porn3",
                addictionTypeId = addictionId,
                appPackage = "com.test.app",
            )

        assertTrue(triggered)
        assertTrue(scorer.isTriggered())
        val intervention = scorer.getIntervention()
        assertNotNull(intervention)
        assertEquals("porn3", intervention?.keyword)
        assertEquals(addictionId, intervention?.addictionTypeId)
        assertEquals("com.test.app", intervention?.appPackage)
    }

    @Test
    fun `should trigger intervention after three matches for BETS`() {
        val addictionId = AddictionTypeEnum.BETS.ordinal

        assertFalse(scorer.addMatch("Bet site 1", "bet1", addictionId, "com.test.app"))
        assertFalse(scorer.addMatch("Bet site 2", "bet2", addictionId, "com.test.app"))
        val triggered =
            scorer.addMatch(
                reason = "Bet site 3",
                keyword = "bet3",
                addictionTypeId = addictionId,
                appPackage = "com.test.app",
            )

        assertTrue(triggered)
        assertTrue(scorer.isTriggered())
        assertNotNull(scorer.getIntervention())
        assertEquals("bet3", scorer.getIntervention()?.keyword)
        assertEquals(addictionId, scorer.getIntervention()?.addictionTypeId)
        assertEquals("com.test.app", scorer.getIntervention()?.appPackage)
    }

    @Test
    fun `should NOT trigger intervention for OTHERS with only one match`() {
        val triggered =
            scorer.addMatch(
                reason = "Some keyword",
                keyword = "something",
                addictionTypeId = AddictionTypeEnum.OTHERS.ordinal,
                appPackage = "com.test.app",
            )

        assertFalse(triggered)
        assertFalse(scorer.isTriggered())
        assertNull(scorer.getIntervention())
    }

    @Test
    fun `should trigger intervention for OTHERS after nine matches`() {
        val addictionId = AddictionTypeEnum.OTHERS.ordinal

        repeat(8) { i ->
            assertFalse(scorer.addMatch("Other $i", "k$i", addictionId, "pkg"))
        }

        val triggered = scorer.addMatch("Other 9", "k9", addictionId, "pkg")
        assertTrue(triggered)
        assertTrue(scorer.isTriggered())
        assertNotNull(scorer.getIntervention())
        assertEquals("k9", scorer.getIntervention()?.keyword)
    }

    @Test
    fun `should NOT count the same keyword twice`() {
        val addictionId = AddictionTypeEnum.BETS.ordinal

        scorer.addMatch("First time", "duplicate", addictionId, "pkg")
        scorer.addMatch("Second time", "duplicate", addictionId, "pkg")
        assertFalse(scorer.isTriggered())

        scorer.addMatch("Third time", "new_keyword", addictionId, "pkg")
        assertFalse(scorer.isTriggered())

        scorer.addMatch("Fourth time", "another_new", addictionId, "pkg")
        assertTrue(scorer.isTriggered())
    }

    @Test
    fun `should ignore keyword case when checking for duplicates`() {
        val addictionId = AddictionTypeEnum.BETS.ordinal

        scorer.addMatch("First", "BET", addictionId, "pkg")
        scorer.addMatch("Second", "bet", addictionId, "pkg")
        assertFalse(scorer.isTriggered())

        scorer.addMatch("Third", "bEt", addictionId, "pkg")
        assertFalse(scorer.isTriggered())
    }

    @Test
    fun `should reset state after reset is called`() {
        val addictionId = AddictionTypeEnum.BETS.ordinal

        scorer.addMatch("Match 1", "k1", addictionId, "pkg")
        scorer.addMatch("Match 2", "k2", addictionId, "pkg")
        scorer.addMatch("Match 3", "k3", addictionId, "pkg")
        assertTrue(scorer.isTriggered())

        scorer.reset()

        assertFalse(scorer.isTriggered())
        assertNull(scorer.getIntervention())
        assertFalse(scorer.addMatch("Match 1", "k1", addictionId, "pkg"))
    }
}
