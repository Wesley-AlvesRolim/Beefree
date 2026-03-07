package com.wesley.beefreepoc.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wesley.beefreepoc.ui.components.AccessibilitySettingsUI
import com.wesley.beefreepoc.ui.components.OverlaySettingsUI
import com.wesley.beefreepoc.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val viewModel: SettingsViewModel =
        viewModel {
            SettingsViewModel(context)
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
    ) {
        AccessibilitySettingsUI(viewModel)
        OverlaySettingsUI(viewModel)
    }
}
