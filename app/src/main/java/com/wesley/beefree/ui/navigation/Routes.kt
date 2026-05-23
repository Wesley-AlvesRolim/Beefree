package com.wesley.beefree.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wesley.beefree.R
import com.wesley.beefree.ui.screens.ActivityTrajectoryScreen
import com.wesley.beefree.ui.screens.HelpInterventionScreen
import com.wesley.beefree.ui.screens.HomeScreen
import com.wesley.beefree.ui.screens.RecoveryTrajectoryScreen
import com.wesley.beefree.ui.screens.SettingsScreen
import com.wesley.beefree.ui.screens.TriggerMapScreen
import com.wesley.beefree.ui.screens.checkin.CheckInScreen
import com.wesley.beefree.ui.screens.emotionalrecord.EmotionalRecordScreen
import com.wesley.beefree.ui.screens.onboarding.OnboardingScreen
import com.wesley.beefree.ui.viewmodel.CheckInViewModel
import com.wesley.beefree.ui.viewmodel.EmotionalRecordNavigationDestination
import com.wesley.beefree.ui.viewmodel.EmotionalRecordViewModel
import com.wesley.beefree.ui.viewmodel.HelpInterventionSource
import com.wesley.beefree.ui.viewmodel.HelpInterventionViewModel
import com.wesley.beefree.ui.viewmodel.HomeNavigationDestination
import com.wesley.beefree.ui.viewmodel.HomeViewModel
import com.wesley.beefree.ui.viewmodel.OnboardingViewModelImpl
import com.wesley.beefree.ui.viewmodel.RecoveryTrajectoryViewModel
import com.wesley.beefree.ui.viewmodel.SettingsViewModel
import com.wesley.beefree.ui.viewmodel.TriggerMapViewModel

sealed class Screen(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    object Onboarding : Screen("onboarding", R.string.onboarding_top_bar_title, Icons.Default.Home)

    object Home : Screen("home", R.string.nav_home, Icons.Default.Home)

    object CheckIn : Screen("check_in", R.string.check_in_title, Icons.Default.CheckCircle)

    object ActivityTrajectory :
        Screen("activity_trajectory", R.string.activity_trajectory_title, Icons.Default.Map)

    object RecoveryTrajectory : Screen(
        "recovery_trajectory",
        R.string.recovery_trajectory_title,
        Icons.AutoMirrored.Filled.TrendingUp,
    )

    object TriggerMap : Screen("trigger_map", R.string.trigger_map_title, Icons.Default.TagFaces)

    object HelpIntervention :
        Screen("help_intervention", R.string.help_title, Icons.AutoMirrored.Filled.Help)

    object EmotionalRecord :
        Screen("emotional_record", R.string.emotional_record_title, Icons.Default.TagFaces)

    object Settings : Screen("settings", R.string.settings_title, Icons.Default.Settings)
}

@Composable
fun Routes(
    navController: NavHostController,
    innerPadding: PaddingValues,
    isOnboardingCompleted: Boolean = true,
    onOnboardingFinished: () -> Unit = {},
) {
    val context = LocalContext.current
    val onboardingViewModel: OnboardingViewModelImpl =
        viewModel(factory = OnboardingViewModelImpl.factory(context))
    val homeViewModel: HomeViewModel =
        viewModel(factory = HomeViewModel.factory(context))
    val emotionalRecordViewModel: EmotionalRecordViewModel =
        viewModel(factory = EmotionalRecordViewModel.factory(context))
    val settingsViewModel: SettingsViewModel =
        viewModel(factory = SettingsViewModel.factory(context))

    val startDestination = if (isOnboardingCompleted) Screen.Home.route else Screen.Onboarding.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding),
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                viewModel = onboardingViewModel,
                onFinish = {
                    onOnboardingFinished()
                    homeViewModel.refresh()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
            )
        }
        composable(Screen.Home.route) {
            LaunchedEffect(homeViewModel) {
                homeViewModel.navigationEvents.collect { destination ->
                    when (destination) {
                        HomeNavigationDestination.CheckIn ->
                            navController.navigate(Screen.CheckIn.route)

                        HomeNavigationDestination.RecoveryTrajectory ->
                            navController.navigate(Screen.ActivityTrajectory.route)

                        HomeNavigationDestination.FeelingDetails ->
                            navController.navigate(Screen.RecoveryTrajectory.route)

                        is HomeNavigationDestination.HelpIntervention ->
                            navController.navigate("${Screen.HelpIntervention.route}?source=${destination.source.name}")

                        HomeNavigationDestination.TriggerMap ->
                            navController.navigate(Screen.TriggerMap.route)

                        HomeNavigationDestination.Onboarding ->
                            navController.navigate(Screen.Onboarding.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                    }
                }
            }
            HomeScreen(viewModel = homeViewModel)
        }
        composable(Screen.CheckIn.route) {
            CheckInScreen(
                viewModel = viewModel(factory = CheckInViewModel.factory(context)),
                onDone = {
                    navController.popBackStack()
                    homeViewModel.refresh()
                },
            )
        }
        composable(Screen.ActivityTrajectory.route) {
            ActivityTrajectoryScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.RecoveryTrajectory.route) {
            RecoveryTrajectoryScreen(
                viewModel = viewModel(factory = RecoveryTrajectoryViewModel.factory(context)),
                onBack = { navController.popBackStack() },
            )
        }
        composable(Screen.TriggerMap.route) {
            TriggerMapScreen(
                viewModel = viewModel(factory = TriggerMapViewModel.factory(context)),
                onBack = { navController.popBackStack() },
            )
        }
        composable(
            route = "${Screen.HelpIntervention.route}?source={source}",
            arguments =
                listOf(
                    navArgument("source") {
                        type = NavType.StringType
                        defaultValue = HelpInterventionSource.FAB.name
                    },
                ),
        ) { backStackEntry ->
            val sourceString =
                backStackEntry.arguments?.getString("source") ?: HelpInterventionSource.FAB.name
            val source =
                try {
                    HelpInterventionSource.valueOf(sourceString)
                } catch (e: Exception) {
                    HelpInterventionSource.FAB
                }
            HelpInterventionScreen(
                viewModel = viewModel(factory = HelpInterventionViewModel.factory(context, source)),
                onDismiss = { navController.popBackStack() },
            )
        }
        composable(Screen.EmotionalRecord.route) {
            LaunchedEffect(emotionalRecordViewModel) {
                emotionalRecordViewModel.navigationEvents.collect { destination ->
                    when (destination) {
                        EmotionalRecordNavigationDestination.Done ->
                            navController.popBackStack()
                    }
                }
            }
            EmotionalRecordScreen(
                viewModel = emotionalRecordViewModel,
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
            )
        }
    }
}
