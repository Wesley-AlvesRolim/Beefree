package com.wesley.beefreepoc.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wesley.beefreepoc.ui.screens.HomeScreen
import com.wesley.beefreepoc.ui.screens.SettingsScreen

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    object Home : Screen("home", "Início", Icons.Default.Home)

    object Settings : Screen("settings", "Configurações", Icons.Default.Settings)
}

@Composable
fun Routes(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(innerPadding),
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}
