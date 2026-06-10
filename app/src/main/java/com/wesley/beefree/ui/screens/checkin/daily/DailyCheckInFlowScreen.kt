package com.wesley.beefree.ui.screens.checkin.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.R
import com.wesley.beefree.domain.checkin.ActivityType
import com.wesley.beefree.domain.checkin.BooleanBranchStep
import com.wesley.beefree.domain.checkin.DailyCheckInAnswer
import com.wesley.beefree.domain.checkin.DailyCheckInFlow
import com.wesley.beefree.domain.checkin.DailyCheckInStep
import com.wesley.beefree.domain.checkin.DualScaleStep
import com.wesley.beefree.domain.checkin.EmotionalRecordStep
import com.wesley.beefree.domain.checkin.MindfulnessStep
import com.wesley.beefree.domain.checkin.MultiSelectStep
import com.wesley.beefree.domain.checkin.RelapseRegistrationStep
import com.wesley.beefree.domain.checkin.ScaleStep
import com.wesley.beefree.domain.checkin.SingleSelectStep
import com.wesley.beefree.domain.checkin.SingleSelectWithContextStep
import com.wesley.beefree.domain.checkin.TextStep
import com.wesley.beefree.domain.checkin.TextWithSuggestionsStep
import com.wesley.beefree.domain.checkin.TherapeuticActivityStep
import com.wesley.beefree.domain.checkin.VideoWatchStep
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.treatments.checkin.ActDailyCheckInFlow
import com.wesley.beefree.domain.treatments.checkin.TccDailyCheckInFlow
import com.wesley.beefree.ui.components.checkin.daily.BooleanBranchStepContent
import com.wesley.beefree.ui.components.checkin.daily.DualScaleStepContent
import com.wesley.beefree.ui.components.checkin.daily.EmotionalRecordStepContent
import com.wesley.beefree.ui.components.checkin.daily.MindfulnessStepContent
import com.wesley.beefree.ui.components.checkin.daily.MultiSelectStepContent
import com.wesley.beefree.ui.components.checkin.daily.RelapseRegistrationStepContent
import com.wesley.beefree.ui.components.checkin.daily.ScaleStepContent
import com.wesley.beefree.ui.components.checkin.daily.SingleSelectStepContent
import com.wesley.beefree.ui.components.checkin.daily.SingleSelectWithContextStepContent
import com.wesley.beefree.ui.components.checkin.daily.TextStepContent
import com.wesley.beefree.ui.components.checkin.daily.TextWithSuggestionsStepContent
import com.wesley.beefree.ui.components.checkin.daily.TherapeuticActivityStepContent
import com.wesley.beefree.ui.components.checkin.daily.VideoWatchStepContent
import com.wesley.beefree.ui.components.checkin.daily.mapScaleStepType
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeStepper
import com.wesley.beefree.ui.theme.BeeFreeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyCheckInFlowScreen(
    flow: DailyCheckInFlow,
    treatmentProfile: TreatmentProfile,
    currentStep: DailyCheckInStep,
    stepNumber: Int,
    totalSteps: Int,
    answers: Map<String, DailyCheckInAnswer>,
    hasEmotionalRecordToday: Boolean,
    todaysEmotionRecord: Map<FeelingType, Int>?,
    selectedActivity: ActivityType?,
    previousObjective: String? = null,
    onAnswer: (String, DailyCheckInAnswer) -> Unit,
    onSelectActivity: (ActivityType) -> Unit,
    onGoRecord: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onClose: () -> Unit,
    canGoBack: Boolean,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceContainerLowest),
                title = { BeeHeadlineSmall(stringResource(R.string.daily_checkin_flow_title)) },
                navigationIcon = {
                    if (canGoBack) {
                        IconButton(onClick = onPrevious) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.daily_checkin_close_description))
                    }
                },
            )
        },
        bottomBar = {
            DailyCheckInBottomBar(
                step = currentStep,
                enabled = isStepAnswered(currentStep, answers, hasEmotionalRecordToday),
                isLastStep = isLastStep(currentStep, flow, answers),
                hasEmotionalRecordToday = hasEmotionalRecordToday,
                onNext = onNext,
                onGoRecord = onGoRecord,
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = BeeSpacing.L),
        ) {
            Spacer(modifier = Modifier.height(BeeSpacing.M))
            BeeStepper(
                step = stepNumber,
                total = totalSteps,
            )
            Spacer(modifier = Modifier.height(BeeSpacing.L))
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
            ) {
                StepBody(
                    step = currentStep,
                    answers = answers,
                    hasEmotionalRecordToday = hasEmotionalRecordToday,
                    todaysEmotionRecord = todaysEmotionRecord,
                    selectedActivity = selectedActivity,
                    previousObjective = previousObjective,
                    onAnswer = onAnswer,
                    onSelectActivity = onSelectActivity,
                    onGoRecord = onGoRecord,
                )
                Spacer(modifier = Modifier.height(BeeSpacing.L))
            }
        }
    }
}

