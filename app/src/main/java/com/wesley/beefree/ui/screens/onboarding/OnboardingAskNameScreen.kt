package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.*

@Composable
fun OnboardingAskNameScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
) {
    OnboardingLayout(
        sectionTitle = stringResource(R.string.onboarding_section_sobre_voce),
        chapterNumber = 1,
        bottomBar = {
            OnboardingNavigationRow(
                onNext,
                nextEnabled = answers.userName.isNotBlank(),
            )
        },
        contentVerticalArrangement = Arrangement.Top,
    ) {
        BeeMascotSpeech(
            speechText = stringResource(R.string.onboarding_ask_name_mascot_speech),
            tone = BeeMascotSpeechTone.Primary,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_ask_name_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        BeeTextField(
            value = answers.userName,
            onValueChange = { onUpdate { copy(userName = it) } },
            label = { BeeBodyMedium(stringResource(R.string.onboarding_ask_name_hint)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
