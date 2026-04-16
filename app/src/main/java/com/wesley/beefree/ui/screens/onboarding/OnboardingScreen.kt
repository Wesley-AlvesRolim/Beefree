package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.StepType
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeButtonGhost
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.mocks.OnboardingViewModelMock
import com.wesley.beefree.ui.viewmodel.ports.OnboardingViewModelPort

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModelPort,
) {
    val context = LocalContext.current
    val lifecycleOwner = rememberUpdatedState(newValue = LocalLifecycleOwner.current)

    var finishError by remember { mutableStateOf<Throwable?>(null) }

    finishError?.let {
        AlertDialog(
            onDismissRequest = { finishError = null },
            confirmButton = {
                BeeButtonGhost(onClick = { finishError = null }) {
                    BeeLabelMedium(stringResource(android.R.string.ok))
                }
            },
            text = {
                BeeBodyMedium(stringResource(R.string.onboarding_save_error))
            },
        )
    }

    val currentStep by viewModel.currentStep.collectAsState()
    val answers by viewModel.answers.collectAsState()
    val scaleResult by viewModel.scaleResult.collectAsState()
    val clinicalProfile by viewModel.clinicalProfile.collectAsState()
    val isAccessibilityEnabled by viewModel.isAccessibilityEnabled.collectAsState()

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) viewModel.updatePermissions(context)
            }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    when (currentStep.type) {
        StepType.WELCOME ->
            OnboardingWelcomeScreen(onNext = { viewModel.next() })

        StepType.PRESENTATION ->
            OnboardingHowItWorksScreen(onNext = { viewModel.next() })

        StepType.ASK_NAME ->
            OnboardingAskNameScreen(
                answers = answers,
                onUpdate = { viewModel.updateAnswer(it) },
                onNext = { viewModel.next() },
            )

        StepType.ADDICTION_SELECTOR ->
            OnboardingAddictionSelectorScreen(
                answers = answers,
                onUpdate = { viewModel.updateAnswer(it) },
                onNext = { viewModel.next() },
            )

        StepType.GENDER ->
            OnboardingGenderScreen(
                answers = answers,
                onUpdate = { viewModel.updateAnswer(it) },
                onNext = { viewModel.next() },
                onBack = { viewModel.previous() },
            )

        StepType.PPCS6_FORM ->
            OnboardingPpcs6Screen(
                answers = answers,
                onUpdate = { viewModel.updateAnswer(it) },
                onNext = { viewModel.next() },
                onBack = { viewModel.previous() },
            )

        StepType.EMA_FORM ->
            OnboardingEmaScreen(
                answers = answers,
                onUpdate = { viewModel.updateAnswer(it) },
                onNext = { viewModel.next() },
                onBack = { viewModel.previous() },
            )

        StepType.FREQUENCY_FORM ->
            OnboardingFrequencyScreen(
                answers = answers,
                onUpdate = { viewModel.updateAnswer(it) },
                onNext = { viewModel.next() },
                onBack = { viewModel.previous() },
            )

        StepType.PGSI_FORM ->
            OnboardingPgsiScreen(
                answers = answers,
                onUpdate = { viewModel.updateAnswer(it) },
                onNext = { viewModel.next() },
                onBack = { viewModel.previous() },
            )

        StepType.SYMPTOMS ->
            OnboardingSymptomsScreen(
                answers = answers,
                onUpdate = { viewModel.updateAnswer(it) },
                onNext = { viewModel.next() },
                onBack = { viewModel.previous() },
            )

        StepType.NEURODIVERGENCE ->
            OnboardingNeurodivergenceScreen(
                answers = answers,
                onUpdate = { viewModel.updateAnswer(it) },
                onNext = { viewModel.next() },
                onBack = { viewModel.previous() },
            )

        StepType.HOBBIES ->
            OnboardingHobbiesScreen(
                answers = answers,
                onUpdate = { viewModel.updateAnswer(it) },
                onNext = { viewModel.next() },
                onBack = { viewModel.previous() },
            )

        StepType.GOALS ->
            OnboardingGoalsScreen(
                answers = answers,
                onUpdate = { viewModel.updateAnswer(it) },
                onNext = { viewModel.next() },
                onBack = { viewModel.previous() },
            )

        StepType.SCORE_RESULT ->
            OnboardingScoreResultScreen(
                scaleResult = scaleResult,
                clinicalProfile = clinicalProfile,
                onNext = { viewModel.next() },
            )

        StepType.CORE_VALUES ->
            OnboardingCoreValuesScreen(
                answers = answers,
                onUpdate = { viewModel.updateAnswer(it) },
                onNext = { viewModel.next() },
                onBack = { viewModel.previous() },
            )

        StepType.REQUEST_PERMISSIONS ->
            OnboardingRequestPermissionsScreen(
                onNext = { viewModel.next() },
                onBack = { viewModel.previous() },
            )

        StepType.REQUEST_PERMISSION_MONITOR ->
            RequestMonitorPermissionScreen(
                isAccessibilityEnabled = isAccessibilityEnabled,
                onOpenSettings = { viewModel.openAccessibilitySettings(context) },
                onNext = { viewModel.next() },
                onBack = { viewModel.previous() },
            )

        StepType.FINISH ->
            OnboardingFinishScreen(
                onFinish = {
                    viewModel.finishOnboarding(
                        onFinish = onFinish,
                        onError = { finishError = it },
                    )
                },
            )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    BeeFreeTheme {
        OnboardingScreen(onFinish = {}, viewModel = OnboardingViewModelMock())
    }
}
