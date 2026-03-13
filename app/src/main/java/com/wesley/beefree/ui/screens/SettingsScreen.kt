package com.wesley.beefree.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.wesley.beefree.ui.theme.BeeFreeTheme
import com.wesley.beefree.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val isAccessibilityEnabled by viewModel.isAccessibilityServiceEnabled.collectAsState()
    val isAccessibilityStarted by viewModel.isAccessibilityServiceStarted.collectAsState()
    val isOverlayPermissionEnabled by viewModel.isOverlayPermissionEnabled.collectAsState()

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

    SettingsScreenContent(
        isAccessibilityEnabled = isAccessibilityEnabled,
        isAccessibilityStarted = isAccessibilityStarted,
        isOverlayPermissionEnabled = isOverlayPermissionEnabled,
        onUpdateStatuses = { viewModel.updateStatuses() },
        onToggleAccessibility = { viewModel.toggleAccessibilityService() },
        onOpenAccessibilitySettings = { viewModel.openAccessibilitySettings() },
        onStartOverlayService = { viewModel.startOverlayService() },
        onOpenOverlaySettings = { viewModel.openOverlaySettings() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    isAccessibilityEnabled: Boolean,
    isAccessibilityStarted: Boolean,
    isOverlayPermissionEnabled: Boolean,
    onUpdateStatuses: () -> Unit,
    onToggleAccessibility: () -> Unit,
    onOpenAccessibilitySettings: () -> Unit,
    onStartOverlayService: () -> Unit,
    onOpenOverlaySettings: () -> Unit,
) {
    var monitorAdult by remember { mutableStateOf(true) }
    var monitorBets by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações", fontWeight = FontWeight.Bold) },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Monitoramento Ativo",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                    Spacer(Modifier.height(16.dp))
                    SwitchRow(
                        "Conteúdo adulto",
                        monitorAdult,
                    ) { monitorAdult = it }
                    SwitchRow(
                        "Apostas online",
                        monitorBets,
                    ) { monitorBets = it }
                }
            }

            Section("Personalização") {
                ListItem(
                    headlineContent = { Text("Mensagem de Bloqueio") },
                    supportingContent = { Text("Edite o texto e a imagem da tela de alerta") },
                    leadingContent = { Icon(Icons.Default.Edit, null) },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)),
                )
            }

            Section(
                title = "Rede de Apoio (WhatsApp)",
                action = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.AddCircle,
                            "Adicionar",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            ) {
                ListItem(
                    headlineContent = { Text("João Primo") },
                    supportingContent = { Text("(85) 00000-0000") },
                    leadingContent = { Icon(Icons.Default.Person, null) },
                    trailingContent = { Icon(Icons.Default.Delete, "Remover") },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)),
                )
            }

            Section("Privacidade") {
                Button(
                    onClick = {},
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(Icons.Default.Share, null, Modifier.padding(end = 8.dp))
                    Text("Exportar meus dados", color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Section("Permissões") {
                Column(Modifier.padding(16.dp)) {
                    SwitchRow(
                        "Leitura de tela",
                        isAccessibilityEnabled && isAccessibilityStarted,
                    ) {
                        if (isAccessibilityEnabled) {
                            onToggleAccessibility()
                        } else {
                            onOpenAccessibilitySettings()
                        }
                    }
                    SwitchRow(
                        "Bloqueio de tela",
                        isOverlayPermissionEnabled,
                    ) { onOpenOverlaySettings() }
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
        Text(label, Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
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
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
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
            onUpdateStatuses = {},
            onToggleAccessibility = {},
            onOpenAccessibilitySettings = {},
            onStartOverlayService = {},
            onOpenOverlaySettings = {},
        )
    }
}