@Composable
private fun DailyCheckInBottomBar(
    step: DailyCheckInStep,
    enabled: Boolean,
    isLastStep: Boolean,
    hasEmotionalRecordToday: Boolean,
    onNext: () -> Unit,
    onGoRecord: () -> Unit,
) {
    val shouldStartRecord = step is EmotionalRecordStep && !hasEmotionalRecordToday
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(BeeSpacing.M),
        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
    ) {
        BeeButtonPrimary(
            onClick = if (shouldStartRecord) onGoRecord else onNext,
            modifier = Modifier.weight(1f),
            enabled = if (shouldStartRecord) true else enabled,
        ) {
            BeeLabelLarge(
                text =
                    if (shouldStartRecord) {
                        stringResource(R.string.daily_checkin_emotional_record_go_record)
                    } else if (isLastStep) {
                        stringResource(R.string.daily_checkin_log)
                    } else {
                        stringResource(R.string.daily_checkin_continue)
                    },
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
private fun StepBody(
    step: DailyCheckInStep,
    answers: Map<String, DailyCheckInAnswer>,
    hasEmotionalRecordToday: Boolean,
    todaysEmotionRecord: Map<FeelingType, Int>?,
    selectedActivity: ActivityType?,
    previousObjective: String?,
    onAnswer: (String, DailyCheckInAnswer) -> Unit,
    onSelectActivity: (ActivityType) -> Unit,
    onGoRecord: () -> Unit,
) {
    when (step) {
        is EmotionalRecordStep -> {
            EmotionalRecordStepContent(
                spec = step,
                emotionRecordValues = todaysEmotionRecord?.mapValues { it.value.toFloat() },
                onGoRecord = onGoRecord,
            )
        }

        is SingleSelectWithContextStep -> {
            val current = answers[step.id] as? DailyCheckInAnswer.SingleSelectWithContext
            SingleSelectWithContextStepContent(
                spec = step,
                selectedId = current?.id,
                contextValue = current?.context.orEmpty(),
                previousObjective = previousObjective,
                onSelect = { id ->
                    onAnswer(step.id, DailyCheckInAnswer.SingleSelectWithContext(id, current?.context))
                },
                onContextChange = { ctx ->
                    onAnswer(step.id, DailyCheckInAnswer.SingleSelectWithContext(current?.id ?: "", ctx.takeIf { it.isNotBlank() }))
                },
            )
        }

        is TherapeuticActivityStep -> {
            TherapeuticActivityStepContent(
                spec = step,
                selectedActivity = selectedActivity,
                onSelectActivity = { type ->
                    onSelectActivity(type)
                    onAnswer(step.id, DailyCheckInAnswer.TherapeuticActivity(type))
                },
            )
        }

        is TextWithSuggestionsStep -> {
            val value = (answers[step.id] as? DailyCheckInAnswer.TextWithSuggestions)?.value.orEmpty()
            TextWithSuggestionsStepContent(
                spec = step,
                value = value,
                onChange = { onAnswer(step.id, DailyCheckInAnswer.TextWithSuggestions(it)) },
            )
        }

        is RelapseRegistrationStep -> {
            val current = answers[step.id] as? DailyCheckInAnswer.RelapseRegistration
            RelapseRegistrationStepContent(
                spec = step,
                selectedHour = current?.hour,
                selectedMinute = current?.minute,
                selectedTriggers = current?.triggers.orEmpty(),
                contextValue = current?.context.orEmpty(),
                onTimeSelect = { hour, minute ->
                    onAnswer(step.id, DailyCheckInAnswer.RelapseRegistration(hour, minute, current?.triggers.orEmpty(), current?.context))
                },
                onTriggerToggle = { id ->
                    val triggers = current?.triggers.orEmpty()
                    val next = if (id in triggers) triggers - id else triggers + id
                    onAnswer(step.id, DailyCheckInAnswer.RelapseRegistration(current?.hour, current?.minute, next, current?.context))
                },
                onContextChange = { ctx ->
                    onAnswer(
                        step.id,
                        DailyCheckInAnswer.RelapseRegistration(
                            current?.hour,
                            current?.minute,
                            current?.triggers.orEmpty(),
                            ctx.takeIf { it.isNotBlank() },
                        ),
                    )
                },
            )
        }

        is ScaleStep -> {
            val value = (answers[step.id] as? DailyCheckInAnswer.Scale)?.value ?: 5
            val ui = mapScaleStepType(step.type)
            ScaleStepContent(
                spec = step,
                value = value,
                onChange = { onAnswer(step.id, DailyCheckInAnswer.Scale(it)) },
                color = ui.color,
                icon = ui.icon,
                label = stringResource(ui.labelRes),
                hint = stringResource(ui.hintRes),
            )
        }

        is DualScaleStep -> {
            val current = answers[step.id] as? DailyCheckInAnswer.DualScale
            val first = current?.first ?: 5
            val second = current?.second ?: 5
            DualScaleStepContent(
                spec = step,
                firstValue = first,
                secondValue = second,
                onFirstChange = { onAnswer(step.id, DailyCheckInAnswer.DualScale(it, second)) },
                onSecondChange = { onAnswer(step.id, DailyCheckInAnswer.DualScale(first, it)) },
            )
        }

        is MultiSelectStep -> {
            val current = answers[step.id] as? DailyCheckInAnswer.MultiSelect
            val ids = current?.ids.orEmpty()
            val context = current?.context.orEmpty()
            MultiSelectStepContent(
                spec = step,
                selectedIds = ids,
                contextValue = context,
                onToggle = { id ->
                    val next = if (ids.contains(id)) ids - id else ids + id
                    onAnswer(step.id, DailyCheckInAnswer.MultiSelect(next, context.takeIf { it.isNotBlank() }))
                },
                onContextChange = { value ->
                    onAnswer(step.id, DailyCheckInAnswer.MultiSelect(ids, value.takeIf { it.isNotBlank() }))
                },
            )
        }

        is SingleSelectStep -> {
            val selected = (answers[step.id] as? DailyCheckInAnswer.SingleSelect)?.id
            SingleSelectStepContent(
                spec = step,
                selectedId = selected,
                onSelect = { onAnswer(step.id, DailyCheckInAnswer.SingleSelect(it)) },
            )
        }

        is TextStep -> {
            val value = (answers[step.id] as? DailyCheckInAnswer.Text)?.value.orEmpty()
            TextStepContent(
                spec = step,
                value = value,
                onChange = { onAnswer(step.id, DailyCheckInAnswer.Text(it)) },
            )
        }

        is BooleanBranchStep -> {
            val value = (answers[step.id] as? DailyCheckInAnswer.Bool)?.value
            BooleanBranchStepContent(
                spec = step,
                value = value,
                onChange = { onAnswer(step.id, DailyCheckInAnswer.Bool(it)) },
            )
        }

        is VideoWatchStep -> {
            VideoWatchStepContent(
                step = step,
                onWatched = { onAnswer(step.id, DailyCheckInAnswer.VideoWatch) },
            )
        }

        is MindfulnessStep -> {
            MindfulnessStepContent(
                step = step,
                onDone = { onAnswer(step.id, DailyCheckInAnswer.Mindfulness) },
            )
        }
    }
}

private fun isStepAnswered(
    step: DailyCheckInStep,
    answers: Map<String, DailyCheckInAnswer>,
    hasEmotionalRecordToday: Boolean,
): Boolean {
    val answer = answers[step.id]
    return when (step) {
        is EmotionalRecordStep -> hasEmotionalRecordToday || answer is DailyCheckInAnswer.EmotionalRecord
        is SingleSelectWithContextStep -> (answer as? DailyCheckInAnswer.SingleSelectWithContext)?.id?.isNotBlank() == true
        is TherapeuticActivityStep -> answer is DailyCheckInAnswer.TherapeuticActivity
        is TextWithSuggestionsStep -> (answer as? DailyCheckInAnswer.TextWithSuggestions)?.value?.isNotBlank() == true
        is RelapseRegistrationStep -> {
            val reg = answer as? DailyCheckInAnswer.RelapseRegistration
            reg != null && reg.hour != null && reg.triggers.isNotEmpty()
        }
        is ScaleStep -> answer is DailyCheckInAnswer.Scale
        is DualScaleStep -> answer is DailyCheckInAnswer.DualScale
        is MultiSelectStep -> (answer as? DailyCheckInAnswer.MultiSelect)?.ids?.isNotEmpty() == true
        is SingleSelectStep -> answer is DailyCheckInAnswer.SingleSelect
        is TextStep -> (answer as? DailyCheckInAnswer.Text)?.value?.isNotBlank() == true
        is BooleanBranchStep -> answer is DailyCheckInAnswer.Bool
        is VideoWatchStep -> answer is DailyCheckInAnswer.VideoWatch
        is MindfulnessStep -> answer is DailyCheckInAnswer.Mindfulness
    }
}

private fun isLastStep(
    step: DailyCheckInStep,
    flow: DailyCheckInFlow,
    answers: Map<String, DailyCheckInAnswer>,
): Boolean {
    val last = flow.steps.lastOrNull() ?: return false
    if (last is BooleanBranchStep) {
        val answer = answers[last.id] as? DailyCheckInAnswer.Bool
        if (answer != null) {
            val terminal = if (answer.value) last.onYes else last.onNo
            return terminal.id == step.id
        }
        return false
    }
    return last.id == step.id
}

@Composable
private fun FlowStepPreview(
    flow: DailyCheckInFlow,
    profile: TreatmentProfile,
    stepIndex: Int,
) {
    val steps = flow.steps
    val currentStep = steps[stepIndex]
    BeeFreeTheme {
        DailyCheckInFlowScreen(
            flow = flow,
            treatmentProfile = profile,
            currentStep = currentStep,
            stepNumber = stepIndex + 1,
            totalSteps = steps.size,
            answers = emptyMap(),
            hasEmotionalRecordToday = false,
            todaysEmotionRecord = null,
            selectedActivity = null,
            canGoBack = stepIndex > 0,
            onAnswer = { _, _ -> },
            onSelectActivity = {},
            onGoRecord = {},
            onPrevious = {},
            onNext = {},
            onClose = {},
        )
    }
}

@Composable
private fun FlowBranchTerminalPreview(
    flow: DailyCheckInFlow,
    profile: TreatmentProfile,
    takeBranch: Boolean,
) {
    val steps = flow.steps
    val branchStep = steps.last() as BooleanBranchStep
    val currentStep = if (takeBranch) branchStep.onYes else branchStep.onNo
    BeeFreeTheme {
        DailyCheckInFlowScreen(
            flow = flow,
            treatmentProfile = profile,
            currentStep = currentStep,
            stepNumber = steps.size + 1,
            totalSteps = steps.size + 1,
            answers = mapOf(branchStep.id to DailyCheckInAnswer.Bool(takeBranch)),
            hasEmotionalRecordToday = false,
            todaysEmotionRecord = null,
            selectedActivity = null,
            canGoBack = true,
            onAnswer = { _, _ -> },
            onSelectActivity = {},
            onGoRecord = {},
            onPrevious = {},
            onNext = {},
            onClose = {},
        )
    }
}

@Preview(showBackground = true, name = "TCC - step 1: Emotional Record")
@Composable
private fun DailyCheckInFlowTccStep1Preview() = FlowStepPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, 0)

@Preview(showBackground = true, name = "TCC - step 2: Objective Review")
@Composable
private fun DailyCheckInFlowTccStep2Preview() = FlowStepPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, 1)

@Preview(showBackground = true, name = "TCC - step 3: Therapeutic Activity")
@Composable
private fun DailyCheckInFlowTccStep3Preview() = FlowStepPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, 2)

