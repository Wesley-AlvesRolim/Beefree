package com.wesley.beefree.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.BreathingPhaseEnum
import com.wesley.beefree.domain.intervention.ClinicalProfileStrategyFactory
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeMascotSpeechTone
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeStepper
import com.wesley.beefree.ui.screens.helpinterventionscreens.ActDirectionStepContent
import com.wesley.beefree.ui.screens.helpinterventionscreens.ActValuesStepContent
import com.wesley.beefree.ui.screens.helpinterventionscreens.BodyLocationStepContent
import com.wesley.beefree.ui.screens.helpinterventionscreens.CommittedActionStepContent
import com.wesley.beefree.ui.screens.helpinterventionscreens.IntensityStepContent
import com.wesley.beefree.ui.screens.helpinterventionscreens.ReflectionStepContent
import com.wesley.beefree.ui.screens.helpinterventionscreens.TextInputStepContent
import com.wesley.beefree.ui.screens.helpinterventionscreens.TimerStepContent
import com.wesley.beefree.ui.screens.helpinterventionscreens.UrgeSurfingStepContent
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.HelpInterventionViewModel

@Composable
fun HelpInterventionScreen(
    viewModel: HelpInterventionViewModel,
    onDismiss: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        return
    }

    HelpInterventionContent(
        currentStepIndex = state.currentStepIndex,
        allSteps = state.allSteps,
        meditationTextIndex = state.meditationTextIndex,
        breathingPhaseEnum = state.breathingPhaseEnum,
        breathingSecondsLeft = state.breathingSecondsLeft,
        breathingCycleCount = state.breathingCycleCount,
        isComplete = state.isComplete,
        onNext = { answer -> viewModel.onNext(answer) },
        onBack = viewModel::onBack,
        onAdvanceMeditation = viewModel::advanceMeditationStep,
        onDismiss = onDismiss,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HelpInterventionContent(
    currentStepIndex: Int,
    allSteps: List<HelpInterventionStep>,
    meditationTextIndex: Int,
    breathingPhaseEnum: BreathingPhaseEnum,
    breathingSecondsLeft: Int,
    breathingCycleCount: Int,
    isComplete: Boolean,
    onNext: (Any) -> Unit,
    onBack: () -> Unit,
    onAdvanceMeditation: () -> Unit,
    onDismiss: () -> Unit,
) {
    var currentAnswer: Any? by remember { mutableStateOf(null) }

    val currentStep = allSteps.getOrNull(currentStepIndex)
    val isLastStep = currentStepIndex >= allSteps.size - 1

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceContainerLowest),
                title = { BeeHeadlineSmall(stringResource(R.string.help_intervention_title)) },
                navigationIcon = {
                    if (currentStepIndex > 0) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                },
            )
        },
        bottomBar = {
            HelpInterventionBottomBar(
                onNext = { if (currentAnswer != null) onNext(currentAnswer!!) },
                onDismiss = onDismiss,
                canGoNext = currentAnswer != null && !isLastStep,
                isCompleted = isComplete,
            )
        },
    ) { padding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = BeeSpacing.L),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
        ) {
            item {
                BeeStepper(
                    step = currentStepIndex,
                    total = allSteps.size,
                )
            }

            if (currentStep != null) {
                item {
                    StepContent(
                        step = currentStep,
                        meditationTextIndex = meditationTextIndex,
                        breathingPhaseEnum = breathingPhaseEnum,
                        breathingSecondsLeft = breathingSecondsLeft,
                        breathingCycleCount = breathingCycleCount,
                        onAnswerChange = { currentAnswer = it },
                        onAdvanceMeditation = onAdvanceMeditation,
                    )
                }
            }

            item { Spacer(Modifier.height(BeeSpacing.M)) }
        }
    }
}

