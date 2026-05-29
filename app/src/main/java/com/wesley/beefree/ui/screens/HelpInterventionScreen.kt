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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.BreathingPhaseEnum
import com.wesley.beefree.domain.entities.CoreValueType
import com.wesley.beefree.domain.entities.UserCoreValue
import com.wesley.beefree.domain.intervention.ClinicalProfileStrategyFactory
import com.wesley.beefree.domain.intervention.HelpInterventionStep
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
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
import com.wesley.beefree.ui.viewmodel.AnswerKey
import com.wesley.beefree.ui.viewmodel.HelpInterventionViewModel
import com.wesley.beefree.ui.viewmodel.stepAnswerKey
import kotlin.Int

private object HelpInterventionSpeechKeys {
    const val ACT_ACTION = "help_intervention.mascot_speech_act_action"
    const val TCC_THOUGHT = "help_intervention.mascot_speech_tcc_thought"
    const val TCC_EVIDENCE_FOR = "help_intervention.mascot_speech_tcc_evidence_for"
    const val TCC_EVIDENCE_AGAINST = "help_intervention.mascot_speech_tcc_evidence_against"
    const val TCC_ALTERNATIVE = "help_intervention.mascot_speech_tcc_alternative"
    const val HYBRID_RESTRUCTURING = "help_intervention.mascot_speech_hybrid_restructuring"
    const val TCC_ACTION = "help_intervention.mascot_speech_tcc_action"
}

private object HelpInterventionTextInputHints {
    const val TCC_THOUGHT = "help_intervention.tcc_auto_thought.hint"
    const val TCC_EVIDENCE_FOR = "help_intervention.tcc_evidence_for.hint"
    const val TCC_EVIDENCE_AGAINST = "help_intervention.tcc_evidence_against.hint"
    const val TCC_ALTERNATIVE = "help_intervention.tcc_alternative_thought.hint"
    const val HYBRID_RESTRUCTURING = "help_intervention.tcc_restructuring.hint"
}

private object HelpInterventionPreviewKeys {
    const val DRINK_WATER = "help_intervention.act_actions.drink_water"
}

