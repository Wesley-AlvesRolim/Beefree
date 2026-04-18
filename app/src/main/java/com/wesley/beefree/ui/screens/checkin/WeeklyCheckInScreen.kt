package com.wesley.beefree.ui.screens.checkin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.screens.checkin.components.WeeklyCheckinAcceptanceStep
import com.wesley.beefree.ui.screens.checkin.components.WeeklyCheckinCognitiveDefusionAndSelfAsContextStep
import com.wesley.beefree.ui.screens.checkin.components.WeeklyCheckinCommittedActionStep
import com.wesley.beefree.ui.screens.checkin.components.WeeklyCheckinPresentMomentStep
import com.wesley.beefree.ui.screens.checkin.components.WeeklyCheckinValuesStep
import com.wesley.beefree.ui.screens.checkin.components.WeeklyTopBar
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.CheckInViewModel

private const val TOTAL_STEPS = 5

@Composable
fun WeeklyCheckInScreen(viewModel: CheckInViewModel) {
    val currentStep by viewModel.weeklyStep.collectAsState()
    val coreValues by viewModel.coreValues.collectAsState()
    val valueConnectionLevels by viewModel.valueConnectionLevels.collectAsState()
    val valuesAlignmentText by viewModel.valuesAlignmentText.collectAsState()
    val emotionalSatisfaction by viewModel.emotionalSatisfaction.collectAsState()
    val realConnectionLevel by viewModel.realConnectionLevel.collectAsState()
    val weeklyAnxiety by viewModel.weeklyAnxiety.collectAsState()
    val defusionChoice by viewModel.defusionChoice.collectAsState()
    val defusionObservation by viewModel.defusionObservation.collectAsState()

    BackHandler(enabled = currentStep > 1) { viewModel.previousWeeklyStep() }

    WeeklyCheckInScreen(
        currentStep = currentStep,
        coreValues = coreValues,
        valueConnectionLevels = valueConnectionLevels,
        valuesAlignmentText = valuesAlignmentText,
        emotionalSatisfaction = emotionalSatisfaction,
        realConnectionLevel = realConnectionLevel,
        weeklyAnxiety = weeklyAnxiety,
        defusionChoice = defusionChoice,
        defusionObservation = defusionObservation,
        onBack = { viewModel.previousWeeklyStep() },
        onNext = { viewModel.nextWeeklyStep() },
        onSubmit = { viewModel.submit() },
        onUpdateValueLevel = { name, level -> viewModel.updateValueConnectionLevel(name, level) },
        onUpdateValuesAlignment = { viewModel.updateValuesAlignmentText(it) },
        onUpdateEmotionalSatisfaction = { viewModel.updateEmotionalSatisfaction(it) },
        onUpdateRealConnection = { viewModel.updateRealConnectionLevel(it) },
        onUpdateWeeklyAnxiety = { viewModel.updateWeeklyAnxiety(it) },
        onUpdateDefusionChoice = { viewModel.updateDefusionChoice(it) },
        onUpdateDefusionObservation = { viewModel.updateDefusionObservation(it) },
    )
}

