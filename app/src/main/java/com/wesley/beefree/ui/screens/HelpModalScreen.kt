package com.wesley.beefree.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.ui.theme.BeeFreeTheme

@Composable
fun HelpModalScreen(
    coreValues: List<String> = listOf("Família"),
    supportContactName: String = "John Doe",
    onDismiss: () -> Unit = {},
    onRideUrge: () -> Unit = {},
    onChallengeThought: () -> Unit = {},
    onSeeValues: () -> Unit = {},
    onReachAnchor: () -> Unit = {},
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)),
    ) {
        Box(Modifier.fillMaxSize().clickable { onDismiss() })
    }
}

@Preview(showBackground = true)
@Composable
private fun HelpModalScreenPreview() {
    BeeFreeTheme { HelpModalScreen() }
}
