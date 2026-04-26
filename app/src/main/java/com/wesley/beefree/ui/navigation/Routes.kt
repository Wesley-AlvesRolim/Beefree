package com.wesley.beefree.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wesley.beefree.R
import com.wesley.beefree.ui.screens.HomeScreen
import com.wesley.beefree.ui.screens.SettingsScreen
import com.wesley.beefree.ui.screens.checkin.CheckInScreen
import com.wesley.beefree.ui.viewmodel.CheckInViewModel
import com.wesley.beefree.ui.viewmodel.HomeViewModel
import com.wesley.beefree.ui.viewmodel.SettingsViewModel

sealed class Screen(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    object Home : Screen("home", R.string.nav_home, Icons.Default.Home)

    object Settings : Screen("settings", R.string.settings_title, Icons.Default.Settings)

    object CheckIn : Screen("check_in", R.string.check_in_title, Icons.Default.CheckCircle)
}

@Composable
fun Routes(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel =
        viewModel(factory = HomeViewModel.factory(context))
    val settingsViewModel: SettingsViewModel =
        viewModel(factory = SettingsViewModel.factory(context))

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(innerPadding),
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
            )
        }
        composable(Screen.Settings.route) { SettingsScreen(settingsViewModel) }
        composable(Screen.CheckIn.route) {
            CheckInScreen(
                viewModel = viewModel(factory = CheckInViewModel.factory(context)),
                onDone = {
                    navController.popBackStack()
                    homeViewModel.refresh()
                },
            )
        }
    }
}
