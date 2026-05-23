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
import com.wesley.beefree.domain.entities.CoreValueType
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
        CoreValueType.FAMILY to R.string.onboarding_core_value_family,
        CoreValueType.FAITH to R.string.onboarding_core_value_faith,
        CoreValueType.HONESTY to R.string.onboarding_core_value_honesty,
        CoreValueType.HEALTH to R.string.onboarding_core_value_health,
        CoreValueType.RELATIONSHIPS to R.string.onboarding_core_value_relationships,
        CoreValueType.GROWTH to R.string.onboarding_core_value_growth,
        CoreValueType.WORK to R.string.onboarding_core_value_work,
        CoreValueType.COMMUNITY to R.string.onboarding_core_value_community,
        CoreValueType.LOVE to R.string.onboarding_core_value_love,
        CoreValueType.FREEDOM to R.string.onboarding_core_value_freedom,
    )

@Composable
fun OnboardingCoreValuesScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val coreValueEntries = coreValueResources.map { (type, res) -> type to stringResource(res) }

    OnboardingLayout(
        onBack = onBack,
        bottomBar = {
            OnboardingNavigationRow(
                onNext = onNext,
                nextEnabled = answers.coreValues.size == MAX_CORE_VALUES,
            )
        },
    ) {
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
            coreValueEntries.forEach { (type, label) ->
                val isSelected = type.name in answers.coreValues
                val atLimit = answers.coreValues.size >= MAX_CORE_VALUES
                OnboardingMultiSelectOption(
                    text = label,
                    isSelected = isSelected,
                    onClick = {
                        if (isSelected || !atLimit) {
                            onUpdate {
                                val updated = if (isSelected) coreValues - type.name else coreValues + type.name
                                copy(coreValues = updated)
                            }
                        }
                    },
                )
            }
        }
    }
}
