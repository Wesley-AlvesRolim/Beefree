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

private val goalResources =
    listOf(
        R.string.onboarding_goal_relationships,
        R.string.onboarding_goal_focus,
        R.string.onboarding_goal_self_esteem,
        R.string.onboarding_goal_sleep,
        R.string.onboarding_goal_anxiety,
        R.string.onboarding_goal_family,
        R.string.onboarding_goal_spiritual,
        R.string.onboarding_goal_mental_health,
        R.string.onboarding_goal_social,
        R.string.onboarding_goal_career,
    )

@Composable
fun OnboardingGoalsScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val goalLabels = goalResources.map { stringResource(it) }

    OnboardingLayout(
        onBack = onBack,
        bottomBar = {
            OnboardingNavigationRow(onNext = onNext)
        },
    ) {
        OnboardingMascot()
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_goals_title))
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        Column(
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            goalLabels.forEach { goal ->
                val isSelected = goal in answers.goals
                OnboardingMultiSelectOption(
                    text = goal,
                    isSelected = isSelected,
                    onClick = {
                        onUpdate {
                            val updated = if (isSelected) goals - goal else goals + goal
                            copy(goals = updated)
                        }
                    },
                )
            }
        }
    }
}
