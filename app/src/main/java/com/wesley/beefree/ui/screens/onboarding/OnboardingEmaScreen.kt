package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingLikertOption
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.*

private val scaleLabels =
    listOf(
        R.string.onboarding_ema_label_0,
        R.string.onboarding_ema_label_1,
        R.string.onboarding_ema_label_2,
        R.string.onboarding_ema_label_3,
        R.string.onboarding_ema_label_4,
        R.string.onboarding_ema_label_5,
        R.string.onboarding_ema_label_6,
    )

@Composable
fun OnboardingEmaScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val currentAnswer = answers.emaAnswers.getOrNull(0)

    OnboardingLayout(
        onBack = onBack,
        sectionTitle = stringResource(R.string.onboarding_section_sua_perspectiva),
        chapterNumber = 3,
        bottomBar = {
            OnboardingNavigationRow(
                onNext = onNext,
                nextEnabled = currentAnswer != null,
            )
        },
    ) {
        BeeMascotSpeech(
            speechText = stringResource(R.string.onboarding_ema_mascot_speech),
            tone = BeeMascotSpeechTone.Primary,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_ema_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        BeeBodyLarge(
            text = stringResource(R.string.onboarding_ema_q1),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
            scaleLabels.forEachIndexed { index, labelRes ->
                OnboardingLikertOption(
                    number = index + 1,
                    text = stringResource(labelRes),
                    selected = currentAnswer == index,
                    onClick = { onUpdate { copy(emaAnswers = listOf(index)) } },
                )
            }
        }
    }
}
