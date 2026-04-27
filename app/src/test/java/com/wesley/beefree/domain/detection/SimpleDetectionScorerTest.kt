package com.wesley.beefree.domain.detection

import com.wesley.beefree.domain.entities.AddictionCategoryEnum
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
        val categoryId = AddictionCategoryEnum.ADULT_CONTENT.ordinal + 1

        assertFalse(scorer.addMatch("Porn content 1", "porn1", categoryId, "com.test.app"))
        assertFalse(scorer.addMatch("Porn content 2", "porn2", categoryId, "com.test.app"))
        val triggered =
            scorer.addMatch(
                reason = "Porn content 3",
                keyword = "porn3",
                addictionCategoryId = categoryId,
                appPackage = "com.test.app",
            )

        assertTrue(triggered)
        assertTrue(scorer.isTriggered())
        val intervention = scorer.getIntervention()
        assertNotNull(intervention)
        assertEquals("porn3", intervention?.keyword)
        assertEquals(categoryId, intervention?.addictionCategoryId)
        assertEquals("com.test.app", intervention?.appPackage)
    }

    @Test
    fun `should trigger intervention after three matches for BETS`() {
        val categoryId = AddictionCategoryEnum.BETS.ordinal + 1

        assertFalse(scorer.addMatch("Bet site 1", "bet1", categoryId, "com.test.app"))
        assertFalse(scorer.addMatch("Bet site 2", "bet2", categoryId, "com.test.app"))
        val triggered =
            scorer.addMatch(
                reason = "Bet site 3",
                keyword = "bet3",
                addictionCategoryId = categoryId,
                appPackage = "com.test.app",
            )

        assertTrue(triggered)
        assertTrue(scorer.isTriggered())
        assertNotNull(scorer.getIntervention())
        assertEquals("bet3", scorer.getIntervention()?.keyword)
        assertEquals(categoryId, scorer.getIntervention()?.addictionCategoryId)
        assertEquals("com.test.app", scorer.getIntervention()?.appPackage)
    }

    @Test
    fun `should NOT trigger intervention for OTHERS with only one match`() {
        val triggered =
            scorer.addMatch(
                reason = "Some keyword",
                keyword = "something",
                addictionCategoryId = AddictionCategoryEnum.OTHERS.ordinal,
                appPackage = "com.test.app",
            )

        assertFalse(triggered)
        assertFalse(scorer.isTriggered())
        assertNull(scorer.getIntervention())
    }

    @Test
    fun `should trigger intervention for OTHERS after nine matches`() {
        val categoryId = AddictionCategoryEnum.OTHERS.ordinal + 1

        repeat(8) { i ->
            assertFalse(scorer.addMatch("Other $i", "k$i", categoryId, "pkg"))
        }

        val triggered = scorer.addMatch("Other 9", "k9", categoryId, "pkg")
        assertTrue(triggered)
        assertTrue(scorer.isTriggered())
        assertNotNull(scorer.getIntervention())
        assertEquals("k9", scorer.getIntervention()?.keyword)
    }

    @Test
    fun `should NOT count the same keyword twice`() {
        val categoryId = AddictionCategoryEnum.BETS.ordinal + 1

        scorer.addMatch("First time", "duplicate", categoryId, "pkg")
        scorer.addMatch("Second time", "duplicate", categoryId, "pkg")
        assertFalse(scorer.isTriggered())

        scorer.addMatch("Third time", "new_keyword", categoryId, "pkg")
        assertFalse(scorer.isTriggered())

        scorer.addMatch("Fourth time", "another_new", categoryId, "pkg")
        assertTrue(scorer.isTriggered())
    }

    @Test
    fun `should ignore keyword case when checking for duplicates`() {
        val categoryId = AddictionCategoryEnum.BETS.ordinal + 1

        scorer.addMatch("First", "BET", categoryId, "pkg")
        scorer.addMatch("Second", "bet", categoryId, "pkg")
        assertFalse(scorer.isTriggered())

        scorer.addMatch("Third", "bEt", categoryId, "pkg")
        assertFalse(scorer.isTriggered())
    }

    @Test
    fun `should reset state after reset is called`() {
        val categoryId = AddictionCategoryEnum.BETS.ordinal + 1

        scorer.addMatch("Match 1", "k1", categoryId, "pkg")
        scorer.addMatch("Match 2", "k2", categoryId, "pkg")
        scorer.addMatch("Match 3", "k3", categoryId, "pkg")
        assertTrue(scorer.isTriggered())

        scorer.reset()

        assertFalse(scorer.isTriggered())
        assertNull(scorer.getIntervention())
        assertFalse(scorer.addMatch("Match 1", "k1", categoryId, "pkg"))
    }
}
