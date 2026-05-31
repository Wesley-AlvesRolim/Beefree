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
import com.wesley.beefree.domain.emotion.usecases.SaveEmotionRecordUseCase
import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.entities.UserAddiction
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import com.wesley.beefree.domain.repository.ports.RiskWeightsRepository
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
import com.wesley.beefree.domain.risk.RiskWeights
import com.wesley.beefree.domain.risk.usecases.CalculateAndSaveRiskAssessmentUseCase
import com.wesley.beefree.infrastructure.logging.Logger
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.EmotionalRecordStep
import com.wesley.beefree.ui.viewmodel.EmotionalRecordViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class EmotionalRecordScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    @Test
    fun introStepShowsTitleAndStartButton() {
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
    fun clickingStartAdvancesToCaptureStep() {
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
    fun captureBackButtonReturnsToIntro() {
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
    fun saveButtonIsDisabledWhileSaving() {
        val viewModel = buildViewModel(metricsRepository = BlockingMetricsRepository())
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
    fun successfulSaveAdvancesToDoneStep() {
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
    fun failedSaveShowsErrorDialog() {
        val viewModel =
            buildViewModel(
                metricsRepository = FailingMetricsRepository("save failed"),
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
    fun dismissingErrorDialogClearsErrorState() {
        val viewModel =
            buildViewModel(
                metricsRepository = FailingMetricsRepository("save failed"),
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
    fun sliderChangeReflectsInCaptureValueLabel() {
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

    private fun buildViewModel(
        metricsRepository: MetricsRepository = RecordingMetricsRepository(),
        userProfileRepository: UserProfileRepository = FakeUserProfileRepository(),
    ): EmotionalRecordViewModel =
        EmotionalRecordViewModel(
            userProfileRepository = userProfileRepository,
            saveEmotionRecordUseCase = SaveEmotionRecordUseCase(metricsRepository),
            calculateAndSaveRiskAssessmentUseCase =
                CalculateAndSaveRiskAssessmentUseCase(
                    metricsRepository = metricsRepository,
                    riskWeightsRepository = FakeRiskWeightsRepository(),
                ),
            logger = NoOpLogger,
        )

    private object NoOpLogger : Logger {
        override fun d(
            tag: String,
            message: String,
        ) = Unit

        override fun info(
            tag: String,
            message: String,
        ) = Unit
    }

    private class FakeUserProfileRepository : UserProfileRepository {
        override fun getAllProfiles(): Flow<List<UserProfile>> =
            flowOf(
                listOf(
                    UserProfile(
                        id = 1,
                        profileName = "Test",
                        createdAt = 0L,
                        updatedAt = 0L,
                    ),
                ),
            )

        override suspend fun insertProfile(profile: UserProfile): Long = TODO("Not used")

        override suspend fun updateProfile(profile: UserProfile) = TODO("Not used")

        override suspend fun getProfileById(id: Int): UserProfile? = TODO("Not used")

        override suspend fun associateAddictionToProfile(userAddiction: UserAddiction): Long = TODO("Not used")

        override suspend fun removeAddictionFromProfile(userAddiction: UserAddiction) = TODO("Not used")

        override fun getAddictionsByUserId(userId: Int): Flow<List<UserAddiction>> = flowOf(emptyList())
    }

    private open class RecordingMetricsRepository : MetricsRepository {
        val inserted = mutableListOf<EmotionRecord>()

        override suspend fun insertEmotionRecord(record: EmotionRecord): Long {
            inserted.add(record)
            return inserted.size.toLong()
        }

        override fun getEmotionRecords(userId: Int): Flow<List<EmotionRecord>> = flowOf(emptyList())

        override fun getEmotionRecordsByType(
            userId: Int,
            feelingType: FeelingType,
        ): Flow<List<EmotionRecord>> = flowOf(emptyList())

        override suspend fun getLatestEmotionRecord(userId: Int): EmotionRecord? = null

        override suspend fun insertRiskFeatureSnapshot(snapshot: RiskFeatureSnapshot): Long = 0L

        override fun getRiskFeatureSnapshots(userId: Int): Flow<List<RiskFeatureSnapshot>> = flowOf(emptyList())

        override suspend fun getLatestRiskFeatureSnapshot(userId: Int): RiskFeatureSnapshot? = null

        override suspend fun insertRiskAssessment(assessment: RiskAssessment): Long = 0L

        override suspend fun deleteAllRiskAssessmentsForUser(userId: Int) {
        }

        override fun getRiskAssessments(userId: Int): Flow<List<RiskAssessment>> = flowOf(emptyList())
    }

    private class FailingMetricsRepository(
        private val errorMessage: String,
    ) : RecordingMetricsRepository() {
        override suspend fun insertEmotionRecord(record: EmotionRecord): Long = throw IllegalStateException(errorMessage)
    }

    private class BlockingMetricsRepository : RecordingMetricsRepository() {
        override suspend fun insertEmotionRecord(record: EmotionRecord): Long {
            delay(2_000L)
            return super.insertEmotionRecord(record)
        }
    }

    private class FakeRiskWeightsRepository : RiskWeightsRepository {
        override fun getWeights(userId: Int): RiskWeights = RiskWeights()

        override fun saveWeights(
            userId: Int,
            weights: RiskWeights,
        ) = Unit
    }
}