@Composable
fun HelpInterventionScreen(
    viewModel: HelpInterventionViewModel,
    onDismiss: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isComplete) {
        if (state.isComplete) onDismiss()
    }

    if (state.isLoading) {
        return
    }

    val priorityKeys =
        listOf(
            AnswerKey.ACT_ACTION_CUSTOM,
            AnswerKey.ACT_ACTION,
            AnswerKey.TCC_ACTION_CUSTOM,
            AnswerKey.TCC_ACTION,
        )

    val lastCommittedAction =
        priorityKeys
            .mapNotNull { state.answers[it.value] as? String }
            .firstOrNull { it.isNotEmpty() }

    val currentStep = state.allSteps.getOrNull(state.currentStepIndex)
    val stepId = currentStep?.let { stepAnswerKey(it) } ?: ""
    val currentAnswer = viewModel.getAnswer(stepId)
    val isTheLastScreen = state.currentStepIndex == state.allSteps.lastIndex

    val canContinue =
        when (currentStep) {
            is HelpInterventionStep.UrgeSurfingStep -> state.breathingCycleCount >= currentStep.minimumCycles
            else -> currentAnswer != null
        }

    HelpInterventionContent(
        currentStepIndex = state.currentStepIndex,
        allSteps = state.allSteps,
        meditationTextIndex = state.meditationTextIndex,
        breathingPhaseEnum = state.breathingPhaseEnum,
        breathingSecondsLeft = state.breathingSecondsLeft,
        breathingCycleCount = state.breathingCycleCount,
        isTheLastScreen = isTheLastScreen,
        isSaving = state.isSaving,
        userCoreValues = state.userCoreValues,
        lastCommittedAction = lastCommittedAction,
        currentAnswer = currentAnswer,
        canContinue = canContinue,
        answers = state.answers,
        onNext = { answer -> viewModel.onNext(answer) },
        onBack = viewModel::onBack,
        onAnswerChange = viewModel::updateAnswer,
        onAdvanceMeditation = viewModel::advanceMeditationStep,
        onStartBreathing = viewModel::startBreathingTimer,
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
    isTheLastScreen: Boolean,
    isSaving: Boolean,
    userCoreValues: List<UserCoreValue>,
    lastCommittedAction: String?,
    currentAnswer: Any?,
    canContinue: Boolean,
    answers: Map<String, Any>,
    onNext: (Any) -> Unit,
    onBack: () -> Unit,
    onAnswerChange: (String, Any) -> Unit,
    onAdvanceMeditation: () -> Unit,
    onStartBreathing: () -> Unit,
    onDismiss: () -> Unit,
) {
    val currentStep = allSteps.getOrNull(currentStepIndex)

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
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.emi_close_description))
                    }
                },
            )
        },
        bottomBar = {
            HelpInterventionBottomBar(
                onNext = { if (canContinue) onNext(currentAnswer ?: true) },
                onDismiss = onDismiss,
                canGoNext = canContinue,
                isTheLastScreen = isTheLastScreen,
                isSaving = isSaving,
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
                    step = currentStepIndex + 1,
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
                        userCoreValues = userCoreValues,
                        lastCommittedAction = lastCommittedAction,
                        answers = answers,
                        onAnswerChange = onAnswerChange,
                        onAdvanceMeditation = onAdvanceMeditation,
                        onStartBreathing = onStartBreathing,
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
    userCoreValues: List<UserCoreValue>,
    lastCommittedAction: String?,
    answers: Map<String, Any>,
    onAnswerChange: (String, Any) -> Unit,
    onAdvanceMeditation: () -> Unit,
    onStartBreathing: () -> Unit,
) {
    val getIntAnswer = { key: String -> answers[key] as? Int ?: 0 }
    val getStringAnswer = { key: String -> answers[key] as? String ?: "" }
    val getSetAnswer = { key: String -> answers[key] as? Set<String> ?: emptySet() }

    when (step) {
        is HelpInterventionStep.IntensityStep ->
            IntensityStepContent(
                step = step,
                value = getIntAnswer(AnswerKey.INITIAL_INTENSITY.value),
                onValueChange = { onAnswerChange(AnswerKey.INITIAL_INTENSITY.value, it) },
                onAnswerChange = { },
            )

        is HelpInterventionStep.BodyLocationStep -> {
            BodyLocationStepContent(
                step = step,
                selected = getSetAnswer(AnswerKey.BODY_LOCATIONS.value),
                onSelectedChange = { onAnswerChange(AnswerKey.BODY_LOCATIONS.value, it) },
                onAnswerChange = { },
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
                onStartBreathing = onStartBreathing,
                onAnswerChange = { },
            )

        is HelpInterventionStep.PostSurfIntensityStep ->
            IntensityStepContent(
                step = step,
                value = getIntAnswer(AnswerKey.POST_SURF_INTENSITY.value),
                onValueChange = { onAnswerChange(AnswerKey.POST_SURF_INTENSITY.value, it) },
                onAnswerChange = { },
            )

        is HelpInterventionStep.ActValuesStep -> {
            val userValueNames = userCoreValues.map { it.value.name }.toSet()
            ActValuesStepContent(
                step = step,
                selectedValue = getStringAnswer(AnswerKey.ACT_VALUE.value),
                customValue = getStringAnswer(AnswerKey.ACT_VALUE_CUSTOM.value),
                onSelectedChange = { onAnswerChange(AnswerKey.ACT_VALUE.value, it) },
                onCustomValueChange = { onAnswerChange(AnswerKey.ACT_VALUE_CUSTOM.value, it) },
                onAnswerChange = { },
                userCoreValueNames = userValueNames,
            )
        }

        is HelpInterventionStep.ActDirectionStep ->
            ActDirectionStepContent(
                step = step,
                selectedValue = getStringAnswer(AnswerKey.ACT_DIRECTION.value),
                onSelectedChange = { onAnswerChange(AnswerKey.ACT_DIRECTION.value, it) },
                onAnswerChange = { },
            )

        is HelpInterventionStep.ActCommittedActionStep ->
            CommittedActionStepContent(
                titleKey = step.titleKey,
                suggestions = step.suggestions,
                selectedValue = getStringAnswer(AnswerKey.ACT_ACTION.value),
                customValue = getStringAnswer(AnswerKey.ACT_ACTION_CUSTOM.value),
                speechTone = BeeMascotSpeechTone.Tertiary,
                speechKey = HelpInterventionSpeechKeys.ACT_ACTION,
                onSelectedChange = { onAnswerChange(AnswerKey.ACT_ACTION.value, it) },
                onCustomValueChange = { onAnswerChange(AnswerKey.ACT_ACTION_CUSTOM.value, it) },
                onAnswerChange = { },
            )

        is HelpInterventionStep.TccAutomaticThoughtStep ->
            TextInputStepContent(
                titleKey = step.titleKey,
                value = getStringAnswer(AnswerKey.TCC_AUTO_THOUGHT.value),
                onValueChange = { onAnswerChange(AnswerKey.TCC_AUTO_THOUGHT.value, it) },
                placeholderKey = HelpInterventionTextInputHints.TCC_THOUGHT,
                speechKey = HelpInterventionSpeechKeys.TCC_THOUGHT,
                speechTone = BeeMascotSpeechTone.Secondary,
                onAnswerChange = { },
            )

        is HelpInterventionStep.TccEvidenceForStep ->
            TextInputStepContent(
                titleKey = step.titleKey,
                value = getStringAnswer(AnswerKey.TCC_EVIDENCE_FOR.value),
                onValueChange = { onAnswerChange(AnswerKey.TCC_EVIDENCE_FOR.value, it) },
                placeholderKey = HelpInterventionTextInputHints.TCC_EVIDENCE_FOR,
                speechKey = HelpInterventionSpeechKeys.TCC_EVIDENCE_FOR,
                speechTone = BeeMascotSpeechTone.Secondary,
                onAnswerChange = { },
            )

        is HelpInterventionStep.TccEvidenceAgainstStep ->
            TextInputStepContent(
                titleKey = step.titleKey,
                value = getStringAnswer(AnswerKey.TCC_EVIDENCE_AGAINST.value),
                onValueChange = { onAnswerChange(AnswerKey.TCC_EVIDENCE_AGAINST.value, it) },
                placeholderKey = HelpInterventionTextInputHints.TCC_EVIDENCE_AGAINST,
                speechKey = HelpInterventionSpeechKeys.TCC_EVIDENCE_AGAINST,
                speechTone = BeeMascotSpeechTone.Secondary,
                onAnswerChange = { },
            )

        is HelpInterventionStep.TccAlternativeThoughtStep ->
            TextInputStepContent(
                titleKey = step.titleKey,
                value = getStringAnswer(AnswerKey.TCC_ALTERNATIVE_THOUGHT.value),
                onValueChange = { onAnswerChange(AnswerKey.TCC_ALTERNATIVE_THOUGHT.value, it) },
                placeholderKey =
                    if (step.titleKey == "help_intervention.tcc_restructuring.title") {
                        HelpInterventionTextInputHints.HYBRID_RESTRUCTURING
                    } else {
                        HelpInterventionTextInputHints.TCC_ALTERNATIVE
                    },
                speechKey =
                    if (step.titleKey == "help_intervention.tcc_restructuring.title") {
                        HelpInterventionSpeechKeys.HYBRID_RESTRUCTURING
                    } else {
                        HelpInterventionSpeechKeys.TCC_ALTERNATIVE
                    },
                speechTone = BeeMascotSpeechTone.Secondary,
                onAnswerChange = { },
            )

        is HelpInterventionStep.TccActionStep ->
            CommittedActionStepContent(
                titleKey = step.titleKey,
                suggestions = step.suggestions,
                selectedValue = getStringAnswer(AnswerKey.TCC_ACTION.value),
                customValue = getStringAnswer(AnswerKey.TCC_ACTION_CUSTOM.value),
                speechTone = BeeMascotSpeechTone.Secondary,
                speechKey = HelpInterventionSpeechKeys.TCC_ACTION,
                onSelectedChange = { onAnswerChange(AnswerKey.TCC_ACTION.value, it) },
                onCustomValueChange = { onAnswerChange(AnswerKey.TCC_ACTION_CUSTOM.value, it) },
                onAnswerChange = { },
            )

        is HelpInterventionStep.TimerStep ->
            TimerStepContent(
                step = step,
                selectedAction = lastCommittedAction,
                secondsLeft = getIntAnswer(AnswerKey.TIMER_SECONDS.value),
                timerStarted = answers.containsKey(AnswerKey.TIMER_STARTED.value),
                timerCompleted = answers[AnswerKey.TIMER.value] == true,
                onTimerTick = { onAnswerChange(AnswerKey.TIMER_SECONDS.value, it) },
                onTimerStart = { onAnswerChange(AnswerKey.TIMER_STARTED.value, true) },
                onAnswerChange = { onAnswerChange(AnswerKey.TIMER.value, it) },
            )

        is HelpInterventionStep.ReflectionStep -> {
            ReflectionStepContent(
                step = step,
                selectedValue = getStringAnswer(AnswerKey.REFLECTION.value),
                onSelectedChange = { onAnswerChange(AnswerKey.REFLECTION.value, it) },
                initialIntensity = getIntAnswer(AnswerKey.INITIAL_INTENSITY.value),
                postSurfIntensity = getIntAnswer(AnswerKey.POST_SURF_INTENSITY.value),
                timerSeconds = getIntAnswer(AnswerKey.TIMER_SECONDS.value),
                committedAction = lastCommittedAction,
            )
        }
    }
}

@Composable
private fun HelpInterventionBottomBar(
    onNext: () -> Unit,
    onDismiss: () -> Unit,
    canGoNext: Boolean,
    isTheLastScreen: Boolean,
    isSaving: Boolean,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(BeeSpacing.M),
        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        if (!isTheLastScreen) {
            BeeButtonPrimary(
                onClick = onNext,
                enabled = canGoNext,
                modifier = Modifier.weight(1f),
            ) {
                BeeLabelMedium(
                    text = stringResource(R.string.next),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        } else {
            BeeButtonPrimary(
                onClick = onNext,
                enabled = !isSaving,
                modifier = Modifier.weight(1f),
            ) {
                BeeLabelMedium(
                    text = stringResource(R.string.done),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
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

@Composable
private fun HelpInterventionStepPreview(index: Int) {
    val strategy = ClinicalProfileStrategyFactory.from(TreatmentProfile.ACT)
    val allSteps = strategy.helpInterventionFlow.allSteps

    BeeFreeTheme {
        HelpInterventionContent(
            currentStepIndex = index,
            allSteps = allSteps,
            meditationTextIndex = 0,
            breathingPhaseEnum = BreathingPhaseEnum.INHALE,
            breathingSecondsLeft = 60,
            breathingCycleCount = 0,
            isTheLastScreen = index == allSteps.lastIndex,
            isSaving = false,
            userCoreValues =
                listOf(
                    UserCoreValue(
                        userProfileId = 1,
                        value = CoreValueType.FAITH,
                        createdAt = System.currentTimeMillis(),
                    ),
                ),
            lastCommittedAction = HelpInterventionPreviewKeys.DRINK_WATER,
            currentAnswer = null,
            canContinue = true,
            answers = emptyMap(),
            onNext = {},
            onBack = {},
            onAnswerChange = { _, _ -> },
            onAdvanceMeditation = {},
            onStartBreathing = {},
            onDismiss = {},
        )
    }
}
