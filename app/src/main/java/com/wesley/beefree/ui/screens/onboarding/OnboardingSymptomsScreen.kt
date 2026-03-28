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
import com.wesley.beefree.ui.components.OnboardingMultiSelectOption
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

private val symptomResources =
    listOf(
        R.string.onboarding_symptom_anxiety,
        R.string.onboarding_symptom_depression,
        R.string.onboarding_symptom_insomnia,
        R.string.onboarding_symptom_low_focus,
        R.string.onboarding_symptom_low_self_esteem,
        R.string.onboarding_symptom_social_isolation,
        R.string.onboarding_symptom_impulsivity,
        R.string.onboarding_symptom_irritability,
    )

@Composable
fun OnboardingSymptomsScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val symptomLabels = symptomResources.map { stringResource(it) }

    OnboardingLayout(onBack = onBack) {
        OnboardingMascot()
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_symptoms_title))
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        Column(
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            symptomLabels.forEach { symptom ->
                val isSelected = symptom in answers.symptoms
                OnboardingMultiSelectOption(
                    text = symptom,
                    isSelected = isSelected,
                    onClick = {
                        onUpdate {
                            val updated = if (isSelected) symptoms - symptom else symptoms + symptom
                            copy(symptoms = updated)
                        }
                    },
                )
            }
        }
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        OnboardingNavigationRow(onNext = onNext)
    }
}
