package com.msharialsayari.musrofaty.ui.navigation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.msharialsayari.musrofaty.BuildConfig
import com.msharialsayari.musrofaty.MainActivity
import com.msharialsayari.musrofaty.pdf.PdfCreatorActivity
import com.msharialsayari.musrofaty.ui.permission.singlePermission
import com.msharialsayari.musrofaty.ui.screens.appearance_screen.AppearanceScreen
import com.msharialsayari.musrofaty.ui.screens.categories_screen.CategoriesScreen
import com.msharialsayari.musrofaty.ui.screens.content_screen.ContentScreen
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardScreen
import com.msharialsayari.musrofaty.ui.screens.filter_screen.FilterScreen
import com.msharialsayari.musrofaty.ui.screens.sender_details_screen.SenderDetailsScreen
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListScreen
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.SendersListScreen
import com.msharialsayari.musrofaty.ui.screens.senders_management_screen.SendersManagementScreen
import com.msharialsayari.musrofaty.ui.screens.settings_screen.SettingsScreen
import com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen.SmsAnalysisScreen
import com.msharialsayari.musrofaty.ui.screens.sms_screen.SmsScreen
import com.msharialsayari.musrofaty.ui.screens.splash_screen.SplashScreen
import com.msharialsayari.musrofaty.ui.screens.store_sms_list_Screen.StoreSmsListScreen
import com.msharialsayari.musrofaty.ui.screens.stores_screen.StoresScreen
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SharingFileUtils
import com.msharialsayari.musrofaty.utils.enums.ScreenType
import com.msharialsayari.musrofaty.utils.findActivity


@Composable
fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    onLanguageChanged: () -> Unit,
    onThemeChanged: () -> Unit,

) {

    val context = LocalContext.current
    val activity = context.findActivity()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        Modifier.padding(innerPadding)
    ) {
        composable(Screen.Splash.route) {
            SplashScreen( onLoadingDone = {
                navController.navigate(BottomNavItem.Dashboard.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
            })
        }

        composable(BottomNavItem.Dashboard.route) {
            DashboardScreen()
        }
        composable(BottomNavItem.SendersList.route) {
            SendersListScreen()
        }
        composable(BottomNavItem.Setting.route) {
            SettingsScreen(
                onLanguageChanged = { onLanguageChanged() },
                onThemeChanged = { onThemeChanged() },
            )
        }

        composable(Screen.SenderDetailsScreen.route + "/{senderId}",
            arguments = listOf(navArgument("senderId") { type = NavType.IntType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val senderId = arguments?.getInt("senderId") ?: 0
            SenderDetailsScreen(
                senderId = senderId,
                onNavigateToContent = { navController.navigate(Screen.ContentScreen.route + "/${it}") },
                onDone = { navController.navigateUp() },
                onBackPressed = { navController.navigateUp() }
            )
        }

        composable(Screen.SenderSmsListScreen.route + "/{senderId}",
            arguments = listOf(navArgument("senderId") { type = NavType.IntType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val senderId = arguments?.getInt("senderId") ?: 0
            SenderSmsListScreen(senderId = senderId)
        }


        composable(Screen.FilterScreen.route + "/{senderId}",
            arguments = listOf(navArgument("senderId") { type = NavType.IntType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val senderId = arguments?.getInt("senderId") ?: 0
            FilterScreen(
                senderId=senderId,
                filterId = null,
                onDone = { navController.navigateUp() },
                onBackPressed = { navController.navigateUp() }
            )
        }

        composable(Screen.FilterScreen.route + "/{senderId}" + "/{filterId}",
            arguments = listOf(navArgument("senderId") { type = NavType.IntType },
                navArgument("filterId") { type = NavType.IntType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val filterId = arguments?.getInt("filterId")
            val senderId = arguments?.getInt("senderId") ?: 0
            FilterScreen(
                senderId=senderId,
                filterId=filterId,
                onDone = { navController.navigateUp() },
                onBackPressed = { navController.navigateUp() }
            )
        }

        composable(Screen.SmsScreen.route + "/{smsId}",
            arguments = listOf(navArgument("smsId") { type = NavType.StringType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val smsId = arguments?.getString("smsId")
            smsId?.let {
                SmsScreen(smsId=it,)
            }
        }


        composable(Screen.CategoryScreen.route + "/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val categoryId = arguments?.getInt("categoryId")
            categoryId?.let {
                CategoriesScreen(
                    categoryId=categoryId,
                    onDone = { navController.navigateUp() },
                    onBackPressed = { navController.navigateUp() })
            }
        }


        composable(Screen.SendersManagementScreen.route) {
            SendersManagementScreen(
                onNavigateToSenderDetails = { navController.navigate(Screen.SenderDetailsScreen.route + "/${it}") },
                onBackPressed = { navController.navigateUp() })
        }

        composable(Screen.SmsAnalysisScreen.route) {
            SmsAnalysisScreen(
                onBackPressed = { navController.navigateUp() }
            )
        }

        composable(Screen.ContentScreen.route + "/{contentId}",
            arguments = listOf(navArgument("contentId") { type = NavType.IntType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val contentId = arguments?.getInt("contentId")
            contentId?.let {
                ContentScreen(
                    contentId=it,
                    onDone = { navController.navigateUp() },
                    onBackPressed = { navController.navigateUp() })
            }
        }

        composable(Screen.AppearanceScreen.route) {
            AppearanceScreen(
                onLanguageChanged = { onLanguageChanged() },
                onThemeChanged = { onThemeChanged() },
                onBackPressed = { navController.navigateUp() }
            )
        }

        composable(Screen.StoresScreen.route) {
            StoresScreen()
        }

        composable(Screen.StoreSmsListScreen.route + "/{storeName}",
            arguments = listOf(navArgument("storeName") { type = NavType.StringType }
            )) {backStackEntry->
            val arguments = backStackEntry.arguments
            val storeName = arguments?.getString("storeName")
            storeName?.let {
                StoreSmsListScreen(storeName = storeName, onBackPressed = { navController.navigateUp() })
            }
        }



        composable(Screen.SinglePermission.route) {
            singlePermission("")
        }

    }
}