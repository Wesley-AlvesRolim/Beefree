package com.wesley.beefree.ui.screens

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodyLarge
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeButtonGhost
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val isAccessibilityEnabled by viewModel.isAccessibilityServiceEnabled.collectAsState()
    val isAccessibilityStarted by viewModel.isAccessibilityServiceStarted.collectAsState()
    val isOverlayPermissionEnabled by viewModel.isOverlayPermissionEnabled.collectAsState()
    val isAdultMonitoringEnabled by viewModel.isAdultMonitoringEnabled.collectAsState()
    val isBetsMonitoringEnabled by viewModel.isBetsMonitoringEnabled.collectAsState()
    val errorText by viewModel.errorMessage.collectAsState(null)

    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    viewModel.updateStatuses()
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    errorText?.let { uiText ->
        AlertDialog(
            onDismissRequest = { viewModel.resetError() },
            confirmButton = {
                BeeButtonGhost(onClick = { viewModel.resetError() }) {
                    BeeLabelMedium(stringResource(android.R.string.ok))
                }
            },
            text = {
                BeeBodyMedium(uiText)
            },
        )
    }

    SettingsScreenContent(
        isAccessibilityEnabled = isAccessibilityEnabled,
        isAccessibilityStarted = isAccessibilityStarted,
        isOverlayPermissionEnabled = isOverlayPermissionEnabled,
        isAdultMonitoringEnabled = isAdultMonitoringEnabled,
        isBetsMonitoringEnabled = isBetsMonitoringEnabled,
        onToggleAccessibility = { viewModel.toggleAccessibilityService() },
        onOpenAccessibilitySettings = { viewModel.openAccessibilitySettings() },
        onOpenOverlaySettings = { viewModel.openOverlaySettings() },
        onToggleAdultMonitoring = { viewModel.toggleAdultMonitoring() },
        onToggleBetsMonitoring = { viewModel.toggleBetsMonitoring() },
        onExportData = { viewModel.exportData() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    isAccessibilityEnabled: Boolean,
    isAccessibilityStarted: Boolean,
    isOverlayPermissionEnabled: Boolean,
    isAdultMonitoringEnabled: Boolean,
    isBetsMonitoringEnabled: Boolean,
    onToggleAccessibility: () -> Unit,
    onOpenAccessibilitySettings: () -> Unit,
    onOpenOverlaySettings: () -> Unit,
    onToggleAdultMonitoring: () -> Unit,
    onToggleBetsMonitoring: () -> Unit,
    onExportData: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors =
                    TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceContainerLowest),
                title = {
                    BeeHeadlineMedium(stringResource(R.string.settings_title))
                },
            )
        },
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(BeeSpacing.M),
            verticalArrangement = Arrangement.spacedBy(BeeSpacing.L),
        ) {
            BeeCardSection {
                Column(Modifier.padding(BeeSpacing.M)) {
                    BeeHeadlineSmall(
                        stringResource(R.string.settings_monitoring_section_title),
                    )
                    Spacer(Modifier.height(BeeSpacing.M))
                    SwitchRow(
                        stringResource(R.string.settings_adult_content_label),
                        isAdultMonitoringEnabled,
                    ) { onToggleAdultMonitoring() }
                    SwitchRow(
                        stringResource(R.string.settings_bets_label),
                        isBetsMonitoringEnabled,
                    ) { onToggleBetsMonitoring() }
                }
            }

            Section(stringResource(R.string.settings_customization_section_title)) {
                ListItem(
                    headlineContent = {
                        BeeBodyLarge(stringResource(R.string.settings_block_message_title))
                    },
                    supportingContent = {
                        BeeBodySmall(stringResource(R.string.settings_block_message_subtitle))
                    },
                    leadingContent = { Icon(Icons.Default.Edit, null) },
                    modifier =
                        Modifier.background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(BeeSpacing.M),
                        ),
                )
            }

            Section(
                title = stringResource(R.string.settings_support_network_section_title),
                action = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.AddCircle,
                            stringResource(R.string.settings_add_contact),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            ) {
                ListItem(
                    headlineContent = { BeeBodyLarge("João Primo") },
                    supportingContent = { BeeBodySmall("(85) 00000-0000") },
                    leadingContent = { Icon(Icons.Default.Person, null) },
                    trailingContent = {
                        Icon(
                            Icons.Default.Delete,
                            stringResource(R.string.settings_remove_contact),
                        )
                    },
                    modifier =
                        Modifier.background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(BeeSpacing.M),
                        ),
                )
            }

            Section(stringResource(R.string.settings_privacy_section_title)) {
                BeeButtonPrimary(
                    onClick = onExportData,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Default.Share, null, Modifier.padding(end = BeeSpacing.S))
                    BeeLabelLarge(
                        stringResource(R.string.settings_export_data),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }

            Section(stringResource(R.string.settings_permissions_section_title)) {
                BeeCardSection {
                    Column(
                        modifier =
                            Modifier
                                .padding(BeeSpacing.M),
                    ) {
                        SwitchRow(
                            stringResource(R.string.settings_screen_reader_label),
                            isAccessibilityEnabled && isAccessibilityStarted,
                        ) {
                            if (isAccessibilityEnabled) {
                                onToggleAccessibility()
                            } else {
                                onOpenAccessibilitySettings()
                            }
                        }
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            SwitchRow(
                                stringResource(R.string.settings_screen_blocker_label),
                                isOverlayPermissionEnabled,
                            ) { onOpenOverlaySettings() }
                        }
                    }
                }
                Spacer(Modifier.height(BeeSpacing.S))
                BeeCardSection {
                    Column(Modifier.padding(BeeSpacing.M)) {
                        BeeLabelLarge(stringResource(R.string.settings_banking_warning_title))
                        Spacer(Modifier.height(BeeSpacing.S))
                        BeeBodySmall(stringResource(R.string.settings_banking_warning_body))
                    }
                }
            }
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        BeeBodyLarge(
            label,
            Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Switch(checked, onCheckedChange)
    }
}

@Composable
private fun Section(
    title: String,
    action: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = BeeSpacing.S),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BeeHeadlineSmall(
                text = title,
            )
            action?.invoke()
        }
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    BeeFreeTheme {
        SettingsScreenContent(
            isAccessibilityEnabled = true,
            isAccessibilityStarted = false,
            isOverlayPermissionEnabled = true,
            isAdultMonitoringEnabled = true,
            isBetsMonitoringEnabled = false,
            onToggleAccessibility = {},
            onOpenAccessibilitySettings = {},
            onOpenOverlaySettings = {},
            onToggleAdultMonitoring = {},
            onToggleBetsMonitoring = {},
            onExportData = {},
        )
    }
}
