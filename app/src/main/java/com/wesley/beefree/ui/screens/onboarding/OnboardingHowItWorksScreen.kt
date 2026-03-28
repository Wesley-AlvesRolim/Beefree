package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun OnboardingHowItWorksScreen(onNext: () -> Unit) {
    OnboardingLayout {
        OnboardingMascot()
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_how_it_works_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            HowItWorksFeatureItem(
                title = stringResource(R.string.onboarding_how_it_works_detect_title),
                body = stringResource(R.string.onboarding_how_it_works_detect_body),
            )
            Spacer(modifier = Modifier.height(BeeSpacing.M))
            HowItWorksFeatureItem(
                title = stringResource(R.string.onboarding_how_it_works_interrupt_title),
                body = stringResource(R.string.onboarding_how_it_works_interrupt_body),
            )
            Spacer(modifier = Modifier.height(BeeSpacing.M))
            HowItWorksFeatureItem(
                title = stringResource(R.string.onboarding_how_it_works_progress_title),
                body = stringResource(R.string.onboarding_how_it_works_progress_body),
            )
        }
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        OnboardingNavigationRow(onNext)
    }
}

@Composable
private fun HowItWorksFeatureItem(
    title: String,
    body: String,
) {
    BeeHeadlineSmall(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Left,
    )
    Spacer(modifier = Modifier.height(BeeSpacing.XS))
    BeeBodyMedium(
        text = body,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Left,
    )
}
