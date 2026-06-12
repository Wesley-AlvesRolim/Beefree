package com.wesley.beefree.ui.screens.emotionalrecord

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.mocks.MetricsRepositoryMock
import com.wesley.beefree.domain.mocks.RiskFeatureSnapshotRepositoryMock
import com.wesley.beefree.domain.mocks.RiskWeightsRepositoryMock
import com.wesley.beefree.domain.mocks.UserProfileRepositoryMock
import com.wesley.beefree.domain.usecases.emotion.SaveEmotionRecordUseCase
import com.wesley.beefree.domain.usecases.risk.CalculateAndSaveRiskAssessmentUseCase
import com.wesley.beefree.domain.usecases.risk.SaveRiskFeatureSnapshotUseCase
import com.wesley.beefree.infrastructure.logging.TestLogger
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.EmotionalRecordStep
import com.wesley.beefree.ui.viewmodel.EmotionalRecordViewModel
import kotlinx.coroutines.Dispatchers
import org.junit.Rule
import org.junit.Test

class EmotionalRecordScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    @Test
    fun intro_step_shows_title_and_start_button() {
        val viewModel = buildViewModel()
        composeTestRule.setContent {
            BeeFreeTheme { EmotionalRecordScreen(viewModel = viewModel) }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.emotional_record_intro_title))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.btn_start))
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun clicking_start_advances_to_capture_step() {
        val viewModel = buildViewModel()
        composeTestRule.setContent {
            BeeFreeTheme { EmotionalRecordScreen(viewModel = viewModel) }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.btn_start))
            .performClick()

        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText(context.getString(R.string.emotional_record_save_button))
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun capture_back_button_returns_to_intro() {
        val viewModel = buildViewModel()
        viewModel.onNext()
        composeTestRule.setContent {
            BeeFreeTheme { EmotionalRecordScreen(viewModel = viewModel) }
        }

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.onboarding_btn_back))
            .performClick()

        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText(context.getString(R.string.btn_start))
            .assertIsDisplayed()
    }

    @Test
    fun save_button_is_disabled_while_saving() {
        val viewModel =
            buildViewModel(
                metricsRepository = MetricsRepositoryMock().apply { delayOnInsertEmotionRecord = 2_000L },
            )
        viewModel.onNext()
        composeTestRule.setContent {
            BeeFreeTheme { EmotionalRecordScreen(viewModel = viewModel) }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.emotional_record_save_button))
            .performClick()

        composeTestRule.waitUntil(2000L) { viewModel.uiState.value.isSaving }
        composeTestRule
            .onNodeWithText(context.getString(R.string.emotional_record_save_button))
            .assertIsNotEnabled()
    }

    @Test
    fun successful_save_advances_to_done_step() {
        val viewModel = buildViewModel()
        viewModel.onNext()
        composeTestRule.setContent {
            BeeFreeTheme { EmotionalRecordScreen(viewModel = viewModel) }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.emotional_record_save_button))
            .performClick()

        composeTestRule.waitUntil(2000L) {
            viewModel.uiState.value.step == EmotionalRecordStep.DONE
        }
        composeTestRule
            .onNodeWithText(context.getString(R.string.emotional_record_done_title))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.emotional_record_done_button))
            .assertIsDisplayed()
    }

    @Test
    fun failed_save_shows_error_dialog() {
        val viewModel =
            buildViewModel(
                metricsRepository =
                    MetricsRepositoryMock().apply {
                        throwOnInsertEmotionRecord = IllegalStateException("save failed")
                    },
            )
        viewModel.onNext()
        composeTestRule.setContent {
            BeeFreeTheme { EmotionalRecordScreen(viewModel = viewModel) }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.emotional_record_save_button))
            .performClick()

        composeTestRule.waitUntil(2000L) { viewModel.uiState.value.error != null }
        composeTestRule
            .onNodeWithText(context.getString(R.string.error_title))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("save failed")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.btn_ok))
            .assertIsDisplayed()
    }

    @Test
    fun dismissing_error_dialog_clears_error_state() {
        val viewModel =
            buildViewModel(
                metricsRepository =
                    MetricsRepositoryMock().apply {
                        throwOnInsertEmotionRecord = IllegalStateException("save failed")
                    },
            )
        viewModel.onNext()
        composeTestRule.setContent {
            BeeFreeTheme { EmotionalRecordScreen(viewModel = viewModel) }
        }

        composeTestRule
            .onNodeWithText(context.getString(R.string.emotional_record_save_button))
            .performClick()
        composeTestRule.waitUntil(2000L) { viewModel.uiState.value.error != null }

        composeTestRule
            .onNodeWithText(context.getString(R.string.btn_ok))
            .performClick()

        composeTestRule.waitUntil(2000L) { viewModel.uiState.value.error == null }
    }

    @Test
    fun slider_change_reflects_in_capture_value_label() {
        val viewModel = buildViewModel()
        viewModel.onNext()
        viewModel.onSliderChange(FeelingType.SLEEP, 8f)
        composeTestRule.setContent {
            BeeFreeTheme { EmotionalRecordScreen(viewModel = viewModel) }
        }

        composeTestRule
            .onNodeWithText("8/10")
            .assertIsDisplayed()
    }

    private fun buildViewModel(metricsRepository: MetricsRepositoryMock = MetricsRepositoryMock()): EmotionalRecordViewModel =
        EmotionalRecordViewModel(
            userProfileRepository =
                UserProfileRepositoryMock().apply {
                    profiles = listOf(UserProfile(id = 1, profileName = "Test", createdAt = 0L, updatedAt = 0L))
                },
            saveEmotionRecordUseCase = SaveEmotionRecordUseCase(metricsRepository),
            saveRiskFeatureSnapshotUseCase = SaveRiskFeatureSnapshotUseCase(RiskFeatureSnapshotRepositoryMock()),
            calculateAndSaveRiskAssessmentUseCase =
                CalculateAndSaveRiskAssessmentUseCase(
                    metricsRepository = metricsRepository,
                    riskWeightsRepository = RiskWeightsRepositoryMock(),
                ),
            logger = TestLogger,
            ioDispatcher = Dispatchers.Unconfined,
        )
}
