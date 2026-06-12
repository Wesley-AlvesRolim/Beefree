package com.wesley.beefree.ui.components.checkin.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.domain.checkin.MultiSelectStep
import com.wesley.beefree.domain.shared.OptionSpec
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeMultiSelectOption
import com.wesley.beefree.ui.components.designsystem.BeeSelectableOptionDirection
import com.wesley.beefree.ui.components.designsystem.BeeSelectableOptionTextVariant
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.components.designsystem.BeeTextArea

private fun getEmotionIcon(optionId: String): ImageVector? =
    when (optionId) {
        "anxiety" -> Icons.Filled.Warning
        "guilt" -> Icons.Filled.SentimentDissatisfied
        "shame" -> Icons.Filled.MoodBad
        "sadness" -> Icons.Filled.SentimentDissatisfied
        "anger" -> Icons.Filled.LocalFireDepartment
        "loneliness" -> Icons.Filled.PersonAdd
        "numb" -> Icons.Filled.Mood
        "other" -> Icons.Filled.QuestionMark
        else -> null
    }

@Composable
fun MultiSelectStepContent(
    spec: MultiSelectStep,
    selectedIds: List<String>,
    contextValue: String,
    onToggle: (String) -> Unit,
    onContextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasIcons = spec.options.any { it.iconId != null || getEmotionIcon(it.id) != null }

    Column(modifier = modifier.fillMaxWidth()) {
        BeeHeadlineSmall(stringByName(spec.titleKey))
        Spacer(modifier = Modifier.height(BeeSpacing.S))
        BeeBodyMedium(
            text = stringByName(spec.subtitleKey),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(BeeSpacing.L))

        if (hasIcons) {
            val chunks = spec.options.chunked(3)
            Column(
                verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                modifier = Modifier.fillMaxWidth(),
            ) {
                chunks.forEach { rowOptions ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        rowOptions.forEach { option ->
                            MultiSelectOptionTile(
                                option = option,
                                isSelected = selectedIds.contains(option.id),
                                onClick = { onToggle(option.id) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (rowOptions.size < 3) {
                            repeat(3 - rowOptions.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        } else {
            spec.options.forEach { option ->
                BeeMultiSelectOption(
                    text = stringByName(option.labelKey),
                    description = option.descriptionKey?.let { stringByName(it) },
                    isSelected = selectedIds.contains(option.id),
                    onClick = { onToggle(option.id) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(BeeSpacing.S))
            }
        }

        if (spec.withContext) {
            Spacer(modifier = Modifier.height(BeeSpacing.M))
            val hint = spec.contextHintKey?.let { stringByName(it) }.orEmpty()
            BeeTextArea(
                value = contextValue,
                onValueChange = onContextChange,
                placeholder = { BeeBodySmall(hint) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
            )
        }
    }
}

@Composable
private fun MultiSelectOptionTile(
    option: OptionSpec,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon =
        if (option.iconId != null) {
            getEmotionIcon(option.iconId)
        } else {
            getEmotionIcon(option.id)
        }

    BeeMultiSelectOption(
        text = stringByName(option.labelKey),
        isSelected = isSelected,
        onClick = onClick,
        modifier =
            modifier
                .fillMaxWidth()
                .height(BeeSpacing.XXL * 2),
        direction = BeeSelectableOptionDirection.Column,
        textVariant = BeeSelectableOptionTextVariant.Small,
        textAlign = TextAlign.Center,
        columnArrangement = Arrangement.Center,
        columnAlignment = Alignment.CenterHorizontally,
        indicator = {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringByName(option.labelKey),
                    tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.height(BeeSpacing.L),
                )
                Spacer(modifier = Modifier.height(BeeSpacing.XXL))
            }
        },
    )
}
