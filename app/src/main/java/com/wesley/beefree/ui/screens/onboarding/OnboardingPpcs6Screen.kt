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
import com.wesley.beefree.ui.components.OnboardingLikertOption
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.*

private val ppcs6Questions =
    listOf(
        R.string.onboarding_ppcs6_q1,
        R.string.onboarding_ppcs6_q2,
        R.string.onboarding_ppcs6_q3,
        R.string.onboarding_ppcs6_q4,
        R.string.onboarding_ppcs6_q5,
        R.string.onboarding_ppcs6_q6,
    )

private val scaleLabels =
    listOf(
        R.string.onboarding_frequency_1,
        R.string.onboarding_frequency_2,
        R.string.onboarding_frequency_3,
        R.string.onboarding_frequency_4,
        R.string.onboarding_frequency_5,
        R.string.onboarding_frequency_6,
        R.string.onboarding_frequency_7,
    )

@Composable
fun OnboardingPpcs6Screen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    var questionIndex by remember { mutableIntStateOf(0) }
    val currentAnswer = answers.ppcs6Answers.getOrNull(questionIndex)
    val isLastQuestion = questionIndex == ppcs6Questions.lastIndex

    OnboardingLayout(
        onBack = { if (questionIndex > 0) questionIndex-- else onBack() },
        sectionTitle = stringResource(R.string.onboarding_section_seu_padrao),
        chapterNumber = 2,
        bottomBar = {
            OnboardingNavigationRow(
                onNext = { if (isLastQuestion) onNext() else questionIndex++ },
                nextEnabled = currentAnswer != null && currentAnswer > 0,
            )
        },
    ) {
        BeeMascotSpeech(
            speechText = stringResource(R.string.onboarding_ppcs6_mascot_speech),
            tone = BeeMascotSpeechTone.Primary,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_ppcs6_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        BeeBodyLarge(
            text = stringResource(ppcs6Questions[questionIndex]),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
            scaleLabels.forEachIndexed { index, labelRes ->
                val value = index + 1
                OnboardingLikertOption(
                    number = value,
                    text = stringResource(labelRes),
                    selected = currentAnswer == value,
                    onClick = {
                        val updated = answers.ppcs6Answers.toMutableList()
                        while (updated.size <= questionIndex) updated.add(0)
                        updated[questionIndex] = value
                        onUpdate { copy(ppcs6Answers = updated) }
                    },
                )
            }
        }
    }
}