@Composable
private fun StepContent(
    step: HelpInterventionStep,
    meditationTextIndex: Int,
    breathingPhaseEnum: BreathingPhaseEnum,
    breathingSecondsLeft: Int,
    breathingCycleCount: Int,
    onAnswerChange: (Any) -> Unit,
    onAdvanceMeditation: () -> Unit,
) {
    var actValueSelected by remember { mutableStateOf("") }
    var actValueCustom by remember { mutableStateOf("") }
    var actDirectionSelected by remember { mutableStateOf("") }
    var committedActionSelected by remember { mutableStateOf("") }
    var committedActionCustom by remember { mutableStateOf("") }
    var textInputValue by remember { mutableStateOf("") }
    var timerSeconds by remember { mutableIntStateOf(0) }
    var timerStarted by remember { mutableStateOf(false) }
    var reflectionSelected by remember { mutableStateOf("") }
    var bodyLocationSelected by remember { mutableStateOf(setOf<String>()) }

    when (step) {
        is HelpInterventionStep.IntensityStep ->
            IntensityStepContent(step = step, onAnswerChange = onAnswerChange)

        is HelpInterventionStep.BodyLocationStep -> {
            BodyLocationStepContent(
                step = step,
                selected = bodyLocationSelected,
                onSelectedChange = { bodyLocationSelected = it },
                onAnswerChange = onAnswerChange,
            )
        }

        is HelpInterventionStep.UrgeSurfingStep ->
            UrgeSurfingStepContent(
                step = step,
                meditationTextIndex = meditationTextIndex,
                phase = breathingPhaseEnum,
                secondsLeft = breathingSecondsLeft,
                cycleCount = breathingCycleCount,
                onAdvanceMeditation = onAdvanceMeditation,
                onAnswerChange = onAnswerChange,
            )

        is HelpInterventionStep.PostSurfIntensityStep ->
            IntensityStepContent(step = step, onAnswerChange = onAnswerChange)

        is HelpInterventionStep.ActValuesStep ->
            ActValuesStepContent(
                step = step,
                selectedValue = actValueSelected,
                customValue = actValueCustom,
                onSelectedChange = { actValueSelected = it },
                onCustomValueChange = { actValueCustom = it },
                onAnswerChange = onAnswerChange,
            )

        is HelpInterventionStep.ActDirectionStep ->
            ActDirectionStepContent(
                step = step,
                selectedValue = actDirectionSelected,
                onSelectedChange = { actDirectionSelected = it },
                onAnswerChange = onAnswerChange,
            )

        is HelpInterventionStep.ActCommittedActionStep ->
            CommittedActionStepContent(
                titleKey = step.titleKey,
                suggestions = step.suggestions,
                selectedValue = committedActionSelected,
                customValue = committedActionCustom,
                speechTone = BeeMascotSpeechTone.Tertiary,
                speechKey = "help_intervention.mascot_speech_act_action",
                onSelectedChange = { committedActionSelected = it },
                onCustomValueChange = { committedActionCustom = it },
                onAnswerChange = onAnswerChange,
            )

        is HelpInterventionStep.TccAutomaticThoughtStep ->
            TextInputStepContent(
                titleKey = step.titleKey,
                value = textInputValue,
                onValueChange = { textInputValue = it },
                speechKey = "help_intervention.mascot_speech_tcc_thought",
                speechTone = BeeMascotSpeechTone.Secondary,
                onAnswerChange = onAnswerChange,
            )

        is HelpInterventionStep.TccEvidenceForStep ->
            TextInputStepContent(
                titleKey = step.titleKey,
                value = textInputValue,
                onValueChange = { textInputValue = it },
                speechKey = "help_intervention.mascot_speech_tcc_evidence_for",
                speechTone = BeeMascotSpeechTone.Secondary,
                onAnswerChange = onAnswerChange,
            )

        is HelpInterventionStep.TccEvidenceAgainstStep ->
            TextInputStepContent(
                titleKey = step.titleKey,
                value = textInputValue,
                onValueChange = { textInputValue = it },
                speechKey = "help_intervention.mascot_speech_tcc_evidence_against",
                speechTone = BeeMascotSpeechTone.Secondary,
                onAnswerChange = onAnswerChange,
            )

        is HelpInterventionStep.TccAlternativeThoughtStep ->
            TextInputStepContent(
                titleKey = step.titleKey,
                value = textInputValue,
                onValueChange = { textInputValue = it },
                speechKey =
                    if (step.titleKey == "help_intervention.tcc_restructuring.title") {
                        "help_intervention.mascot_speech_hybrid_restructuring"
                    } else {
                        "help_intervention.mascot_speech_tcc_alternative"
                    },
                speechTone = BeeMascotSpeechTone.Secondary,
                onAnswerChange = onAnswerChange,
            )

        is HelpInterventionStep.TccActionStep ->
            CommittedActionStepContent(
                titleKey = step.titleKey,
                suggestions = step.suggestions,
                selectedValue = committedActionSelected,
                customValue = committedActionCustom,
                speechTone = BeeMascotSpeechTone.Secondary,
                speechKey = "help_intervention.mascot_speech_tcc_action",
                onSelectedChange = { committedActionSelected = it },
                onCustomValueChange = { committedActionCustom = it },
                onAnswerChange = onAnswerChange,
            )

        is HelpInterventionStep.TimerStep ->
            TimerStepContent(
                step = step,
                secondsLeft = timerSeconds,
                timerStarted = timerStarted,
                onTimerTick = { timerSeconds = it },
                onTimerStart = { timerStarted = true },
                onAnswerChange = onAnswerChange,
            )

        is HelpInterventionStep.ReflectionStep ->
            ReflectionStepContent(
                step = step,
                selectedValue = reflectionSelected,
                onSelectedChange = { reflectionSelected = it },
                onAnswerChange = onAnswerChange,
            )
    }
}

