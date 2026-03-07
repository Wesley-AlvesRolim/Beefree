package com.wesley.beefree.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wesley.beefree.R
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = viewModel(),
) {
    val currentStep by viewModel.currentStep.collectAsState()

    when (currentStep) {
        OnboardingStep.WELCOME -> WelcomeScreen(onNext = { viewModel.nextStep() })
        OnboardingStep.HELP ->
            HelpScreen(
                onNext = { viewModel.nextStep() },
                onBack = { viewModel.previousStep() },
            )
        OnboardingStep.PERMISSION_SCREEN_MONITOR ->
            PermissionMonitorScreen(
                onNext = { viewModel.nextStep() },
                onBack = { viewModel.previousStep() },
            )
        OnboardingStep.PERMISSION_SCREEN_OVERLAY ->
            PermissionOverlayScreen(
                onFinish = onFinish,
                onBack = { viewModel.previousStep() },
            )
    }
}

enum class OnboardingStep {
    WELCOME,
    HELP,
    PERMISSION_SCREEN_MONITOR,
    PERMISSION_SCREEN_OVERLAY,
}

@Composable
fun WelcomeScreen(onNext: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Welcome to BeeFree!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Button(onClick = onNext) {
            Text("Next")
        }
    }
}

@Composable
fun HelpScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "What you wanna help?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(onClick = onBack) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onNext) {
                Text("Next")
            }
        }
    }
}

@Composable
fun PermissionMonitorScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Permissions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(
            modifier = Modifier.padding(16.dp),
        )
        Text(
            text = context.getString(R.string.request_screen_monitor_permission),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(
            modifier = Modifier.padding(8.dp),
        )
        Button(onClick = {
            com.wesley.beefree.utils.AccessibilityUtils
                .openAccessibilitySettings(context)
        }) {
            Text("Request permission")
        }
        Spacer(
            modifier = Modifier.padding(16.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(onClick = onBack) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onNext) {
                Text("Next")
            }
        }
    }
}

@Composable
fun PermissionOverlayScreen(
    onFinish: () -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Permissions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(
            modifier = Modifier.padding(16.dp),
        )
        Text(
            text = context.getString(R.string.request_overlay_permission),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(
            modifier = Modifier.padding(8.dp),
        )
        Button(onClick = {
            com.wesley.beefree.utils.OverlayUtils
                .openSettingsToEnableTheOverlayPermission(context)
        }) {
            Text("Request permission")
        }
        Spacer(
            modifier = Modifier.padding(16.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(onClick = onBack) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onFinish) {
                Text("Finish")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    BeeFreeTheme {
        OnboardingScreen(onFinish = {})
    }
}
