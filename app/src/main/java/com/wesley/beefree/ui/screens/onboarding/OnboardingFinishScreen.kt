package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.wesley.beefree.ui.components.OnboardingTitle

@Composable
fun OnboardingFinishScreen(onFinish: () -> Unit) {
    OnboardingLayout {
        OnboardingMascot()
        Spacer(modifier = Modifier.padding(8.dp))
        OnboardingTitle(stringResource(R.string.onboarding_finish_title))
        Spacer(modifier = Modifier.padding(24.dp))
        Text(
            text = stringResource(R.string.onboarding_finish_body),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Left,
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Button(onClick = onFinish) {
            Text(stringResource(R.string.onboarding_btn_start))
        }
    }
}
