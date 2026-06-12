package com.wesley.beefree.ui.screens.checkin.daily

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.wesley.beefree.ui.viewmodel.CheckInViewModel
import com.wesley.beefree.ui.viewmodel.DailyCheckInPhase

@Composable
fun DailyCheckInRouter(
    viewModel: CheckInViewModel,
    onClose: () -> Unit,
) {
    val phase by viewModel.dailyPhase.collectAsState()
    val flow by viewModel.dailyFlow.collectAsState()
    val answers by viewModel.dailyAnswers.collectAsState()
    val visited by viewModel.dailyVisitedSteps.collectAsState()
    val profileName by viewModel.profileName.collectAsState()
    val treatmentProfile by viewModel.treatmentProfile.collectAsState()
    val isCompleted by viewModel.isCompleted.collectAsState()
    val hasEmotionalRecordToday by viewModel.hasEmotionalRecordToday.collectAsState()
    val selectedActivity by viewModel.selectedActivity.collectAsState()
    val todaysEmotionRecord by viewModel.todaysEmotionRecord.collectAsState()
    val previousObjective by viewModel.previousObjective.collectAsState()

    val resolvedFlow = flow ?: return

    when (phase) {
        DailyCheckInPhase.INVITE ->
            DailyCheckInInviteScreen(
                flow = resolvedFlow,
                profileName = profileName,
                isCheckInDone = isCompleted,
                onStartCheckIn = { viewModel.startDailyFlow() },
                onStartEmotionalRecord = { viewModel.startEmotionRecord() },
            )

        DailyCheckInPhase.FLOW -> {
            val current = viewModel.currentDailyStep() ?: return
            DailyCheckInFlowScreen(
                flow = resolvedFlow,
                treatmentProfile = treatmentProfile,
                currentStep = current,
                stepNumber = viewModel.currentStepNumber(),
                totalSteps = viewModel.totalStepsEstimate(),
                answers = answers,
                hasEmotionalRecordToday = hasEmotionalRecordToday,
                todaysEmotionRecord = todaysEmotionRecord,
                selectedActivity = selectedActivity,
                previousObjective = previousObjective,
                onAnswer = { id, answer -> viewModel.setDailyAnswer(id, answer) },
                onSelectActivity = { type -> viewModel.selectActivity(type) },
                onGoRecord = { viewModel.startEmotionRecord() },
                onPrevious = { viewModel.previousDailyStep() },
                onNext = { viewModel.nextDailyStep() },
                onClose = onClose,
                canGoBack = visited.size > 1,
            )
        }

        DailyCheckInPhase.DONE ->
            DailyCheckInDoneScreen(onClose = onClose)
    }
}
