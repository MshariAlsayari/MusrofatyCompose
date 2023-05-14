package com.msharialsayari.musrofaty.ui.navigation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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


@Composable
fun NavigationGraph(
    activity: MainActivity,
    navController: NavHostController,
    innerPadding: PaddingValues,
    screenType: ScreenType,
    onLanguageChanged: () -> Unit,
    onThemeChanged: () -> Unit,

) {
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
            DashboardScreen(
                screenType=screenType,
                onSmsClicked = {
                    navController.navigate(Screen.SmsScreen.route + "/${it}")
                },
                onNavigateToSenderSmsList = {
                    navController.navigate(Screen.SenderSmsListScreen.route + "/${it}")
                })
        }
        composable(BottomNavItem.SendersList.route) {
            SendersListScreen(
                onNavigateToSenderDetails = {
                    navController.navigate(Screen.SenderDetailsScreen.route + "/${it}")
                },
                onNavigateToSenderSmsList = {
                    navController.navigate(Screen.SenderSmsListScreen.route + "/${it}")
                }
            )
        }
        composable(BottomNavItem.Setting.route) {
            SettingsScreen(
                onAppearanceClicked = {
                    navController.navigate(Screen.AppearanceScreen.route)
                },
                onSendersClicked = {
                    navController.navigate(Screen.SendersManagementScreen.route)
                },
                onStoresClicked = {
                    navController.navigate(Screen.StoresScreen.route)
                },
                onAnalysisClicked = {
                    navController.navigate(Screen.SmsAnalysisScreen.route)
                },
                onUpdateClicked = {
                    var packageName =  activity.packageName
                    if (BuildConfig.DEBUG) {
                        var pk =""
                        packageName.split(".")
                                   .mapIndexed { index, s ->
                                       when (index) {
                                           0 -> pk += "$s."
                                           1 -> pk += "$s."
                                           2 -> pk += s
                                       }
                                   }

                        packageName = pk
                    }

                    try {
                        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                    } catch (e: ActivityNotFoundException) {
                        activity. startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                    }
                }
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
            SenderSmsListScreen(
                senderId = senderId,
                onDetailsClicked = { navController.navigate(Screen.SenderDetailsScreen.route + "/${it}") },
                onBack = { navController.navigateUp() },
                onNavigateToFilterScreen = { senderId, filterId ->
                    if (filterId == null)
                        navController.navigate(Screen.FilterScreen.route + "/${senderId}")
                    else
                        navController.navigate(Screen.FilterScreen.route + "/${senderId}" + "/${filterId}")

                },
                onSmsClicked = {
                    navController.navigate(Screen.SmsScreen.route + "/${it}")
                },
                onExcelFileGenerated = {
                    val fileURI = SharingFileUtils.accessFile(activity, Constants.EXCEL_FILE_NAME)
                    val intent = SharingFileUtils.createSharingIntent(activity, fileURI)
                    activity.startActivity(intent)
                },
                onNavigateToPDFCreatorActivity = {
                    PdfCreatorActivity.startPdfCreatorActivity(activity,it)
                }
            )
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
                SmsScreen(
                    smsId=it,
                    onNavigateToCategoryScreen = { navController.navigate(Screen.CategoryScreen.route + "/${it}") },
                    onBackPressed = { navController.navigateUp() }
                )
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
            StoresScreen(
                onNavigateToCategoryScreen = { navController.navigate(Screen.CategoryScreen.route + "/${it}") },
                onNavigateToStoreSmsListScreen = {navController.navigate(Screen.StoreSmsListScreen.route + "/${it}")},
                onBackPressed = { navController.navigateUp() }
            )
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