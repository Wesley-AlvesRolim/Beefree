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
import com.wesley.beefree.ui.components.designsystem.*

@Composable
fun OnboardingGenderScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val genderOptions =
        listOf(
            stringResource(R.string.onboarding_gender_male),
            stringResource(R.string.onboarding_gender_female),
            stringResource(R.string.onboarding_gender_other),
            stringResource(R.string.onboarding_gender_prefer_not),
        )

    OnboardingLayout(onBack = onBack) {
        OnboardingMascot()
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_gender_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        Column(
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
        ) {
            genderOptions.forEach { option ->
                OnboardingSelectableOption(
                    text = option,
                    isSelected = answers.gender == option,
                    onClick = { onUpdate { copy(gender = option) } },
                )
            }
        }
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        OnboardingNavigationRow(
            onNext = onNext,
            nextEnabled = answers.gender.isNotBlank(),
        )
    }
}
