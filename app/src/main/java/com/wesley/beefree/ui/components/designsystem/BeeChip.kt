package com.wesley.beefree.ui.components.designsystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BeeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor =
        if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.secondaryContainer
        }
    val contentColor =
        if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSecondaryContainer
        }

    Surface(
        modifier = modifier.clip(CircleShape).clickable(onClick = onClick),
        shape = CircleShape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Box(
            modifier = Modifier.padding(horizontal = BeeSpacing.M, vertical = BeeSpacing.S),
            contentAlignment = Alignment.Center,
        ) {
            BeeLabelMedium(label, color = contentColor)
        }
    }
}

@Composable
fun BeeChipMood(
    emoji: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor =
        if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    val contentColor =
        if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(BeeSpacing.M),
        color = containerColor,
        contentColor = contentColor,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = BeeSpacing.S, horizontal = BeeSpacing.XS),
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(BeeSpacing.XS))
            BeeLabelSmall(
                text = label,
                color = contentColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun BeeChipTag(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Box(
            modifier = Modifier.padding(horizontal = BeeSpacing.M, vertical = BeeSpacing.S),
            contentAlignment = Alignment.Center,
        ) {
            BeeLabelMedium(label, color = contentColor)
        }
    }
}
