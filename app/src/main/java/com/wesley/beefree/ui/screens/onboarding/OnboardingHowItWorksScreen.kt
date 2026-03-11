package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingMascot
import com.wesley.beefree.ui.components.OnboardingTitle

@Composable
fun OnboardingHowItWorksScreen(onNext: () -> Unit) {
    OnboardingLayout {
        OnboardingMascot()
        Spacer(modifier = Modifier.padding(8.dp))
        OnboardingTitle(stringResource(R.string.onboarding_how_it_works_title))
        Spacer(modifier = Modifier.padding(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            HowItWorksFeatureItem(
                title = stringResource(R.string.onboarding_how_it_works_detect_title),
                body = stringResource(R.string.onboarding_how_it_works_detect_body),
            )
            Spacer(modifier = Modifier.padding(16.dp))
            HowItWorksFeatureItem(
                title = stringResource(R.string.onboarding_how_it_works_interrupt_title),
                body = stringResource(R.string.onboarding_how_it_works_interrupt_body),
            )
            Spacer(modifier = Modifier.padding(16.dp))
            HowItWorksFeatureItem(
                title = stringResource(R.string.onboarding_how_it_works_progress_title),
                body = stringResource(R.string.onboarding_how_it_works_progress_body),
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Button(onClick = onNext) {
            Text(stringResource(R.string.onboarding_btn_continue))
        }
    }
}

@Composable
private fun HowItWorksFeatureItem(
    title: String,
    body: String,
) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Left,
    )
    Spacer(modifier = Modifier.padding(8.dp))
    Text(
        text = body,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Left,
    )
}
