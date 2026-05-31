package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.NeurodivergenceAnswer
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.*

@Composable
fun OnboardingNeurodivergenceScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val neurodivergenceOptions =
        listOf(
            Triple(
                stringResource(R.string.onboarding_neurodivergence_yes),
                Icons.Filled.Check,
                NeurodivergenceAnswer.YES,
            ),
            Triple(
                stringResource(R.string.onboarding_neurodivergence_no),
                Icons.Filled.Close,
                NeurodivergenceAnswer.NO,
            ),
            Triple(
                stringResource(R.string.onboarding_neurodivergence_prefer_not),
                Icons.Filled.Remove,
                NeurodivergenceAnswer.PREFER_NOT_SAY,
            ),
        )

    OnboardingLayout(
        onBack = onBack,
        sectionTitle = stringResource(R.string.onboarding_section_seu_contexto),
        chapterNumber = 4,
        bottomBar = {
            OnboardingNavigationRow(
                onNext = onNext,
                nextEnabled = answers.neurodivergenceAnswer != NeurodivergenceAnswer.NOT_ANSWERED,
            )
        },
    ) {
        BeeMascotSpeech(
            speechText = stringResource(R.string.onboarding_neurodivergence_mascot_speech),
            tone = BeeMascotSpeechTone.Primary,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_neurodivergence_title))
        Spacer(modifier = Modifier.height(BeeSpacing.XL))
        Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.M)) {
            neurodivergenceOptions.forEach { (label, icon, value) ->
                NeurodivergenceTile(
                    label = label,
                    icon = icon,
                    isSelected = answers.neurodivergenceAnswer == value,
                    onClick = { onUpdate { copy(neurodivergenceAnswer = value) } },
                )
            }
        }
    }
}

@Composable
private fun NeurodivergenceTile(
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
