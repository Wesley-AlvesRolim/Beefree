package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.*

private data class FrequencyOption(
    val labelRes: Int,
    val subLabelRes: Int,
    val progress: Float,
)

private val frequencyOptions =
    listOf(
        FrequencyOption(R.string.onboarding_ppu_freq_1, R.string.onboarding_ppu_freq_sub_1, 0f),
        FrequencyOption(R.string.onboarding_ppu_freq_2, R.string.onboarding_ppu_freq_sub_2, 0.15f),
        FrequencyOption(R.string.onboarding_ppu_freq_3, R.string.onboarding_ppu_freq_sub_3, 0.30f),
        FrequencyOption(R.string.onboarding_ppu_freq_4, R.string.onboarding_ppu_freq_sub_4, 0.50f),
        FrequencyOption(R.string.onboarding_ppu_freq_5, R.string.onboarding_ppu_freq_sub_5, 1f),
    )

@Composable
fun OnboardingFrequencyScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    OnboardingLayout(
        onBack = onBack,
        sectionTitle = stringResource(R.string.onboarding_section_seu_padrao),
        chapterNumber = 2,
        bottomBar = {
            OnboardingNavigationRow(
                onNext = onNext,
                nextEnabled = answers.frequencyAnswer != 0,
            )
        },
    ) {
        BeeMascotSpeech(
            speechText = stringResource(R.string.onboarding_frequency_mascot_speech),
            tone = BeeMascotSpeechTone.Primary,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_frequency_form_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
            frequencyOptions.forEachIndexed { index, option ->
                val value = index + 1
                val isSelected = answers.frequencyAnswer == value
                Card(
                    onClick = { onUpdate { copy(frequencyAnswer = value) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainerLowest
                                },
                        ),
                    border = if (isSelected) BorderStroke(BeeSpacing.XS, MaterialTheme.colorScheme.primary) else null,
                ) {
                    Column(modifier = Modifier.padding(BeeSpacing.M)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            BeeLabelLarge(
                                text = stringResource(option.labelRes),
                                color =
                                    if (isSelected) {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                            )
                            BeeBodySmall(
                                text = stringResource(option.subLabelRes),
                                color =
                                    if (isSelected) {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                            )
                        }
                        Spacer(modifier = Modifier.height(BeeSpacing.S))
                        LinearProgressIndicator(
                            progress = { option.progress },
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(BeeSpacing.XS),
                            color =
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainerHighest
                                },
                            trackColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        )
                    }
                }
            }
        }
    }
}
