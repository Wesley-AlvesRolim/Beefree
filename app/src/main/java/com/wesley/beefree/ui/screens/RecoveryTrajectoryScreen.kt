package com.wesley.beefree.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.RecoveryTrajectoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryTrajectoryScreen(
    viewModel: RecoveryTrajectoryViewModel,
    onBack: () -> Unit = {},
) {
    RecoveryTrajectoryScreenContent(onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryTrajectoryScreenContent(onBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceContainerLowest),
                title = { BeeHeadlineMedium(stringResource(R.string.recovery_trajectory_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = BeeSpacing.M),
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun RecoveryTrajectoryScreenPreview() {
    BeeFreeTheme {
        RecoveryTrajectoryScreenContent(
            onBack = {},
        )
    }
}
