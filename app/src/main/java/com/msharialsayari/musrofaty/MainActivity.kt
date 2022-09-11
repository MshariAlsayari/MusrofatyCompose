package com.msharialsayari.musrofaty


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardScreen
import com.msharialsayari.musrofaty.ui.screens.settings_screen.SettingsScreen
import com.msharialsayari.musrofaty.ui.screens.sms_list_screen.SmsListScreen
import com.msharialsayari.musrofaty.ui.screens.splash_screen.SplashScreen
import com.msharialsayari.musrofaty.ui.theme.MusrofatyComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusrofatyComposeTheme {
                MainScreenView()
            }
        }
    }
}


@Composable
fun MainScreenView() {
    val bottomBarState        = rememberSaveable { (mutableStateOf(false)) }
    val topBarState           = rememberSaveable { (mutableStateOf(true)) }
    val navController         = rememberNavController()
    val bottomNavigationItems = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.SmsList,
        BottomNavItem.Setting
    )


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navBackStackEntry?.destination?.route) {
        BottomNavItem.Dashboard.screen_route -> {
            bottomBarState.value = true
            topBarState.value = false
        }
        BottomNavItem.SmsList.screen_route  -> {
            bottomBarState.value = true
            topBarState.value = false
        }
        BottomNavItem.Setting.screen_route -> {
            bottomBarState.value = true
            topBarState.value = false
        }

        else ->{
            bottomBarState.value = false
            topBarState.value = false
        }

    }


    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController, items = bottomNavigationItems,  bottomBarState = bottomBarState)
        }
    ) { innerPadding ->
        NavigationGraph(navController = navController, innerPadding = innerPadding)
    }
}


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