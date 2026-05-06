package com.wesley.beefree.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wesley.beefree.ui.components.designsystem.BeeBodySmall
import com.wesley.beefree.ui.components.designsystem.BeeSpacing

private val bottomBarRoutes = setOf(Screen.Home.route, Screen.Settings.route)

@Composable
fun NavBar(
    openCheckIn: Boolean = false,
    openSos: Boolean = false,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomBar = navBackStackEntry?.destination?.route in bottomBarRoutes

    LaunchedEffect(openCheckIn) {
        if (openCheckIn) {
            navController.navigate(Screen.CheckIn.route)
        }
    }

    LaunchedEffect(openSos) {
        if (openSos) {
            navController.navigate(Screen.HelpIntervention.route)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { if (showBottomBar) NavBarWithItems(navController) },
    ) { innerPadding ->
        Routes(navController, innerPadding)
    }
}

@Composable
fun NavBarWithItems(navController: NavHostController) {
    val items = listOf(Screen.Home, Screen.Settings)

    fun onClick(screen: Screen) {
        navController.navigate(screen.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .navigationBarsPadding()
                .padding(horizontal = BeeSpacing.S, vertical = BeeSpacing.S),
        horizontalArrangement = Arrangement.spacedBy(BeeSpacing.S),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val fgColor =
                if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            val bgColor =
                if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(BeeSpacing.XL))
                        .background(bgColor)
                        .clickable { onClick(screen) }
                        .padding(horizontal = BeeSpacing.L, vertical = BeeSpacing.S),
            ) {
                Icon(screen.icon, contentDescription = null, tint = fgColor)
                BeeBodySmall(stringResource(screen.labelRes), color = fgColor)
            }
        }
    }
}
