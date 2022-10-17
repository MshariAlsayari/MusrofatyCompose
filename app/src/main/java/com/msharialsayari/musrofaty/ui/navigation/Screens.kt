package com.msharialsayari.musrofaty.ui.navigation

import com.msharialsayari.musrofaty.R


open class BaseScreen(val route: String, var title:Int?=null){


    companion object {

        fun getScreenByRoute(route: String): BaseScreen {
            return when (route) {
                Screen.Splash.route -> Screen.Splash
                Screen.SenderDetailsScreen.route -> Screen.SenderDetailsScreen
                Screen.SenderSmsListScreen.route -> Screen.SenderSmsListScreen
                Screen.CategoryScreen.route -> Screen.CategoryScreen
                Screen.FilterScreen.route -> Screen.FilterScreen
                Screen.SmsScreen.route -> Screen.SmsScreen
                Screen.SendersManagementScreen.route -> Screen.SendersManagementScreen
                Screen.SmsAnalysisScreen.route -> Screen.SmsAnalysisScreen
                Screen.ContentScreen.route -> Screen.ContentScreen
                Screen.AppearanceScreen.route -> Screen.AppearanceScreen
                Screen.StoresScreen.route -> Screen.StoresScreen
                Screen.SinglePermission.route -> Screen.SinglePermission
                BottomNavItem.Dashboard.route -> BottomNavItem.Dashboard
                BottomNavItem.SendersList.route -> BottomNavItem.SendersList
                BottomNavItem.Setting.route -> BottomNavItem.Setting
                else -> {
                    Screen.SinglePermission
                }
            }
        }
    }

}
sealed class Screen(route: String, title: Int? = null): BaseScreen(route, title) {
    object Splash                  : Screen("splash_screen")
    object SenderDetailsScreen     : Screen("sender_details_screen", title = R.string.sender_details_title_screen)
    object SenderSmsListScreen     : Screen("sender_sms_list_screen")
    object CategoryScreen          : Screen("category_screen", title = R.string.category_title_screen)
    object FilterScreen            : Screen("filter_screen", title = R.string.filter_title_screen)
    object SmsScreen               : Screen("sms_screen", title = R.string.sms_title_screen)
    object SendersManagementScreen : Screen("sender_management_screen",title = R.string.pref_managment_sender_title)
    object SmsAnalysisScreen       : Screen("sms_analysis_screen",title = R.string.pref_managment_analysis_title)
    object ContentScreen           : Screen("content_screen")
    object AppearanceScreen        : Screen("appearance_screen",title = R.string.pref_appearance_title)
    object StoresScreen            : Screen("stores_screen",title = R.string.pref_managment_stores_title)
    object SinglePermission        : Screen("single_permission_screen")

}


sealed class BottomNavItem(var icon:Int,route: String, title: Int? = null): BaseScreen(route, title){
    object Dashboard   : BottomNavItem(title= R.string.dashboard_title_screen ,icon=R.drawable.ic_dashboard, route="dashboard_item")
    object SendersList : BottomNavItem(title= R.string.senders_list_title_screen,icon=R.drawable.ic_dashboard, route="senders_list_item")
    object Setting     : BottomNavItem(title= R.string.settings_title_screen,icon=R.drawable.ic_settings , route="settings_item")
}