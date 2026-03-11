package com.wesley.beefree.ui.screens.onboading

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingMascot
import com.wesley.beefree.ui.components.OnboardingTitle

@Composable
fun OnBoardingWelcomeScreen(onNext: () -> Unit) {
    OnboardingLayout {
        OnboardingMascot()
        Spacer(modifier = Modifier.padding(8.dp))
        OnboardingTitle(stringResource(R.string.onboarding_welcome_title))
        Spacer(modifier = Modifier.padding(24.dp))
        Text(
            text = stringResource(R.string.onboarding_welcome_body),
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
        )
        Spacer(modifier = Modifier.padding(24.dp))
        Button(onClick = onNext) {
            Text(stringResource(R.string.onboarding_btn_continue))
        }
    }
}
