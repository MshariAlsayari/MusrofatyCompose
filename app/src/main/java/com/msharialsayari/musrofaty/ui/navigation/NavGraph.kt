package com.msharialsayari.musrofaty.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardScreen
import com.msharialsayari.musrofaty.ui.screens.settings_screen.SettingsScreen
import com.msharialsayari.musrofaty.ui.screens.sms_list_screen.SmsListScreen
import com.msharialsayari.musrofaty.ui.screens.splash_screen.SplashScreen

@Composable
fun NavigationGraph(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        Modifier.padding(innerPadding)
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(BottomNavItem.Dashboard.screen_route) {
            DashboardScreen()
        }
        composable(BottomNavItem.SmsList.screen_route) {
            SmsListScreen()
        }
        composable(BottomNavItem.Setting.screen_route) {
            SettingsScreen()
        }

    }
}