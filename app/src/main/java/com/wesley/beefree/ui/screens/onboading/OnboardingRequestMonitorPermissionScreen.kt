package com.wesley.beefree.ui.screens.onboading

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingMascot
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle

@Composable
fun RequestMonitorPermissionScreen(
    isAccessibilityEnabled: Boolean,
    onOpenSettings: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    OnboardingLayout {
        OnboardingMascot()
        Spacer(modifier = Modifier.padding(8.dp))
        OnboardingTitle(stringResource(R.string.onboarding_monitor_permission_title))
        Spacer(modifier = Modifier.padding(24.dp))
        Text(
            text = stringResource(R.string.onboarding_monitor_permission_body),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Left,
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            onClick = onOpenSettings,
            enabled = !isAccessibilityEnabled,
            colors =
                if (isAccessibilityEnabled) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    ButtonDefaults.buttonColors()
                },
        ) {
            Text(
                stringResource(
                    if (isAccessibilityEnabled) {
                        R.string.onboarding_monitor_permission_active
                    } else {
                        R.string.onboarding_monitor_permission_enable
                    },
                ),
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        OnboardingNavigationRow(onBack = onBack, onNext = onNext, nextEnabled = isAccessibilityEnabled)
    }
}
