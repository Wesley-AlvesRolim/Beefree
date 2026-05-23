package com.wesley.beefree.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R
import com.wesley.beefree.domain.entities.CoreValueType
import com.wesley.beefree.domain.onboarding.OnboardingAnswers
import com.wesley.beefree.ui.components.OnboardingLayout
import com.wesley.beefree.ui.components.OnboardingNavigationRow
import com.wesley.beefree.ui.components.OnboardingTitle
import com.wesley.beefree.ui.components.designsystem.*

private const val MAX_CORE_VALUES = 3

private data class CoreValueOption(
    val type: CoreValueType,
    val labelRes: Int,
    val icon: ImageVector,
)

private val coreValueOptions =
    listOf(
        CoreValueOption(CoreValueType.FAMILY, R.string.onboarding_core_value_family, Icons.Filled.Diversity3),
        CoreValueOption(CoreValueType.FAITH, R.string.onboarding_core_value_faith, Icons.Filled.AutoAwesome),
        CoreValueOption(CoreValueType.HONESTY, R.string.onboarding_core_value_honesty, Icons.Filled.Verified),
        CoreValueOption(CoreValueType.HEALTH, R.string.onboarding_core_value_health, Icons.Filled.Favorite),
        CoreValueOption(CoreValueType.RELATIONSHIPS, R.string.onboarding_core_value_relationships, Icons.Filled.Group),
        CoreValueOption(CoreValueType.GROWTH, R.string.onboarding_core_value_growth, Icons.Filled.TrendingUp),
        CoreValueOption(CoreValueType.WORK, R.string.onboarding_core_value_work, Icons.Filled.Work),
        CoreValueOption(CoreValueType.FREEDOM, R.string.onboarding_core_value_freedom, Icons.Filled.Air),
        CoreValueOption(CoreValueType.PRESENCE, R.string.onboarding_core_value_presence, Icons.Filled.Visibility),
    )

@Composable
fun OnboardingCoreValuesScreen(
    answers: OnboardingAnswers,
    onUpdate: (OnboardingAnswers.() -> OnboardingAnswers) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    OnboardingLayout(
        onBack = onBack,
        sectionTitle = stringResource(R.string.onboarding_section_sua_direcao),
        chapterNumber = 5,
        bottomBar = {
            OnboardingNavigationRow(
                onNext = onNext,
                nextEnabled = answers.coreValues.size == MAX_CORE_VALUES,
            )
        },
    ) {
        BeeMascotSpeech(
            speechText = stringResource(R.string.onboarding_core_values_mascot_speech),
            tone = BeeMascotSpeechTone.Primary,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.M))
        OnboardingTitle(stringResource(R.string.onboarding_core_values_title))
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeBodyMedium(
            text = stringResource(R.string.onboarding_core_values_subtitle),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.L))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(BeeSpacing.XXL * 7),
            userScrollEnabled = false,
        ) {
            items(coreValueOptions) { option ->
                val label = stringResource(option.labelRes)
                val isSelected = option.type.name in answers.coreValues
                val atLimit = answers.coreValues.size >= MAX_CORE_VALUES
                CoreValueTile(
                    label = label,
                    icon = option.icon,
                    isSelected = isSelected,
                    onClick = {
                        if (isSelected || !atLimit) {
                            onUpdate {
                                val updated = if (isSelected) coreValues - option.type.name else coreValues + option.type.name
                                copy(coreValues = updated)
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun CoreValueTile(
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
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(BeeSpacing.L),
            )
            Spacer(modifier = Modifier.height(BeeSpacing.XXL))
        },
    )
}