@Preview(showBackground = true, name = "TCC - mindfulness 1: Breathing")
@Composable
private fun DailyCheckInFlowTccMindfulness1Preview() =
    FlowActivitySubStepPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, ActivityType.MINDFULNESS, 0)

@Preview(showBackground = true, name = "TCC - exercise 1: Situation")
@Composable
private fun DailyCheckInFlowTccExercise1Preview() =
    FlowActivitySubStepPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, ActivityType.PROFILE_EXERCISE, 0)

@Preview(showBackground = true, name = "TCC - exercise 2: Auto Thought")
@Composable
private fun DailyCheckInFlowTccExercise2Preview() =
    FlowActivitySubStepPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, ActivityType.PROFILE_EXERCISE, 1)

@Preview(showBackground = true, name = "TCC - exercise 3: Emotion")
@Composable
private fun DailyCheckInFlowTccExercise3Preview() =
    FlowActivitySubStepPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, ActivityType.PROFILE_EXERCISE, 2)

@Preview(showBackground = true, name = "TCC - exercise 4: Alternative Thought")
@Composable
private fun DailyCheckInFlowTccExercise4Preview() =
    FlowActivitySubStepPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, ActivityType.PROFILE_EXERCISE, 3)

@Preview(showBackground = true, name = "TCC - video 1: Watch")
@Composable
private fun DailyCheckInFlowTccVideo1Preview() =
    FlowActivitySubStepPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, ActivityType.VIDEO, 0)

