package com.wesley.beefree.ui.components.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

private val BeeButtonShape: Shape = RoundedCornerShape(12.dp)
private val BeeButtonHeight = 56.dp

@Composable
fun BeeButtonPrimary(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(BeeButtonHeight),
        enabled = enabled,
        shape = BeeButtonShape,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        content = content,
    )
}

@Composable
fun BeeButtonSecondary(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(BeeButtonHeight),
        enabled = enabled,
        shape = BeeButtonShape,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
        content = content,
    )
}

@Composable
fun BeeButtonOutlined(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(BeeButtonHeight),
        enabled = enabled,
        shape = BeeButtonShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors =
            ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        content = content,
    )
}

@Composable
fun BeeButtonGhost(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(BeeButtonHeight),
        enabled = enabled,
        shape = BeeButtonShape,
        colors =
            ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
            ),
        content = content,
    )
}

@Composable
fun BeeFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(56.dp),
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        content = content,
    )
}
