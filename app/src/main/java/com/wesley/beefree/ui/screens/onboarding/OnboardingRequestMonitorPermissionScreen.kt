package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingMascot
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.*

@Composable
fun RequestMonitorPermissionScreen(
    isAccessibilityEnabled: Boolean,
    onOpenSettings: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    OnboardingLayout(onBack = onBack) {
        OnboardingMascot()
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_monitor_permission_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        BeeBodyLarge(
            text = stringResource(R.string.onboarding_monitor_permission_body),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.XL))

        if (isAccessibilityEnabled) {
            BeeButtonSecondary(
                onClick = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
            ) {
                BeeLabelLarge(stringResource(R.string.onboarding_monitor_permission_active))
            }
        } else {
            BeeButtonPrimary(
                onClick = onOpenSettings,
                modifier = Modifier.fillMaxWidth(),
            ) {
                BeeLabelLarge(stringResource(R.string.onboarding_monitor_permission_enable), color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingNavigationRow(onNext = onNext, nextEnabled = isAccessibilityEnabled)
    }
}
