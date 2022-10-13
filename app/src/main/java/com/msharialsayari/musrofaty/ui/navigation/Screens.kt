package com.msharialsayari.musrofaty.ui.navigation

import com.msharialsayari.musrofaty.R

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object SenderDetails : Screen("sender_details")
    object SenderSmsList : Screen("sender_sms_list")
    object CategoryScreen : Screen("category_screen")
    object FilterScreen : Screen("filter_screen")
    object SmsScreen : Screen("sms_screen")
    object SendersManagementScreen : Screen("sender_management_screen")
    object SmsAnalysisScreen : Screen("sms_analysis_screen")
    object ContentScreen : Screen("content_screen")
    object AppearanceScreen : Screen("appearance_screen")
    object SinglePermission : Screen("single_permission_screen")

}


sealed class BottomNavItem(var title:Int, var icon:Int, var screen_route:String){
    object Dashboard   : BottomNavItem(R.string.dashboard_title_screen    ,R.drawable.ic_dashboard, "dashboard")
    object SendersList : BottomNavItem(R.string.senders_list_title_screen,R.drawable.ic_dashboard, "senders_list")
    object Setting     : BottomNavItem(R.string.settings_title_screen,R.drawable.ic_settings , "settings")
}