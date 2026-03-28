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
import com.wesley.beefree.ui.components.OnboardingMascot
import com.wesley.beefree.ui.components.OnboardingMultiSelectOption
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

private const val MAX_CORE_VALUES = 3

private val coreValueResources =
    listOf(
        R.string.onboarding_core_value_family,
        R.string.onboarding_core_value_faith,
        R.string.onboarding_core_value_honesty,
        R.string.onboarding_core_value_health,
        R.string.onboarding_core_value_relationships,
        R.string.onboarding_core_value_growth,
        R.string.onboarding_core_value_work,
        R.string.onboarding_core_value_community,
        R.string.onboarding_core_value_love,
        R.string.onboarding_core_value_freedom,
    )

@Composable
fun OnboardingCoreValuesScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val coreValueLabels = coreValueResources.map { stringResource(it) }

    OnboardingLayout(onBack = onBack) {
        OnboardingMascot()
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_core_values_title))
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeBodyMedium(
            text = stringResource(R.string.onboarding_core_values_subtitle),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        Column(
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            coreValueLabels.forEach { value ->
                val isSelected = value in answers.coreValues
                val atLimit = answers.coreValues.size >= MAX_CORE_VALUES
                OnboardingMultiSelectOption(
                    text = value,
                    isSelected = isSelected,
                    onClick = {
                        if (isSelected || !atLimit) {
                            onUpdate {
                                val updated = if (isSelected) coreValues - value else coreValues + value
                                copy(coreValues = updated)
                            }
                        }
                    },
                )
            }
        }
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        OnboardingNavigationRow(
            onNext = onNext,
            nextEnabled = answers.coreValues.size == MAX_CORE_VALUES,
        )
    }
}
