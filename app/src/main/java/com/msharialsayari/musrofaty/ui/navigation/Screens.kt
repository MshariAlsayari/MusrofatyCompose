package com.msharialsayari.musrofaty.ui.navigation

import com.msharialsayari.musrofaty.R


open class BaseScreen(val route: String, var title:Int?=null)

sealed class Screen(route: String, title: Int? = null): BaseScreen(route, title) {
    object Splash                  : Screen("splash_screen")
    object SenderDetailsScreen     : Screen("sender_details_screen", title = R.string.sender_details_title_screen)
    object SenderSmsListScreen     : Screen("sender_sms_list_screen")
    object CategoryScreen          : Screen("category_screen", title = R.string.category_title_screen)
    object FilterScreen            : Screen("filter_screen", title = R.string.filter_title_screen)
    object SmsScreen               : Screen("sms_screen", title = R.string.sms_title_screen)
    object SendersManagementScreen : Screen("sender_management_screen",title = R.string.pref_managment_sender_title)
    object SmsAnalysisScreen       : Screen("sms_analysis_screen",title = R.string.pref_managment_analysis_title)
    object ContentScreen           : Screen("content_screen",title = R.string.sender_category)
    object AppearanceScreen        : Screen("appearance_screen",title = R.string.pref_appearance_title)
    object StoresScreen            : Screen("stores_screen",title = R.string.pref_managment_stores_title)
    object StoreSmsListScreen      : Screen("store_sms_list_screen",title = R.string.pref_managment_stores_title)
    object SinglePermission        : Screen("single_permission_screen")
    object StatisticsScreen       : Screen("statistics_screen",title = R.string.statistics_screen_title)

    object SmsListScreen   : Screen("sms_list_screen",title = R.string.category_title_screen)

}


sealed class BottomNavItem(var icon:Int,route: String, title: Int? = null): BaseScreen(route, title){
    object Dashboard   : BottomNavItem(title= R.string.dashboard_title_screen ,icon=R.drawable.ic_dashboard, route="dashboard_item")
    object SendersList : BottomNavItem(title= R.string.senders_list_title_screen,icon=R.drawable.ic_list, route="senders_list_item")
    object Setting     : BottomNavItem(title= R.string.settings_title_screen,icon=R.drawable.ic_settings , route="settings_item")
}