package com.wesley.beefree.ui.components.checkin.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wesley.beefree.R
import com.wesley.beefree.domain.checkin.ActivityOption
import com.wesley.beefree.domain.checkin.ActivityType
import com.wesley.beefree.domain.checkin.TherapeuticActivityStep
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelSmall
import com.wesley.beefree.ui.components.designsystem.BeeSelectableOption
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

@Composable
fun TherapeuticActivityStepContent(
    spec: TherapeuticActivityStep,
    selectedActivity: ActivityType?,
    onSelectActivity: (ActivityType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedType = selectedActivity ?: spec.activityOptions.firstOrNull()?.type
    val (featuredOptions, alternativeOptions) =
        spec.activityOptions.partition { it.type == selectedType }
    val featuredOption = featuredOptions.firstOrNull()

    Column(modifier = modifier.fillMaxWidth()) {
        BeeCardSection {
            Column(Modifier.padding(BeeSpacing.M)) {
                BeeHeadlineSmall(stringByName(spec.titleKey))
                Spacer(modifier = Modifier.height(BeeSpacing.S))
                BeeBodyMedium(
                    text = stringByName(spec.subtitleKey),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(BeeSpacing.M))
                if (featuredOption != null) {
                    TherapeuticActivityOptionCard(option = featuredOption, isSelected = true, onClick = { })
                    Spacer(modifier = Modifier.height(BeeSpacing.M))
                }
            }
        }

        if (alternativeOptions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(BeeSpacing.L))
            BeeCardSection {
                Column(Modifier.padding(BeeSpacing.M)) {
                    BeeLabelSmall(
                        text = stringResource(R.string.daily_checkin_activity_swap_label),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(BeeSpacing.S))
                    Column(verticalArrangement = Arrangement.spacedBy(BeeSpacing.S)) {
                        alternativeOptions.forEach { option ->
                            TherapeuticActivityOptionCard(
                                option = option,
                                isSelected = false,
                                onClick = { onSelectActivity(option.type) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TherapeuticActivityOptionCard(
    option: ActivityOption,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    BeeSelectableOption(
        text = stringByName(option.titleKey),
        subtitle = stringByName(option.descriptionKey),
        isSelected = isSelected,
        onClick = onClick,
        indicator = {
            Icon(
                imageVector = option.type.icon,
                contentDescription = stringByName(option.titleKey),
                modifier = Modifier.size(BeeSpacing.L),
            )
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

private val ActivityType.icon
    get() =
        when (this) {
            ActivityType.MINDFULNESS -> Icons.Filled.SelfImprovement
            ActivityType.PROFILE_EXERCISE -> Icons.Filled.FitnessCenter
            ActivityType.VIDEO -> Icons.Filled.OndemandVideo
        }