@Preview(showBackground = true, name = "TCC - video 2: Reflection")
@Composable
private fun DailyCheckInFlowTccVideo2Preview() =
    FlowActivitySubStepPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, ActivityType.VIDEO, 1)

@Preview(showBackground = true, name = "TCC - step 4: Goal Definition")
@Composable
private fun DailyCheckInFlowTccStep4Preview() = FlowStepPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, 3)

@Preview(showBackground = true, name = "TCC - step 5: Relapse Check")
@Composable
private fun DailyCheckInFlowTccStep5Preview() = FlowStepPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, 4)

@Preview(showBackground = true, name = "TCC - step 6: Day Evaluation")
@Composable
private fun DailyCheckInFlowTccStep6NoPreview() = FlowBranchTerminalPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, false)

@Preview(showBackground = true, name = "TCC - step 6: Relapse Registration")
@Composable
private fun DailyCheckInFlowTccStep6YesPreview() = FlowBranchTerminalPreview(TccDailyCheckInFlow.flow, TreatmentProfile.TCC, true)

@Preview(showBackground = true, name = "ACT - step 1: Emotional Record")
@Composable
private fun DailyCheckInFlowActStep1Preview() = FlowStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, 0)

@Preview(showBackground = true, name = "ACT - step 2: Objective Review")
@Composable
private fun DailyCheckInFlowActStep2Preview() = FlowStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, 1)

