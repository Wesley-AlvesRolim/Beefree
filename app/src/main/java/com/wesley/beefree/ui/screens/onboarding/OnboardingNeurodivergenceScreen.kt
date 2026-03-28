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
fun OnboardingNeurodivergenceScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    OnboardingLayout(onBack = onBack) {
        OnboardingMascot()
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_neurodivergence_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        Column(
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
        ) {
            OnboardingSelectableOption(
                text = stringResource(R.string.onboarding_neurodivergence_yes),
                isSelected = answers.hasNeurodivergence == true,
                onClick = { onUpdate { copy(hasNeurodivergence = true) } },
            )
            OnboardingSelectableOption(
                text = stringResource(R.string.onboarding_neurodivergence_no),
                isSelected = answers.hasNeurodivergence == false,
                onClick = { onUpdate { copy(hasNeurodivergence = false) } },
            )
            OnboardingSelectableOption(
                text = stringResource(R.string.onboarding_neurodivergence_prefer_not),
                isSelected = answers.hasNeurodivergence == null && answers.gender.isNotBlank(),
                onClick = { onUpdate { copy(hasNeurodivergence = null) } },
            )
        }
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        OnboardingNavigationRow(onNext = onNext)
    }
}