@Composable
private fun HelpInterventionBottomBar(
    onNext: () -> Unit,
    onDismiss: () -> Unit,
    canGoNext: Boolean,
    isCompleted: Boolean,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(BeeSpacing.M),
        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        if (!isCompleted) {
            BeeButtonPrimary(
                onClick = onNext,
                enabled = canGoNext,
                modifier = Modifier.weight(1f),
            ) {
                BeeBodyMedium(stringResource(R.string.next))
            }
        } else {
            BeeButtonPrimary(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
            ) {
                BeeBodyMedium(stringResource(R.string.done))
            }
        }
    }
}

@Preview(showBackground = true, name = "Step 0: Start")
@Composable
fun PreviewStep0() = HelpInterventionStepPreview(0)

@Preview(showBackground = true, name = "Step 1")
@Composable
fun PreviewStep1() = HelpInterventionStepPreview(1)

@Preview(showBackground = true, name = "Step 2")
@Composable
fun PreviewStep2() = HelpInterventionStepPreview(2)

@Preview(showBackground = true, name = "Step 3")
@Composable
fun PreviewStep3() = HelpInterventionStepPreview(3)

@Preview(showBackground = true, name = "Step 4")
@Composable
fun PreviewStep4() = HelpInterventionStepPreview(4)

@Preview(showBackground = true, name = "Step 5")
@Composable
fun PreviewStep5() = HelpInterventionStepPreview(5)

@Preview(showBackground = true, name = "Step 6")
@Composable
fun PreviewStep6() = HelpInterventionStepPreview(6)

@Preview(showBackground = true, name = "Step 7")
@Composable
fun PreviewStep7() = HelpInterventionStepPreview(7)

@Preview(showBackground = true, name = "Step 8")
@Composable
fun PreviewStep8() = HelpInterventionStepPreview(8)

@Preview(showBackground = true, name = "Step 9")
@Composable
fun PreviewStep9() = HelpInterventionStepPreview(9)

@Preview(showBackground = true, name = "Step 10")
@Composable
fun PreviewStep10() = HelpInterventionStepPreview(10)

@Preview(showBackground = true, name = "Step 11")
@Composable
fun PreviewStep11() = HelpInterventionStepPreview(11)

@Composable
private fun HelpInterventionStepPreview(index: Int) {
    val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.HYBRID)
    val allSteps = strategy.helpInterventionFlow.allSteps

    BeeFreeTheme {
        HelpInterventionContent(
            currentStepIndex = index,
            allSteps = allSteps,
            meditationTextIndex = 0,
            breathingPhaseEnum = BreathingPhaseEnum.INHALE,
            breathingSecondsLeft = 60,
            breathingCycleCount = 0,
            isComplete = false,
            onNext = {},
            onBack = {},
            onAdvanceMeditation = {},
            onDismiss = {},
        )
    }
}
