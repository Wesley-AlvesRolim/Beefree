package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
fun OnboardingWelcomeScreen(onNext: () -> Unit) {
    OnboardingLayout(showTopBar = false) {
        OnboardingMascot()
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_welcome_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        BeeBodyLarge(
            text = stringResource(R.string.onboarding_welcome_body),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        OnboardingNavigationRow(onNext)
    }
}
