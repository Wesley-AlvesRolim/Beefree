package com.wesley.beefree.ui.screens.onboading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.OnboardingViewModelImpl
import com.wesley.beefree.ui.viewmodel.ports.OnboardingStep
import com.wesley.beefree.ui.viewmodel.ports.OnboardingViewModelPort

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModelPort = viewModel<OnboardingViewModelImpl>(),
) {
    val context = LocalContext.current
    val lifecycleOwner = rememberUpdatedState(newValue = LocalLifecycleOwner.current)

    val currentStep by viewModel.currentStep.collectAsState()
    val selectedAddictions by viewModel.selectedAddictions.collectAsState()
    val isAccessibilityEnabled by viewModel.isAccessibilityEnabled.collectAsState()
    val isOverlayEnabled by viewModel.isOverlayEnabled.collectAsState()

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    viewModel.updatePermissions(context)
                }
            }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    when (currentStep) {
        OnboardingStep.WELCOME ->
            OnBoardingWelcomeScreen(onNext = { viewModel.nextStep() })

        OnboardingStep.HOW_IT_WORKS ->
            OnBoardingHowItWorksScreen(onNext = { viewModel.nextStep() })

        OnboardingStep.ADDICTION_SELECTOR ->
            OnboardingAddictionSelectorScreen(
                selectedAddictions = selectedAddictions,
                onToggleAddiction = { viewModel.toggleAddiction(it) },
                onNext = { viewModel.nextStep() },
                onBack = { viewModel.previousStep() },
            )

        OnboardingStep.REQUEST_PERMISSIONS ->
            OnBoardingRequestPermissionsScreen(
                onNext = { viewModel.nextStep() },
                onBack = { viewModel.previousStep() },
            )

        OnboardingStep.REQUEST_PERMISSION_SCREEN_MONITOR ->
            RequestMonitorPermissionScreen(
                isAccessibilityEnabled = isAccessibilityEnabled,
                onOpenSettings = { viewModel.openAccessibilitySettings(context) },
                onNext = { viewModel.nextStep() },
                onBack = { viewModel.previousStep() },
            )

        OnboardingStep.REQUEST_PERMISSION_SCREEN_OVERLAY ->
            RequestPermissionOverlayScreen(
                isOverlayEnabled = isOverlayEnabled,
                onOpenSettings = { viewModel.openOverlaySettings(context) },
                onNext = { viewModel.nextStep() },
                onBack = { viewModel.previousStep() },
            )

        OnboardingStep.FINISH ->
            OnboardingFinishScreen(
                onFinish = { viewModel.finishOnboarding(onFinish) },
                onBack = { viewModel.previousStep() },
            )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    BeeFreeTheme {
        OnboardingScreen(onFinish = {})
    }
}
