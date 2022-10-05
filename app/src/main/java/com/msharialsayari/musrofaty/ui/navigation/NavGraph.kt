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
import com.msharialsayari.musrofaty.ui.screens.filter_screen.FilterScreen
import com.msharialsayari.musrofaty.ui.screens.sender_details_screen.SenderDetailsScreen
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListScreen
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.SendersListScreen
import com.msharialsayari.musrofaty.ui.screens.settings_screen.SettingsScreen
import com.msharialsayari.musrofaty.ui.screens.splash_screen.SplashScreen

@Composable
fun NavigationGraph(
    activity: Activity,
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        Modifier.padding(innerPadding)
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(settingPermission = {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri: Uri = Uri.fromParts("package", navController.context.packageName, null)
                intent.data = uri
                activity.startActivityForResult(intent, 123)
            }, onLoadingDone = {
                navController.navigate(BottomNavItem.Dashboard.screen_route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
            })
        }

        composable(BottomNavItem.Dashboard.screen_route) {
            DashboardScreen()
        }
        composable(BottomNavItem.SendersList.screen_route) {
            SendersListScreen(
                onNavigateToSenderDetails = {
                    navController.navigate(Screen.SenderDetails.route + "/${it}")
                },
                onNavigateToSenderSmsList = {
                    navController.navigate(Screen.SenderSmsList.route + "/${it}")
                }
            )
        }
        composable(BottomNavItem.Setting.screen_route) {
            SettingsScreen()
        }

        composable(Screen.SenderDetails.route + "/{senderId}",
            arguments = listOf(navArgument("senderId") { type = NavType.IntType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val senderId = arguments?.getInt("senderId") ?: 0
            SenderDetailsScreen(senderId)
        }

        composable(Screen.SenderSmsList.route + "/{senderId}",
            arguments = listOf(navArgument("senderId") { type = NavType.IntType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val senderId = arguments?.getInt("senderId") ?: 0
            SenderSmsListScreen(
                senderId,
                onDetailsClicked = {
                navController.navigate(Screen.SenderDetails.route + "/${it}")
                },
                onBack = {
                    navController.navigateUp()
                },
                onNavigateToFilterScreen = {
                    if (it == null)
                        navController.navigate(Screen.FilterScreen.route)
                    else
                    navController.navigate(Screen.FilterScreen.route + "/${it}")
                }
            )
        }


        composable(Screen.FilterScreen.route) {
            FilterScreen(null)
        }

        composable(Screen.FilterScreen.route + "/{filterId}",
            arguments = listOf(navArgument("filterId") { type = NavType.IntType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val filterId = arguments?.getInt("filterId")
            FilterScreen(filterId)
        }

        composable(Screen.SinglePermission.route) {
            singlePermission("")
        }

    }
}