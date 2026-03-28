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

private val hobbyResources =
    listOf(
        R.string.onboarding_hobby_exercise,
        R.string.onboarding_hobby_reading,
        R.string.onboarding_hobby_music,
        R.string.onboarding_hobby_meditation,
        R.string.onboarding_hobby_cooking,
        R.string.onboarding_hobby_hiking,
        R.string.onboarding_hobby_photography,
        R.string.onboarding_hobby_art,
        R.string.onboarding_hobby_team_sports,
        R.string.onboarding_hobby_board_games,
    )

@Composable
fun OnboardingHobbiesScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val hobbyLabels = hobbyResources.map { stringResource(it) }

    OnboardingLayout(onBack = onBack) {
        OnboardingMascot()
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_hobbies_title))
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        Column(
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            hobbyLabels.forEach { hobby ->
                val isSelected = hobby in answers.hobbies
                OnboardingMultiSelectOption(
                    text = hobby,
                    isSelected = isSelected,
                    onClick = {
                        onUpdate {
                            val updated = if (isSelected) hobbies - hobby else hobbies + hobby
                            copy(hobbies = updated)
                        }
                    },
                )
            }
        }
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        OnboardingNavigationRow(onNext = onNext)
    }
}
