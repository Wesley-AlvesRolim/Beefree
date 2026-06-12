package com.wesley.beefree.domain.usecases.checkin

import com.wesley.beefree.domain.checkin.ActivityOption
import com.wesley.beefree.domain.checkin.ActivityType
import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.DailyCheckInAnswer
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.repository.ports.CheckInRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SelectTherapeuticActivityUseCaseTest {
    private val checkInRepository: CheckInRepository = mock()
    private val useCase = SelectTherapeuticActivityUseCase(checkInRepository)

    private val userId = 1
    private val now = System.currentTimeMillis()

    private val allOptions =
        listOf(
            ActivityOption(ActivityType.MINDFULNESS, "mindfulness_title", "mindfulness_desc"),
            ActivityOption(ActivityType.PROFILE_EXERCISE, "exercise_title", "exercise_desc"),
            ActivityOption(ActivityType.VIDEO, "video_title", "video_desc"),
        )

    private fun checkInWithActivity(type: ActivityType) =
        DailyCheckIn(
            userProfileId = userId,
            treatmentProfile = TreatmentProfile.TCC,
            answers = mapOf("tcc.therapeutic_activity" to DailyCheckInAnswer.TherapeuticActivity(type)),
            checkedInAt = now,
        )

    @Test
    fun `selects from all options when none used this week`() =
        runTest {
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(flowOf(emptyList()))

            val result = useCase.execute(userId, allOptions, randomSeed = 0)

            assertEquals(allOptions[0].type, result)
        }

    @Test
    fun `excludes video when weekly limit reached`() =
        runTest {
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(
                flowOf(listOf(checkInWithActivity(ActivityType.VIDEO))),
            )

            repeat(10) { seed ->
                val result = useCase.execute(userId, allOptions, randomSeed = seed)
                assertNotEquals(ActivityType.VIDEO, result)
            }
        }

    @Test
    fun `excludes mindfulness when weekly limit reached`() =
        runTest {
            val mindfulnessCheckIns =
                (1..SelectTherapeuticActivityUseCase.MINDFULNESS_WEEKLY_LIMIT)
                    .map { checkInWithActivity(ActivityType.MINDFULNESS) }

            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(
                flowOf(mindfulnessCheckIns),
            )

            repeat(10) { seed ->
                val result = useCase.execute(userId, allOptions, randomSeed = seed)
                assertNotEquals(ActivityType.MINDFULNESS, result)
            }
        }

    @Test
    fun `returns profile exercise when video and mindfulness limits both hit`() =
        runTest {
            val usedAll =
                listOf(
                    checkInWithActivity(ActivityType.VIDEO),
                    checkInWithActivity(ActivityType.MINDFULNESS),
                    checkInWithActivity(ActivityType.MINDFULNESS),
                    checkInWithActivity(ActivityType.MINDFULNESS),
                )

            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(flowOf(usedAll))

            repeat(10) { seed ->
                val result = useCase.execute(userId, allOptions, randomSeed = seed)
                assertEquals(ActivityType.PROFILE_EXERCISE, result)
            }
        }

    @Test
    fun `profile exercise is always available`() =
        runTest {
            whenever(checkInRepository.getDailyCheckIns(userId)).thenReturn(flowOf(emptyList()))

            val exerciseOnly =
                listOf(
                    ActivityOption(ActivityType.PROFILE_EXERCISE, "exercise_title", "exercise_desc"),
                )

            val result = useCase.execute(userId, exerciseOnly, randomSeed = 0)

            assertEquals(ActivityType.PROFILE_EXERCISE, result)
        }
}
