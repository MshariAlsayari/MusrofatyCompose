package com.msharialsayari.musrofaty.ui.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.msharialsayari.musrofaty.ui.permission.singlePermission
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardScreen
import com.msharialsayari.musrofaty.ui.screens.sender_details.SenderDetailsScreen
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.SendersListScreen
import com.msharialsayari.musrofaty.ui.screens.settings_screen.SettingsScreen
import com.msharialsayari.musrofaty.ui.screens.splash_screen.SplashScreen

@Composable
fun NavigationGraph(activity:Activity, navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        Modifier.padding(innerPadding)
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController, settingPermission = {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri: Uri = Uri.fromParts("package", navController.context.packageName, null)
                intent.data = uri
                activity.startActivityForResult(intent, 123)
            })
        }

        composable(BottomNavItem.Dashboard.screen_route) {
            DashboardScreen(navController = navController)
        }
        composable(BottomNavItem.SendersList.screen_route) {
            SendersListScreen(navController = navController)
        }
        composable(BottomNavItem.Setting.screen_route) {
            SettingsScreen(navController = navController)
        }

        composable(Screen.SenderDetails.route,
            arguments = listOf(navArgument("senderId") { type = NavType.IntType }
        )) {backStackEntry->
            SenderDetailsScreen(backStackEntry.arguments?.getInt("senderId")?:0)
        }

        composable(Screen.SinglePermission.route) {
            singlePermission("")
        }

    }
}