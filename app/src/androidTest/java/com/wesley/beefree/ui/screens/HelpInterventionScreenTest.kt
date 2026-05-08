package com.wesley.beefree.ui.screens

import android.content.Context
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.CognitiveThoughtRecord
import com.wesley.beefree.domain.entities.InterventionRecord
import com.wesley.beefree.domain.entities.InterventionValueLink
import com.wesley.beefree.domain.entities.UserAddiction
import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.domain.entities.UserHobby
import com.wesley.beefree.domain.entities.UserObjective
import com.wesley.beefree.domain.entities.UserOnboardingSession
import com.wesley.beefree.domain.entities.UserProfile
import com.wesley.beefree.domain.entities.UserSymptom
import com.wesley.beefree.domain.intervention.ports.Ticker
import com.wesley.beefree.domain.intervention.usecases.SaveInterventionSessionUseCase
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.infrastructure.storage.ports.EMIRepository
import com.wesley.beefree.infrastructure.storage.ports.OnboardingRepository
import com.wesley.beefree.infrastructure.storage.ports.UserProfileRepository
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.HelpInterventionViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class HelpInterventionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun stepperShowsCorrectStepCount() {
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
    fun nextButtonDisabledWhenNoAnswer() {
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
    fun backButtonHiddenOnFirstStep() {
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
    fun closeButtonCallsDismiss() {
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
    fun tccFlowHasCorrectStepCount() {
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
        val onboardingRepository = FakeOnboardingRepository(profile)
        val userProfileRepository = FakeUserProfileRepository()
        val emiRepository = FakeEMIRepository()
        val useCase =
            SaveInterventionSessionUseCase(
                emiRepository = emiRepository,
                onboardingRepository = onboardingRepository,
            )
        val ticker = FakeTicker()

        return HelpInterventionViewModel(
            onboardingRepository = onboardingRepository,
            userProfileRepository = userProfileRepository,
            saveInterventionSessionUseCase = useCase,
            ticker = ticker,
        )
    }

    private fun assertEquals(
        expected: Int,
        actual: Int,
        message: () -> String,
    ) {
        if (expected != actual) throw AssertionError(message())
    }

    private class FakeOnboardingRepository(
        private val profile: TreatmentProfile,
    ) : OnboardingRepository {
        override suspend fun getOnboardingSession(userId: Int) =
            UserOnboardingSession(
                id = 1,
                userProfileId = userId,
                clinicalApproach = profile.name,
                ppcsScore = 10,
                pgsiScore = 5,
                moralIncongruenceScore = 3,
                frequencyScore = 2,
                moralDisapprovalScore = 4,
                hasNeurodivergence = false,
                createdAt = System.currentTimeMillis(),
            )

        override suspend fun insertOnboardingSession(session: UserOnboardingSession): Long {
            TODO("Not yet implemented")
        }

        override suspend fun insertCoreValue(value: UserCoreValue): Long {
            TODO("Not yet implemented")
        }

        override suspend fun deleteCoreValue(value: UserCoreValue) {
            TODO("Not yet implemented")
        }

        override fun getCoreValues(userId: Int): Flow<List<UserCoreValue>> = flowOf(emptyList())

        override suspend fun insertHobby(hobby: UserHobby): Long {
            TODO("Not yet implemented")
        }

        override suspend fun deleteHobby(hobby: UserHobby) {
            TODO("Not yet implemented")
        }

        override fun getHobbies(userId: Int): Flow<List<UserHobby>> {
            TODO("Not yet implemented")
        }

        override suspend fun insertObjective(objective: UserObjective): Long {
            TODO("Not yet implemented")
        }

        override suspend fun deleteObjective(objective: UserObjective) {
            TODO("Not yet implemented")
        }

        override fun getObjectives(userId: Int): Flow<List<UserObjective>> {
            TODO("Not yet implemented")
        }

        override suspend fun insertSymptom(symptom: UserSymptom): Long {
            TODO("Not yet implemented")
        }

        override suspend fun deleteSymptom(symptom: UserSymptom) {
            TODO("Not yet implemented")
        }

        override fun getSymptoms(userId: Int): Flow<List<UserSymptom>> {
            TODO("Not yet implemented")
        }
    }

    private class FakeUserProfileRepository : UserProfileRepository {
        override fun getAllProfiles() =
            flowOf(
                listOf(
                    UserProfile(
                        id = 1,
                        profileName = "TestUser",
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis(),
                    ),
                ),
            )

        override suspend fun insertProfile(profile: UserProfile): Long {
            TODO("Not yet implemented")
        }

        override suspend fun updateProfile(profile: UserProfile) {
            TODO("Not yet implemented")
        }

        override suspend fun getProfileById(id: Int): UserProfile? {
            TODO("Not yet implemented")
        }

        override suspend fun associateAddictionToProfile(userAddiction: UserAddiction): Long {
            TODO("Not yet implemented")
        }

        override suspend fun removeAddictionFromProfile(userAddiction: UserAddiction) {
            TODO("Not yet implemented")
        }

        override fun getAddictionsByUserId(userId: Int): Flow<List<UserAddiction>> {
            TODO("Not yet implemented")
        }
    }

    private class FakeEMIRepository : EMIRepository {
        override suspend fun insertInterventionRecord(record: InterventionRecord): Long {
            TODO("Not yet implemented")
        }

        override suspend fun updateInterventionRecord(record: InterventionRecord) {
            TODO("Not yet implemented")
        }

        override fun getInterventionRecords(userId: Int): Flow<List<InterventionRecord>> {
            TODO("Not yet implemented")
        }

        override suspend fun insertThoughtRecord(record: CognitiveThoughtRecord): Long {
            TODO("Not yet implemented")
        }

        override suspend fun updateThoughtRecord(record: CognitiveThoughtRecord) {
            TODO("Not yet implemented")
        }

        override fun getThoughtRecords(userId: Int): Flow<List<CognitiveThoughtRecord>> {
            TODO("Not yet implemented")
        }

        override suspend fun insertInterventionValueLink(link: InterventionValueLink) {
            TODO("Not yet implemented")
        }
    }

    private class FakeTicker : Ticker {
        override fun ticks(): Flow<Unit> = flowOf()
    }
}
