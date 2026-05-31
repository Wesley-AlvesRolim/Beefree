package com.wesley.beefree.ui.screens.settings

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.wesley.beefree.BuildConfig
import com.wesley.beefree.R
import com.wesley.beefree.ui.components.designsystem.BeeBodyLarge
import com.wesley.beefree.ui.components.designsystem.BeeBodyMedium
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeButtonGhost
import com.wesley.beefree.ui.components.designsystem.BeeButtonPrimary
import com.wesley.beefree.ui.components.designsystem.BeeCardInteractive
import com.wesley.beefree.ui.components.designsystem.BeeCardSection
import com.wesley.beefree.ui.components.designsystem.BeeChipTag
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineMedium
import com.wesley.beefree.ui.components.designsystem.BeeHeadlineSmall
import com.wesley.beefree.ui.components.designsystem.BeeLabelLarge
import com.wesley.beefree.ui.components.designsystem.BeeLabelMedium
import com.wesley.beefree.ui.components.designsystem.BeeSpacing
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateToAbout: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
) {
    val isAdultMonitoringEnabled by viewModel.isAdultMonitoringEnabled.collectAsState()
    val isBetsMonitoringEnabled by viewModel.isBetsMonitoringEnabled.collectAsState()
    val errorText by viewModel.errorMessage.collectAsState(null)

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
        isAdultMonitoringEnabled = isAdultMonitoringEnabled,
        isBetsMonitoringEnabled = isBetsMonitoringEnabled,
        onToggleAdultMonitoring = { viewModel.toggleAdultMonitoring() },
        onToggleBetsMonitoring = { viewModel.toggleBetsMonitoring() },
        onExportData = { viewModel.exportData() },
        onNavigateToAbout = onNavigateToAbout,
        onNavigateToTerms = onNavigateToTerms,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    isAdultMonitoringEnabled: Boolean,
    isBetsMonitoringEnabled: Boolean,
    onToggleAdultMonitoring: () -> Unit,
    onToggleBetsMonitoring: () -> Unit,
    onExportData: () -> Unit,
    onNavigateToAbout: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
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

            Section(stringResource(R.string.settings_support_network_section_title)) {
                BeeCardSection(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(BeeSpacing.M),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
                    ) {
                        Column(Modifier.weight(1f)) {
                            BeeBodyLarge(stringResource(R.string.settings_manage_contacts))
                        }
                        BeeChipTag(
                            label = stringResource(R.string.settings_manage_contacts_coming_soon),
                        )
                    }
                }
            }

            Section(stringResource(R.string.settings_data_management_section_title)) {
                BeeCardSection(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(BeeSpacing.M),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(BeeSpacing.M),
                    ) {
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
                        BeeBodySmall(
                            stringResource(R.string.settings_export_data_subtitle),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Section(stringResource(R.string.settings_support_section_title)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
                ) {
                    NavigationRow(
                        label = stringResource(R.string.settings_about),
                        icon = Icons.Default.Info,
                        onClick = onNavigateToAbout,
                    )
                    NavigationRow(
                        label = stringResource(R.string.settings_terms_of_service),
                        icon = Icons.Default.Description,
                        onClick = onNavigateToTerms,
                    )
                }
            }

            Spacer(Modifier.height(BeeSpacing.S))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(BeeSpacing.S),
            ) {
                BeeBodySmall(
                    stringResource(R.string.settings_version, BuildConfig.VERSION_NAME),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                BeeBodySmall(
                    stringResource(R.string.settings_footer_tagline),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun NavigationRow(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    BeeCardInteractive(
        modifier =
            Modifier
                .fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(BeeSpacing.M),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BeeSpacing.M),
        ) {
            Icon(icon, null)
            BeeBodyLarge(
                label,
                Modifier.weight(1f),
            )
            Icon(Icons.Default.ChevronRight, null)
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
            isAdultMonitoringEnabled = true,
            isBetsMonitoringEnabled = false,
            onToggleAdultMonitoring = {},
            onToggleBetsMonitoring = {},
            onExportData = {},
            onNavigateToAbout = {},
            onNavigateToTerms = {},
        )
    }
}
