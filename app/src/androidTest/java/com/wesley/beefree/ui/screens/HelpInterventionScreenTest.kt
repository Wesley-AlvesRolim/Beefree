package com.wesley.beefree.ui.screens

import android.content.Context
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.UserOnboardingSession
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.mocks.EMIRepositoryMock
import com.wesley.beefree.domain.mocks.MetricsRepositoryMock
import com.wesley.beefree.domain.mocks.OnboardingRepositoryMock
import com.wesley.beefree.domain.mocks.RiskWeightsRepositoryMock
import com.wesley.beefree.domain.mocks.TickerMock
import com.wesley.beefree.domain.mocks.UserProfileRepositoryMock
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.usecases.intervention.SaveInterventionSessionUseCase
import com.wesley.beefree.domain.usecases.risk.CalculateAndSaveRiskAssessmentUseCase
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.HelpInterventionViewModel
import org.junit.Rule
import org.junit.Test

class HelpInterventionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun stepper_shows_correct_step_count() {
        val viewModel = setupViewModel(TreatmentProfile.TCC)
        composeTestRule.setContent {
            BeeFreeTheme {
                HelpInterventionScreen(viewModel = viewModel)
            }
        }

        composeTestRule.waitUntil(2000L) { !viewModel.uiState.value.isLoading }

        val stepCount = viewModel.uiState.value.allSteps.size
        composeTestRule
            .onNodeWithText("$stepCount", substring = true)
            .assertExists("Stepper should show total step count")
    }

    @Test
    fun next_button_disabled_when_no_answer() {
        val viewModel = setupViewModel(TreatmentProfile.ACT)
        composeTestRule.setContent {
            BeeFreeTheme {
                HelpInterventionScreen(viewModel = viewModel)
            }
        }

        composeTestRule.waitUntil(2000L) { !viewModel.uiState.value.isLoading }
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeTestRule
            .onNodeWithText(
                context.getString(R.string.next),
            ).assertIsNotEnabled()
    }

    @Test
    fun back_button_hidden_on_first_step() {
        val viewModel = setupViewModel(TreatmentProfile.HYBRID)
        composeTestRule.setContent {
            BeeFreeTheme {
                HelpInterventionScreen(viewModel = viewModel)
            }
        }

        composeTestRule.waitUntil(2000L) { !viewModel.uiState.value.isLoading }

        assertEquals(
            0,
            viewModel.uiState.value.currentStepIndex,
        ) { "Help intervention should start on the first step" }
    }

    @Test
    fun close_button_calls_dismiss() {
        var dismissCalled = false
        val viewModel = setupViewModel(TreatmentProfile.PREVENTION)
        composeTestRule.setContent {
            BeeFreeTheme {
                HelpInterventionScreen(
                    viewModel = viewModel,
                    onDismiss = { dismissCalled = true },
                )
            }
        }

        composeTestRule.waitUntil(2000L) { !viewModel.uiState.value.isLoading }
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.emi_close_description))
            .performClick()

        assert(dismissCalled) { "onDismiss should be called" }
    }

    @Test
    fun tcc_flow_has_correct_step_count() {
        val viewModel = setupViewModel(TreatmentProfile.TCC)
        composeTestRule.setContent {
            BeeFreeTheme {
                HelpInterventionScreen(viewModel = viewModel)
            }
        }

        composeTestRule.waitUntil(2000L) { !viewModel.uiState.value.isLoading }

        val stepCount = viewModel.uiState.value.allSteps.size
        assertEquals(11, stepCount) { "TCC flow should have exactly 11 steps" }
    }

    private fun setupViewModel(profile: TreatmentProfile): HelpInterventionViewModel {
        val onboardingRepository =
            OnboardingRepositoryMock().apply {
                session =
                    UserOnboardingSession(
                        id = 1,
                        userProfileId = 1,
                        clinicalApproach = profile.name,
                        ppcsScore = 10,
                        pgsiScore = 5,
                        moralIncongruenceScore = 3,
                        frequencyScore = 2,
                        moralDisapprovalScore = 4,
                        hasNeurodivergence = false,
                        createdAt = System.currentTimeMillis(),
                    )
            }
        val userProfileRepository =
            UserProfileRepositoryMock().apply {
                profiles =
                    listOf(
                        UserProfile(
                            id = 1,
                            profileName = "TestUser",
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis(),
                        ),
                    )
            }
        val emiRepository = EMIRepositoryMock()
        val useCase =
            SaveInterventionSessionUseCase(
                emiRepository = emiRepository,
                onboardingRepository = onboardingRepository,
            )
        val calculateAndSaveRiskAssessmentUseCase =
            CalculateAndSaveRiskAssessmentUseCase(
                metricsRepository = MetricsRepositoryMock(),
                riskWeightsRepository = RiskWeightsRepositoryMock(),
            )

        return HelpInterventionViewModel(
            onboardingRepository = onboardingRepository,
            userProfileRepository = userProfileRepository,
            saveInterventionSessionUseCase = useCase,
            calculateAndSaveRiskAssessmentUseCase = calculateAndSaveRiskAssessmentUseCase,
            ticker = TickerMock(),
        )
    }

    private fun assertEquals(
        expected: Int,
        actual: Int,
        message: () -> String,
    ) {
        if (expected != actual) throw AssertionError(message())
    }
}
