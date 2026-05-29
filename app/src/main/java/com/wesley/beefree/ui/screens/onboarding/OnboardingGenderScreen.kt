package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingNavigationRow
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
            Triple(
                stringResource(R.string.onboarding_gender_male),
                Icons.Filled.Male,
                stringResource(R.string.onboarding_gender_male),
            ),
            Triple(
                stringResource(R.string.onboarding_gender_female),
                Icons.Filled.Female,
                stringResource(R.string.onboarding_gender_female),
            ),
            Triple(
                stringResource(R.string.onboarding_gender_prefer_not),
                Icons.Filled.Remove,
                stringResource(R.string.onboarding_gender_prefer_not),
            ),
        )

    OnboardingLayout(
        onBack = onBack,
        sectionTitle = stringResource(R.string.onboarding_section_sobre_voce),
        chapterNumber = 1,
        bottomBar = {
            OnboardingNavigationRow(
                onNext = onNext,
                nextEnabled = answers.gender.isNotBlank(),
            )
        },
    ) {
        BeeMascotSpeech(
            speechText = stringResource(R.string.onboarding_gender_mascot_speech),
            tone = BeeMascotSpeechTone.Primary,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_gender_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.M)) {
            genderOptions.forEach { (label, icon, value) ->
                GenderTile(
                    label = label,
                    icon = icon,
                    isSelected = answers.gender == value,
                    onClick = {
                        onUpdate {
                            copy(gender = value)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun GenderTile(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    BeeSelectableOption(
        text = label,
        indicator = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(BeeSpacing.L),
            )
            Spacer(modifier = Modifier.height(BeeSpacing.XXL))
        },
        isSelected = isSelected,
        onClick = onClick,
    )
}
