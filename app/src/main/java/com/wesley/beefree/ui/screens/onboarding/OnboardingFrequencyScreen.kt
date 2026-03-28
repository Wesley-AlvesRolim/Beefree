package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingMascot
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingSelectableOption
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

private val frequencyOptions =
    listOf(
        R.string.onboarding_ppu_freq_1,
        R.string.onboarding_ppu_freq_2,
        R.string.onboarding_ppu_freq_3,
        R.string.onboarding_ppu_freq_4,
        R.string.onboarding_ppu_freq_5,
    )

@Composable
fun OnboardingFrequencyScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    OnboardingLayout(onBack = onBack) {
        OnboardingMascot()
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_frequency_form_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        Column(
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            frequencyOptions.forEachIndexed { index, labelRes ->
                val value = index + 1
                OnboardingSelectableOption(
                    text = stringResource(labelRes),
                    isSelected = answers.frequencyAnswer == value,
                    onClick = { onUpdate { copy(frequencyAnswer = value) } },
                )
            }
        }
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        OnboardingNavigationRow(
            onNext = onNext,
            nextEnabled = answers.frequencyAnswer != 0,
        )
    }
}
