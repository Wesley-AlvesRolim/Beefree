package com.wesley.beefreepoc.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wesley.beefreepoc.R
import com.wesley.beefreepoc.ui.theme.BeeFreePOCTheme
import com.wesley.beefreepoc.utils.AccessibilityUtils
import com.wesley.beefreepoc.utils.OverlayUtils

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var currentScreen by remember { mutableStateOf(OnboardingStep.WELCOME) }

    when (currentScreen) {
        OnboardingStep.WELCOME -> WelcomeScreen(onNext = { currentScreen = OnboardingStep.HELP })
        OnboardingStep.HELP ->
            HelpScreen(
                onNext = { currentScreen = OnboardingStep.PERMISSION_SCREEN_MONITOR },
                onBack = { currentScreen = OnboardingStep.WELCOME },
            )
        OnboardingStep.PERMISSION_SCREEN_MONITOR ->
            PermissionMonitorScreen(
                onNext = { currentScreen = OnboardingStep.PERMISSION_SCREEN_OVERLAY },
                onBack = { currentScreen = OnboardingStep.HELP },
            )
        OnboardingStep.PERMISSION_SCREEN_OVERLAY ->
            PermissionOverlayScreen(
                onFinish = onFinish,
                onBack = { currentScreen = OnboardingStep.PERMISSION_SCREEN_MONITOR },
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

    fun requestScreenMonitorPermission() {
        AccessibilityUtils.openAccessibilitySettings(context)
    }

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
        Button(onClick = { requestScreenMonitorPermission() }) {
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

    fun requestOverlayPermission() {
        OverlayUtils.openSettingsToEnableTheOverlayPermission(context)
    }

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
        Button(onClick = { requestOverlayPermission() }) {
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
    BeeFreePOCTheme {
        OnboardingScreen(onFinish = {})
    }
}
