package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.*

private data class SymptomOption(
    val labelRes: Int,
    val icon: ImageVector,
)

private val symptomOptions =
    listOf(
        SymptomOption(R.string.onboarding_symptom_anxiety, Icons.Filled.Bolt),
        SymptomOption(R.string.onboarding_symptom_depression, Icons.Filled.WaterDrop),
        SymptomOption(R.string.onboarding_symptom_insomnia, Icons.Filled.Bedtime),
        SymptomOption(R.string.onboarding_symptom_low_focus, Icons.Outlined.RemoveRedEye),
        SymptomOption(
            R.string.onboarding_symptom_low_self_esteem,
            Icons.Filled.SentimentDissatisfied,
        ),
        SymptomOption(R.string.onboarding_symptom_social_isolation, Icons.Filled.PersonOff),
        SymptomOption(R.string.onboarding_symptom_impulsivity, Icons.Filled.FlashOn),
        SymptomOption(R.string.onboarding_symptom_irritability, Icons.Filled.Block),
    )

@Composable
fun OnboardingSymptomsScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    OnboardingLayout(
        onBack = onBack,
        sectionTitle = stringResource(R.string.onboarding_section_sua_perspectiva),
        chapterNumber = 3,
        bottomBar = { OnboardingNavigationRow(onNext = onNext) },
    ) {
        BeeMascotSpeech(
            speechText = stringResource(R.string.onboarding_symptoms_mascot_speech),
            tone = BeeMascotSpeechTone.Primary,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_symptoms_title))
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement =
                Arrangement
                    .spacedBy(BeeSpacing.S),
            verticalArrangement =
                Arrangement
                    .spacedBy(BeeSpacing.S),
            contentPadding = PaddingValues(bottom = BeeSpacing.S),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(BeeSpacing.XXL * 9),
        ) {
            items(symptomOptions) { option ->
                val label = stringResource(option.labelRes)
                val isSelected = label in answers.symptoms
                SymptomTile(
                    label = label,
                    icon = option.icon,
                    isSelected = isSelected,
                    onClick = {
                        onUpdate {
                            val updated = if (isSelected) symptoms - label else symptoms + label
                            copy(symptoms = updated)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun SymptomTile(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    BeeMultiSelectOption(
        text = label,
        isSelected = isSelected,
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(BeeSpacing.XXL * 2),
        direction = BeeSelectableOptionDirection.Column,
        textVariant = BeeSelectableOptionTextVariant.Small,
        textAlign = TextAlign.Center,
        columnArrangement = Arrangement.Center,
        columnAlignment = Alignment.CenterHorizontally,
        indicator = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(BeeSpacing.L),
            )
            Spacer(modifier = Modifier.height(BeeSpacing.XXL))
        },
    )
}