@Composable
private fun WeeklyCheckInScreen(
    currentStep: Int,
    coreValues: List<UserCoreValue>,
    valueConnectionLevels: Map<String, Float>,
    valuesAlignmentText: String,
    emotionalSatisfaction: Float,
    realConnectionLevel: Float,
    weeklyAnxiety: Float,
    defusionChoice: Int?,
    defusionObservation: String,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onSubmit: () -> Unit,
    onUpdateValueLevel: (String, Float) -> Unit,
    onUpdateValuesAlignment: (String) -> Unit,
    onUpdateEmotionalSatisfaction: (Float) -> Unit,
    onUpdateRealConnection: (Float) -> Unit,
    onUpdateWeeklyAnxiety: (Float) -> Unit,
    onUpdateDefusionChoice: (Int?) -> Unit,
    onUpdateDefusionObservation: (String) -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            WeeklyTopBar(currentStep = currentStep, onBack = onBack)

            LinearProgressIndicator(
                progress = { currentStep / TOTAL_STEPS.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceContainerLow,
            )

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = BeeSpacing.L),
            ) {
                Spacer(modifier = Modifier.height(BeeSpacing.L))

                when (currentStep) {
                    1 ->
                        WeeklyCheckinValuesStep(
                            coreValues = coreValues,
                            valueConnectionLevels = valueConnectionLevels,
                            onUpdateLevel = onUpdateValueLevel,
                            onNext = onNext,
                        )
                    2 ->
                        WeeklyCheckinCommittedActionStep(
                            text = valuesAlignmentText,
                            onTextChange = onUpdateValuesAlignment,
                            onNext = onNext,
                        )
                    3 ->
                        WeeklyCheckinPresentMomentStep(
                            emotionalSatisfaction = emotionalSatisfaction,
                            realConnectionLevel = realConnectionLevel,
                            onEmotionalSatisfactionChange = onUpdateEmotionalSatisfaction,
                            onRealConnectionChange = onUpdateRealConnection,
                            onNext = onNext,
                        )
                    4 ->
                        WeeklyCheckinAcceptanceStep(
                            weeklyAnxiety = weeklyAnxiety,
                            onWeeklyAnxietyChange = onUpdateWeeklyAnxiety,
                            onNext = onNext,
                        )
                    5 ->
                        WeeklyCheckinCognitiveDefusionAndSelfAsContextStep(
                            defusionChoice = defusionChoice,
                            defusionObservation = defusionObservation,
                            onChoiceChange = onUpdateDefusionChoice,
                            onObservationChange = onUpdateDefusionObservation,
                            onSubmit = onSubmit,
                        )
                }

                Spacer(modifier = Modifier.height(BeeSpacing.XL))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyCheckInStep1Preview() {
    BeeFreeTheme {
        WeeklyCheckInScreen(
            currentStep = 1,
            coreValues =
                listOf(
                    UserCoreValue(userProfileId = 0, valueName = "Família", createdAt = 0),
                    UserCoreValue(userProfileId = 0, valueName = "Fé", createdAt = 0),
                ),
            valueConnectionLevels = mapOf("Família" to 0.6f, "Fé" to 0.4f),
            valuesAlignmentText = "",
            emotionalSatisfaction = 0.5f,
            realConnectionLevel = 0.5f,
            weeklyAnxiety = 0.5f,
            defusionChoice = null,
            defusionObservation = "",
            onBack = {},
            onNext = {},
            onSubmit = {},
            onUpdateValueLevel = { _, _ -> },
            onUpdateValuesAlignment = {},
            onUpdateEmotionalSatisfaction = {},
            onUpdateRealConnection = {},
            onUpdateWeeklyAnxiety = {},
            onUpdateDefusionChoice = {},
            onUpdateDefusionObservation = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyCheckInStep2Preview() {
    BeeFreeTheme {
        WeeklyCheckInScreen(
            currentStep = 2,
            coreValues = emptyList(),
            valueConnectionLevels = emptyMap(),
            valuesAlignmentText = "",
            emotionalSatisfaction = 0.5f,
            realConnectionLevel = 0.5f,
            weeklyAnxiety = 0.3f,
            defusionChoice = 0,
            defusionObservation = "",
            onBack = {},
            onNext = {},
            onSubmit = {},
            onUpdateValueLevel = { _, _ -> },
            onUpdateValuesAlignment = {},
            onUpdateEmotionalSatisfaction = {},
            onUpdateRealConnection = {},
            onUpdateWeeklyAnxiety = {},
            onUpdateDefusionChoice = {},
            onUpdateDefusionObservation = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyCheckInStep3Preview() {
    BeeFreeTheme {
        WeeklyCheckInScreen(
            currentStep = 3,
            coreValues = emptyList(),
            valueConnectionLevels = emptyMap(),
            valuesAlignmentText = "",
            emotionalSatisfaction = 0.5f,
            realConnectionLevel = 0.5f,
            weeklyAnxiety = 0.3f,
            defusionChoice = 0,
            defusionObservation = "",
            onBack = {},
            onNext = {},
            onSubmit = {},
            onUpdateValueLevel = { _, _ -> },
            onUpdateValuesAlignment = {},
            onUpdateEmotionalSatisfaction = {},
            onUpdateRealConnection = {},
            onUpdateWeeklyAnxiety = {},
            onUpdateDefusionChoice = {},
            onUpdateDefusionObservation = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyCheckInStep4Preview() {
    BeeFreeTheme {
        WeeklyCheckInScreen(
            currentStep = 4,
            coreValues = emptyList(),
            valueConnectionLevels = emptyMap(),
            valuesAlignmentText = "",
            emotionalSatisfaction = 0.5f,
            realConnectionLevel = 0.5f,
            weeklyAnxiety = 0.4f,
            defusionChoice = 0,
            defusionObservation = "",
            onBack = {},
            onNext = {},
            onSubmit = {},
            onUpdateValueLevel = { _, _ -> },
            onUpdateValuesAlignment = {},
            onUpdateEmotionalSatisfaction = {},
            onUpdateRealConnection = {},
            onUpdateWeeklyAnxiety = {},
            onUpdateDefusionChoice = {},
            onUpdateDefusionObservation = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyCheckInStep5Preview() {
    BeeFreeTheme {
        WeeklyCheckInScreen(
            currentStep = 5,
            coreValues = emptyList(),
            valueConnectionLevels = emptyMap(),
            valuesAlignmentText = "",
            emotionalSatisfaction = 0.5f,
            realConnectionLevel = 0.5f,
            weeklyAnxiety = 0.3f,
            defusionChoice = 0,
            defusionObservation = "",
            onBack = {},
            onNext = {},
            onSubmit = {},
            onUpdateValueLevel = { _, _ -> },
            onUpdateValuesAlignment = {},
            onUpdateEmotionalSatisfaction = {},
            onUpdateRealConnection = {},
            onUpdateWeeklyAnxiety = {},
            onUpdateDefusionChoice = {},
            onUpdateDefusionObservation = {},
        )
    }
}
