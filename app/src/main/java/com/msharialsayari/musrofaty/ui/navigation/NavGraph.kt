package com.msharialsayari.musrofaty.ui.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.msharialsayari.musrofaty.MainViewModel
import com.msharialsayari.musrofaty.ui.permission.singlePermission
import com.msharialsayari.musrofaty.ui.screens.appearance_screen.AppearanceScreen
import com.msharialsayari.musrofaty.ui.screens.categories_screen.CategoriesScreen
import com.msharialsayari.musrofaty.ui.screens.content_screen.ContentScreen
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardScreen
import com.msharialsayari.musrofaty.ui.screens.filter_screen.FilterScreen
import com.msharialsayari.musrofaty.ui.screens.filter_screen.FilterViewModel.Companion.CREATE_FILTER_KEY
import com.msharialsayari.musrofaty.ui.screens.filter_screen.FilterViewModel.Companion.FILTER_ID_KEY
import com.msharialsayari.musrofaty.ui.screens.filter_screen.FilterViewModel.Companion.SENDER_ID_KEY
import com.msharialsayari.musrofaty.ui.screens.sender_details_screen.SenderDetailsScreen
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListScreen
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.SendersListScreen
import com.msharialsayari.musrofaty.ui.screens.senders_management_screen.SendersManagementScreen
import com.msharialsayari.musrofaty.ui.screens.settings_screen.SettingsScreen
import com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen.SmsAnalysisScreen
import com.msharialsayari.musrofaty.ui.screens.sms_detector_tool_screen.SmsDetectorToolScreen
import com.msharialsayari.musrofaty.ui.screens.sms_list_screen.SmsListScreen
import com.msharialsayari.musrofaty.ui.screens.sms_list_screen.SmsListViewModel.Companion.SCREEN_TITLE_KEY
import com.msharialsayari.musrofaty.ui.screens.sms_list_screen.SmsListViewModel.Companion.SMS_IDS_KEY
import com.msharialsayari.musrofaty.ui.screens.sms_permission_screen.SmsPermissionScreen
import com.msharialsayari.musrofaty.ui.screens.sms_screen.SmsScreen
import com.msharialsayari.musrofaty.ui.screens.sms_types_screen.SmsTypesScreen
import com.msharialsayari.musrofaty.ui.screens.splash_screen.SplashScreen
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.StatisticsScreen
import com.msharialsayari.musrofaty.ui.screens.store_sms_list_Screen.StoreSmsListScreen
import com.msharialsayari.musrofaty.ui.screens.store_sms_list_Screen.StoreSmsListViewModel.Companion.STORE_NAME_KEY
import com.msharialsayari.musrofaty.ui.screens.stores_screen.StoresScreen


@Composable
fun NavigationGraph(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    innerPadding: PaddingValues,
) {

    val uiState by mainViewModel.uiState.collectAsState()
    val isSmsGranted = uiState.smsPermissionGranted
    val context = LocalContext.current as Activity

    LaunchedEffect(isSmsGranted){
         if(isSmsGranted) {
            navController.navigate(Screen.Splash.route){
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }else{
             navController.navigate(Screen.SmsPermissionScreen.route){
                 popUpTo(navController.graph.id) {
                     inclusive = true
                 }
             }
        }
    }



    NavHost(
        navController = navController,
        startDestination = Screen.Default.route,
        modifier =   Modifier
            .statusBarsPadding()
            .padding(innerPadding)
    ) {

        composable(Screen.Default.route){}

        composable(Screen.Splash.route) {
            SplashScreen(onLoadingDone = {
                navController.navigate(BottomNavItem.Dashboard.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            })
        }

        composable(Screen.SmsPermissionScreen.route) {
            SmsPermissionScreen(onActionBtnClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
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
            )) {
            SenderSmsListScreen()
        }



        composable(Screen.FilterScreen.route + "/{$SENDER_ID_KEY}" + "/{$FILTER_ID_KEY}" + "/{$CREATE_FILTER_KEY}",
            arguments = listOf(
                navArgument(SENDER_ID_KEY) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(FILTER_ID_KEY) {
                    type = NavType.IntType
                    defaultValue = -1
                },

                navArgument(CREATE_FILTER_KEY) {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )) { backStackEntry ->
            FilterScreen()
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
            SmsAnalysisScreen(isSmsAnalysisScreen = true)
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

        composable(Screen.StoreSmsListScreen.route + "/{$STORE_NAME_KEY}",
            arguments = listOf(navArgument(STORE_NAME_KEY) { type = NavType.StringType }
            )) { backStackEntry ->
            StoreSmsListScreen()
        }



        composable(Screen.SinglePermission.route) {
            singlePermission("")
        }

        composable(Screen.StatisticsScreen.route) {
            StatisticsScreen()
        }

        composable(
            Screen.SmsListScreen.route + "/{$SCREEN_TITLE_KEY}" + "/{$SMS_IDS_KEY}",
            arguments = listOf(
                navArgument(SCREEN_TITLE_KEY) { type = NavType.StringType },
                navArgument(SMS_IDS_KEY) {
                    type = NavType.StringType
                }

            )
        ) {
            SmsListScreen()
        }

        composable(Screen.SmsTypesScreen.route) {
            SmsAnalysisScreen(isSmsAnalysisScreen = false)
        }

        composable(Screen.SmsDetectorToolScreen.route) {
            SmsDetectorToolScreen()
        }

    }
}