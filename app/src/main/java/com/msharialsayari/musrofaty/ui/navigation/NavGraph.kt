package com.msharialsayari.musrofaty.ui.navigation

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
import com.msharialsayari.musrofaty.ui.permission.singlePermission
import com.msharialsayari.musrofaty.ui.screens.appearance_screen.AppearanceScreen
import com.msharialsayari.musrofaty.ui.screens.categories_screen.CategoriesScreen
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListScreen
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListViewModel.Companion.CATEGORY_ID_KEY
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListViewModel.Companion.END_DATE_KEY
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListViewModel.Companion.FILTER_OPTION_KEY
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListViewModel.Companion.QUERY_KEY
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListViewModel.Companion.START_DATE_KEY
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
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.StatisticsScreen
import com.msharialsayari.musrofaty.ui.screens.store_sms_list_Screen.StoreSmsListScreen
import com.msharialsayari.musrofaty.ui.screens.stores_screen.StoresScreen
import com.msharialsayari.musrofaty.utils.findActivity


@Composable
fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {

    val context = LocalContext.current
    val activity = context.findActivity()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        Modifier.padding(innerPadding)
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onLoadingDone = {
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
            SettingsScreen()
        }

        composable(Screen.SenderDetailsScreen.route + "/{senderId}",
            arguments = listOf(navArgument("senderId") { type = NavType.IntType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val senderId = arguments?.getInt("senderId") ?: 0
            SenderDetailsScreen(senderId = senderId)
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
            FilterScreen(senderId = senderId, filterId = null)
        }

        composable(Screen.FilterScreen.route + "/{senderId}" + "/{filterId}",
            arguments = listOf(navArgument("senderId") { type = NavType.IntType },
                navArgument("filterId") { type = NavType.IntType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val filterId = arguments?.getInt("filterId")
            val senderId = arguments?.getInt("senderId") ?: 0
            FilterScreen(senderId = senderId, filterId = filterId)
        }

        composable(Screen.SmsScreen.route + "/{smsId}",
            arguments = listOf(navArgument("smsId") { type = NavType.StringType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val smsId = arguments?.getString("smsId")
            smsId?.let {
                SmsScreen(smsId = it)
            }
        }


        composable(
            Screen.CategoryScreen.route + "/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val categoryId = arguments?.getInt("categoryId")
            categoryId?.let {
                CategoriesScreen(categoryId = categoryId)
            }
        }


        composable(Screen.SendersManagementScreen.route) {
            SendersManagementScreen()
        }

        composable(Screen.SmsAnalysisScreen.route) {
            SmsAnalysisScreen()
        }

        composable(Screen.ContentScreen.route + "/{contentId}",
            arguments = listOf(navArgument("contentId") { type = NavType.IntType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val contentId = arguments?.getInt("contentId")
            contentId?.let {
                ContentScreen(contentId = it)
            }
        }

        composable(Screen.AppearanceScreen.route) {
            AppearanceScreen()
        }

        composable(Screen.StoresScreen.route) {
            StoresScreen()
        }

        composable(Screen.StoreSmsListScreen.route + "/{storeName}",
            arguments = listOf(navArgument("storeName") { type = NavType.StringType }
            )) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val storeName = arguments?.getString("storeName")
            storeName?.let {
                StoreSmsListScreen(storeName = storeName)
            }
        }



        composable(Screen.SinglePermission.route) {
            singlePermission("")
        }

        composable(Screen.StatisticsScreen.route) {
            StatisticsScreen()
        }

        composable(
            Screen.CategorySmsListScreen.route + "/{$CATEGORY_ID_KEY}" + "/{$FILTER_OPTION_KEY}" + "/{$START_DATE_KEY}" + "/{$END_DATE_KEY}"+ "/{$QUERY_KEY}",
            arguments = listOf(
                navArgument(CATEGORY_ID_KEY) { type = NavType.IntType },
                navArgument(FILTER_OPTION_KEY) { type = NavType.IntType },
                navArgument(START_DATE_KEY) { type = NavType.LongType },
                navArgument(END_DATE_KEY) { type = NavType.LongType },
                navArgument(QUERY_KEY) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "null"
                },

            )
        ) {
            CategorySmsListScreen()
        }

    }
}