@Preview(showBackground = true, name = "ACT - step 3: Therapeutic Activity")
@Composable
private fun DailyCheckInFlowActStep3Preview() = FlowStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, 2)

@Preview(showBackground = true, name = "ACT - mindfulness 1: Breathing")
@Composable
private fun DailyCheckInFlowActMindfulness1Preview() =
    FlowActivitySubStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, ActivityType.MINDFULNESS, 0)

@Preview(showBackground = true, name = "ACT - exercise 1: Craving")
@Composable
private fun DailyCheckInFlowActExercise1Preview() =
    FlowActivitySubStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, ActivityType.PROFILE_EXERCISE, 0)

@Preview(showBackground = true, name = "ACT - exercise 2: Acceptance")
@Composable
private fun DailyCheckInFlowActExercise2Preview() =
    FlowActivitySubStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, ActivityType.PROFILE_EXERCISE, 1)

@Preview(showBackground = true, name = "ACT - exercise 3: Emotions")
@Composable
private fun DailyCheckInFlowActExercise3Preview() =
    FlowActivitySubStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, ActivityType.PROFILE_EXERCISE, 2)

@Preview(showBackground = true, name = "ACT - exercise 4: Presence")
@Composable
private fun DailyCheckInFlowActExercise4Preview() =
    FlowActivitySubStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, ActivityType.PROFILE_EXERCISE, 3)

