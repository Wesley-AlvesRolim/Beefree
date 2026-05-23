package com.wesley.beefree.ui.components.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

private val BeeSelectableOptionShape = RoundedCornerShape(BeeSpacing.M)

enum class BeeSelectableOptionDirection {
    Row,
    Column,
}

enum class BeeSelectableOptionTextVariant {
    Large,
    Small,
}

@Composable
fun BeeSelectableOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    direction: BeeSelectableOptionDirection = BeeSelectableOptionDirection.Row,
    textVariant: BeeSelectableOptionTextVariant = BeeSelectableOptionTextVariant.Large,
    textAlign: TextAlign? = null,
    textContent: @Composable ColumnScope.() -> Unit = {
        BeeSelectableOptionDefaultTextContent(
            text = text,
            subtitle = subtitle,
            isSelected = isSelected,
            textVariant = textVariant,
            textAlign = textAlign,
            contentColor = MaterialTheme.colorScheme.onSurface,
            selectedContentColor = MaterialTheme.colorScheme.onSurface,
        )
    },
    rowArrangement: Arrangement.Horizontal = Arrangement.spacedBy(BeeSpacing.M),
    rowAlignment: Alignment.Vertical = Alignment.CenterVertically,
    columnArrangement: Arrangement.Vertical = Arrangement.spacedBy(BeeSpacing.S),
    columnAlignment: Alignment.Horizontal = Alignment.Start,
    indicator: @Composable BoxScope.(Boolean) -> Unit = { selected -> BeeRadioIndicator(selected) },
) {
    BeeSelectableOptionCard(
        isSelected = isSelected,
        onClick = onClick,
        modifier = modifier,
        direction = direction,
        textVariant = textVariant,
        textContent = textContent,
        rowArrangement = rowArrangement,
        rowAlignment = rowAlignment,
        columnArrangement = columnArrangement,
        columnAlignment = columnAlignment,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        selectedBorder = BorderStroke(BeeSpacing.XS, MaterialTheme.colorScheme.primary),
        indicator = indicator,
    )
}

@Composable
fun BeeMultiSelectOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    direction: BeeSelectableOptionDirection = BeeSelectableOptionDirection.Row,
    textVariant: BeeSelectableOptionTextVariant = BeeSelectableOptionTextVariant.Large,
    textAlign: TextAlign? = null,
    textContent: @Composable ColumnScope.() -> Unit = {
        BeeSelectableOptionDefaultTextContent(
            text = text,
            subtitle = description,
            isSelected = isSelected,
            textVariant = textVariant,
            textAlign = textAlign,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
            selectedContentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
            subtitleColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    },
    rowArrangement: Arrangement.Horizontal = Arrangement.spacedBy(BeeSpacing.M),
    rowAlignment: Alignment.Vertical = Alignment.CenterVertically,
    columnArrangement: Arrangement.Vertical = Arrangement.spacedBy(BeeSpacing.S),
    columnAlignment: Alignment.Horizontal = Alignment.Start,
    indicator: @Composable BoxScope.(Boolean) -> Unit = { selected -> BeeCheckboxIndicator(selected) },
) {
    BeeSelectableOptionCard(
        isSelected = isSelected,
        onClick = onClick,
        modifier = modifier,
        direction = direction,
        textVariant = textVariant,
        textContent = textContent,
        rowArrangement = rowArrangement,
        rowAlignment = rowAlignment,
        columnArrangement = columnArrangement,
        columnAlignment = columnAlignment,
        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLowest,
        selectedBorder = BorderStroke(BeeSpacing.XS, MaterialTheme.colorScheme.primary),
        indicator = indicator,
    )
}

@Composable
private fun BeeSelectableOptionCard(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    direction: BeeSelectableOptionDirection,
    textVariant: BeeSelectableOptionTextVariant,
    textContent: @Composable ColumnScope.() -> Unit,
    rowArrangement: Arrangement.Horizontal,
    rowAlignment: Alignment.Vertical,
    columnArrangement: Arrangement.Vertical,
    columnAlignment: Alignment.Horizontal,
    containerColor: Color,
    selectedBorder: BorderStroke?,
    indicator: @Composable BoxScope.(Boolean) -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = BeeSelectableOptionShape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) BeeSpacing.NONE else BeeSpacing.XXS),
        border = if (isSelected) selectedBorder else null,
    ) {
        when (direction) {
            BeeSelectableOptionDirection.Row -> {
                Row(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = BeeSpacing.M, vertical = BeeSpacing.M),
                    verticalAlignment = rowAlignment,
                    horizontalArrangement = rowArrangement,
                ) {
                    Box(
                        modifier = Modifier.size(BeeSpacing.L),
                        contentAlignment = Alignment.Center,
                    ) {
                        indicator(isSelected)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        textContent()
                    }
                }
            }

            BeeSelectableOptionDirection.Column -> {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = BeeSpacing.M, vertical = BeeSpacing.M),
                    horizontalAlignment = columnAlignment,
                    verticalArrangement = columnArrangement,
                ) {
                    Box(
                        modifier = Modifier.size(BeeSpacing.L),
                        contentAlignment = Alignment.Center,
                    ) {
                        indicator(isSelected)
                    }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        textContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun BeeSelectableOptionDefaultTextContent(
    text: String,
    subtitle: String?,
    isSelected: Boolean,
    textVariant: BeeSelectableOptionTextVariant,
    textAlign: TextAlign?,
    contentColor: Color,
    selectedContentColor: Color,
    subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    when (textVariant) {
        BeeSelectableOptionTextVariant.Large -> {
            BeeLabelLarge(
                text = text,
                color = if (isSelected) selectedContentColor else contentColor,
                modifier = if (textAlign != null) Modifier.fillMaxWidth() else Modifier,
                textAlign = textAlign,
            )
        }

        BeeSelectableOptionTextVariant.Small -> {
            BeeLabelSmall(
                text = text,
                color = if (isSelected) selectedContentColor else contentColor,
                modifier = if (textAlign != null) Modifier.fillMaxWidth() else Modifier,
                textAlign = textAlign,
            )
        }
    }
    if (subtitle != null) {
        Spacer(modifier = Modifier.height(BeeSpacing.XS))
        BeeBodySmall(
            text = subtitle,
            color = if (isSelected) selectedContentColor else subtitleColor,
            modifier = if (textAlign != null) Modifier.fillMaxWidth() else Modifier,
            textAlign = textAlign,
        )
    }
}

@Composable
private fun BeeRadioIndicator(selected: Boolean) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    Box(
        modifier =
            Modifier
                .size(BeeSpacing.L)
                .border(BeeSpacing.XXS, borderColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier =
                    Modifier
                        .size(BeeSpacing.S)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
            )
        }
    }
}

@Composable
private fun BeeCheckboxIndicator(selected: Boolean) {
    if (selected) {
        Box(
            modifier =
                Modifier
                    .size(BeeSpacing.L)
                    .clip(RoundedCornerShape(BeeSpacing.XS))
                    .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(BeeSpacing.M),
            )
        }
    } else {
        Box(
            modifier =
                Modifier
                    .size(BeeSpacing.L)
                    .border(BeeSpacing.XXS, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(BeeSpacing.XS)),
        )
    }
}
