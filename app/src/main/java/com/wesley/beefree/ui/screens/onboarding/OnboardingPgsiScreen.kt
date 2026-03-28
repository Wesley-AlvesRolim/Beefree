package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingMascot
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingSelectableOption
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.*

private val pgsiQuestions =
    listOf(
        R.string.onboarding_pgsi_q1,
        R.string.onboarding_pgsi_q2,
        R.string.onboarding_pgsi_q3,
        R.string.onboarding_pgsi_q4,
        R.string.onboarding_pgsi_q5,
        R.string.onboarding_pgsi_q6,
        R.string.onboarding_pgsi_q7,
        R.string.onboarding_pgsi_q8,
        R.string.onboarding_pgsi_q9,
    )

private val scaleLabels =
    listOf(
        R.string.onboarding_scale_never,
        R.string.onboarding_frequency_3,
        R.string.onboarding_frequency_4,
        R.string.onboarding_scale_almost_always,
    )

@Composable
fun OnboardingPgsiScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    var questionIndex by remember { mutableIntStateOf(0) }
    val currentAnswer = answers.pgsiAnswers.getOrNull(questionIndex)
    val isLastQuestion = questionIndex == pgsiQuestions.lastIndex

    OnboardingLayout(onBack = { if (questionIndex > 0) questionIndex-- else onBack() }) {
        OnboardingMascot()
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_pgsi_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        BeeBodyLarge(
            text = stringResource(pgsiQuestions[questionIndex]),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        Column(
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
        ) {
            scaleLabels.forEachIndexed { index, labelRes ->
                val value = index
                OnboardingSelectableOption(
                    text = "$value – ${stringResource(labelRes)}",
                    isSelected = currentAnswer == value,
                    onClick = {
                        val updated = answers.pgsiAnswers.toMutableList()
                        while (updated.size <= questionIndex) updated.add(0)
                        updated[questionIndex] = value
                        onUpdate { copy(pgsiAnswers = updated) }
                    },
                )
            }
        }
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        OnboardingNavigationRow(
            onNext = { if (isLastQuestion) onNext() else questionIndex++ },
            nextEnabled = currentAnswer != null,
        )
    }
}
