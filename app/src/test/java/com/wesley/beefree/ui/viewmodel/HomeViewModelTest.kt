package com.wesley.beefree.ui.viewmodel

import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskTrigger
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.home.usecases.HomeData
import com.wesley.beefree.domain.home.usecases.LoadHomeDataUseCase
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val userId = 1
    private val user = UserProfile(id = userId, profileName = "Test", createdAt = 0L, updatedAt = 0L)

    private lateinit var loadHomeDataUseCase: LoadHomeDataUseCase

    @Before
    fun setUp() {
        loadHomeDataUseCase = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `navigate to check-in emits CheckIn`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val viewModel = createViewModel()
            val event = async { viewModel.navigationEvents.first() }
            advanceUntilIdle()

            viewModel.navigateToCheckIn()
            advanceUntilIdle()

            assertEquals(HomeNavigationDestination.CheckIn, event.await())
        }

    @Test
    fun `navigate to recovery trajectory emits RecoveryTrajectory`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val viewModel = createViewModel()
            val event = async { viewModel.navigationEvents.first() }
            advanceUntilIdle()

            viewModel.navigateToRecoveryTrajectory()
            advanceUntilIdle()

            assertEquals(HomeNavigationDestination.RecoveryTrajectory, event.await())
        }

    @Test
    fun `navigate to feeling details emits FeelingDetails`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val viewModel = createViewModel()
            val event = async { viewModel.navigationEvents.first() }
            advanceUntilIdle()

            viewModel.navigateToFeelingDetails()
            advanceUntilIdle()

            assertEquals(HomeNavigationDestination.FeelingDetails, event.await())
        }

    @Test
    fun `navigate to help intervention defaults to FAB source`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val viewModel = createViewModel()
            val event = async { viewModel.navigationEvents.first() }
            advanceUntilIdle()

            viewModel.navigateToHelpIntervention()
            advanceUntilIdle()

            assertEquals(HomeNavigationDestination.HelpIntervention(HelpInterventionSource.FAB), event.await())
        }

    @Test
    fun `navigate to help intervention with WIDGET source`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val viewModel = createViewModel()
            val event = async { viewModel.navigationEvents.first() }
            advanceUntilIdle()

            viewModel.navigateToHelpIntervention(HelpInterventionSource.WIDGET)
            advanceUntilIdle()

            assertEquals(HomeNavigationDestination.HelpIntervention(HelpInterventionSource.WIDGET), event.await())
        }

    @Test
    fun `navigate to trigger map emits TriggerMap`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val viewModel = createViewModel()
            val event = async { viewModel.navigationEvents.first() }
            advanceUntilIdle()

            viewModel.navigateToTriggerMap()
            advanceUntilIdle()

            assertEquals(HomeNavigationDestination.TriggerMap, event.await())
        }

    @Test
    fun `reset error clears error from state`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val viewModel = createViewModel()
            advanceUntilIdle()

            assertNull(viewModel.uiState.value.error)

            viewModel.resetError()
            assertNull(viewModel.uiState.value.error)
        }

    @Test
    fun `successful load populates user and clears loading`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val result =
                HomeData.Success(
                    user = user,
                    psychoeducationMessage = "test message",
                    relapseHistory = emptyList(),
                    relapseSuccessRate = 1f,
                    emotionRecords = emptyList(),
                    hasCheckedInToday = false,
                    treatmentProfile = TreatmentProfile.ACT,
                    todayRiskAssessments = emptyList(),
                    alignedDays = 30,
                    topTriggers = emptyList(),
                )
            val viewModel = createViewModel(homeDataResult = result)
            viewModel.refresh()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertNull(state.error)
            assertEquals(user, state.user)
        }

    @Test
    fun `onboarding required clears loading and defers navigation`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val viewModel = createViewModel(homeDataResult = HomeData.OnboardingRequired)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertNull(state.error)
        }

    @Test
    fun `relapses older than 30 days are excluded from history`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val recentRelapse = RelapseRecord(addictionCategoryId = 1, keywordDetected = "k1", createdAt = System.currentTimeMillis())
            val result =
                HomeData.Success(
                    user = user,
                    psychoeducationMessage = "test",
                    relapseHistory = listOf(recentRelapse),
                    relapseSuccessRate = 0.9f,
                    emotionRecords = emptyList(),
                    hasCheckedInToday = false,
                    treatmentProfile = TreatmentProfile.ACT,
                    todayRiskAssessments = emptyList(),
                    alignedDays = 29,
                    topTriggers = emptyList(),
                )
            val viewModel = createViewModel(homeDataResult = result)
            viewModel.refresh()
            advanceUntilIdle()

            val relapses = viewModel.uiState.value.relapseHistory
            assertEquals(1, relapses.size)
            assertEquals("k1", relapses.first().keywordDetected)
        }

    @Test
    fun topTriggersRankedByLatestRiskFeatureSnapshot() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val triggers =
                listOf(
                    RiskTrigger.CRAVING to 90,
                    RiskTrigger.STRESS to 70,
                    RiskTrigger.FATIGUE to 50,
                )
            val result =
                HomeData.Success(
                    user = user,
                    psychoeducationMessage = "test",
                    relapseHistory = emptyList(),
                    relapseSuccessRate = 1f,
                    emotionRecords = emptyList(),
                    hasCheckedInToday = false,
                    treatmentProfile = TreatmentProfile.ACT,
                    todayRiskAssessments = emptyList(),
                    alignedDays = 30,
                    topTriggers = triggers,
                )
            val viewModel = createViewModel(homeDataResult = result)
            viewModel.refresh()
            advanceUntilIdle()

            val topTriggers = viewModel.uiState.value.topTriggers
            assertEquals(3, topTriggers.size)
            assertEquals(RiskTrigger.CRAVING, topTriggers[0].first)
            assertEquals(90, topTriggers[0].second)
            assertEquals(RiskTrigger.STRESS, topTriggers[1].first)
            assertEquals(70, topTriggers[1].second)
            assertEquals(RiskTrigger.FATIGUE, topTriggers[2].first)
            assertEquals(50, topTriggers[2].second)
        }

    @Test
    fun topTriggersEmptyWhenNoSnapshotExists() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val result =
                HomeData.Success(
                    user = user,
                    psychoeducationMessage = "test",
                    relapseHistory = emptyList(),
                    relapseSuccessRate = 1f,
                    emotionRecords = emptyList(),
                    hasCheckedInToday = false,
                    treatmentProfile = TreatmentProfile.ACT,
                    todayRiskAssessments = emptyList(),
                    alignedDays = 30,
                    topTriggers = emptyList(),
                )
            val viewModel = createViewModel(homeDataResult = result)
            viewModel.refresh()
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.topTriggers
                    .isEmpty(),
            )
        }

    @Test
    fun `treatment profile comes from last daily check-in`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val result =
                HomeData.Success(
                    user = user,
                    psychoeducationMessage = "test",
                    relapseHistory = emptyList(),
                    relapseSuccessRate = 1f,
                    emotionRecords = emptyList(),
                    hasCheckedInToday = false,
                    treatmentProfile = TreatmentProfile.TCC,
                    todayRiskAssessments = emptyList(),
                    alignedDays = 30,
                    topTriggers = emptyList(),
                )
            val viewModel = createViewModel(homeDataResult = result)
            viewModel.refresh()
            advanceUntilIdle()

            assertEquals(TreatmentProfile.TCC, viewModel.uiState.value.treatmentProfile)
        }

    @Test
    fun `today risk assessments are populated from repository`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val assessments =
                listOf(
                    RiskAssessment(
                        userProfileId = userId,
                        riskScore = 75,
                        timeWindow =
                            Calendar
                                .getInstance()
                                .apply {
                                    set(Calendar.HOUR_OF_DAY, 0)
                                }.timeInMillis
                                .toString(),
                        createdAt = System.currentTimeMillis(),
                    ),
                    RiskAssessment(
                        userProfileId = userId,
                        riskScore = 30,
                        timeWindow =
                            Calendar
                                .getInstance()
                                .apply {
                                    set(Calendar.HOUR_OF_DAY, 6)
                                }.timeInMillis
                                .toString(),
                        createdAt = System.currentTimeMillis(),
                    ),
                )
            val result =
                HomeData.Success(
                    user = user,
                    psychoeducationMessage = "test",
                    relapseHistory = emptyList(),
                    relapseSuccessRate = 1f,
                    emotionRecords = emptyList(),
                    hasCheckedInToday = false,
                    treatmentProfile = TreatmentProfile.ACT,
                    todayRiskAssessments = assessments,
                    alignedDays = 30,
                    topTriggers = emptyList(),
                )
            val viewModel = createViewModel(homeDataResult = result)
            viewModel.refresh()
            advanceUntilIdle()

            assertEquals(2, viewModel.uiState.value.todayRiskAssessments.size)
            assertEquals(
                75,
                viewModel.uiState.value.todayRiskAssessments[0]
                    .riskScore,
            )
        }

    @Test
    fun `relapse success rate is 1 when no relapses`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val result =
                HomeData.Success(
                    user = user,
                    psychoeducationMessage = "test",
                    relapseHistory = emptyList(),
                    relapseSuccessRate = 1f,
                    emotionRecords = emptyList(),
                    hasCheckedInToday = false,
                    treatmentProfile = TreatmentProfile.ACT,
                    todayRiskAssessments = emptyList(),
                    alignedDays = 30,
                    topTriggers = emptyList(),
                )
            val viewModel = createViewModel(homeDataResult = result)
            viewModel.refresh()
            advanceUntilIdle()

            assertEquals(1f, viewModel.uiState.value.relapseSuccessRate)
        }

    @Test
    fun `defers onboarding redirect until home becomes visible`() =
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            val viewModel = createViewModel(homeDataResult = HomeData.OnboardingRequired)
            val event = async { viewModel.navigationEvents.first() }
            advanceUntilIdle()

            assertTrue(event.isActive)

            viewModel.onHomeVisible()
            advanceUntilIdle()

            assertEquals(HomeNavigationDestination.Onboarding, event.await())
        }

    private fun createViewModel(homeDataResult: HomeData = defaultSuccessResult()): HomeViewModel {
        runTest {
            whenever(loadHomeDataUseCase.execute()).thenReturn(homeDataResult)
        }
        return HomeViewModel(loadHomeDataUseCase = loadHomeDataUseCase)
    }

    private fun defaultSuccessResult(): HomeData.Success =
        HomeData.Success(
            user = user,
            psychoeducationMessage = "test message",
            relapseHistory = emptyList(),
            relapseSuccessRate = 1f,
            emotionRecords = emptyList(),
            hasCheckedInToday = false,
            treatmentProfile = TreatmentProfile.ACT,
            todayRiskAssessments = emptyList(),
            alignedDays = 30,
            topTriggers = emptyList(),
        )
}