@Preview(showBackground = true, name = "ACT - exercise 5: Committed Action")
@Composable
private fun DailyCheckInFlowActExercise5Preview() =
    FlowActivitySubStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, ActivityType.PROFILE_EXERCISE, 4)

@Preview(showBackground = true, name = "ACT - exercise 6: Direction")
@Composable
private fun DailyCheckInFlowActExercise6Preview() =
    FlowActivitySubStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, ActivityType.PROFILE_EXERCISE, 5)

@Preview(showBackground = true, name = "ACT - video 1: Watch")
@Composable
private fun DailyCheckInFlowActVideo1Preview() =
    FlowActivitySubStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, ActivityType.VIDEO, 0)

@Preview(showBackground = true, name = "ACT - video 2: Reflection")
@Composable
private fun DailyCheckInFlowActVideo2Preview() =
    FlowActivitySubStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, ActivityType.VIDEO, 1)

@Preview(showBackground = true, name = "ACT - step 4: Goal Definition")
@Composable
private fun DailyCheckInFlowActStep4Preview() = FlowStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, 3)

@Preview(showBackground = true, name = "ACT - step 5: Relapse Check")
@Composable
private fun DailyCheckInFlowActStep5Preview() = FlowStepPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, 4)

@Preview(showBackground = true, name = "ACT - step 6: Day Evaluation")
@Composable
private fun DailyCheckInFlowActStep6NoPreview() = FlowBranchTerminalPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, false)

@Preview(showBackground = true, name = "ACT - step 6: Relapse Registration")
@Composable
private fun DailyCheckInFlowActStep6YesPreview() = FlowBranchTerminalPreview(ActDailyCheckInFlow.flow, TreatmentProfile.ACT, true)

@Composable
private fun FlowActivitySubStepPreview(
    flow: DailyCheckInFlow,
    profile: TreatmentProfile,
    activityType: ActivityType,
    subStepIndex: Int,
) {
    val steps = flow.steps
    val activityStep = steps[2] as TherapeuticActivityStep
    val option = activityStep.activityOptions.first { it.type == activityType }
    val currentStep = option.subSteps[subStepIndex]
    val totalSteps = steps.size + option.subSteps.size
    BeeFreeTheme {
        DailyCheckInFlowScreen(
            flow = flow,
            treatmentProfile = profile,
            currentStep = currentStep,
            stepNumber = 3 + subStepIndex + 1,
            totalSteps = totalSteps,
            answers = mapOf(activityStep.id to DailyCheckInAnswer.TherapeuticActivity(activityType)),
            hasEmotionalRecordToday = false,
            todaysEmotionRecord = null,
            selectedActivity = activityType,
            canGoBack = true,
            onAnswer = { _, _ -> },
            onSelectActivity = {},
            onGoRecord = {},
            onPrevious = {},
            onNext = {},
            onClose = {},
        )
    }
}
