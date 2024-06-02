package com.msharialsayari.musrofaty.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.msharialsayari.musrofaty.MainViewModel
import com.msharialsayari.musrofaty.navigation.BottomBarLayout
import com.msharialsayari.musrofaty.navigation.NavigationRailLayout
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigatorViewModel
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.LaunchNavigatorObserver
import com.msharialsayari.musrofaty.utils.enums.ScreenType

@Composable
fun MainScreenView(
    mainViewModel: MainViewModel,
    navigatorViewModel: AppNavigatorViewModel,
    screenType: ScreenType,
) {
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val navController = rememberNavController()
    val bottomNavigationItems = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.SendersList,
        BottomNavItem.Setting
    )


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navBackStackEntry?.destination?.route) {
        BottomNavItem.Dashboard.route -> {
            bottomBarState.value = true
        }

        BottomNavItem.SendersList.route -> {
            bottomBarState.value = true
        }

        BottomNavItem.Setting.route -> {
            bottomBarState.value = true
        }

        else -> {
            bottomBarState.value = false

        }

    }

    LaunchNavigatorObserver(
        navigatorState = navigatorViewModel.destinations,
        navController = navController
    )

    if(screenType.isBottomNavigation){
        BottomBarLayout(
            mainViewModel = mainViewModel,
            navController = navController,
            items = bottomNavigationItems,
            bottomBarState = bottomBarState
        )
    }else{
        NavigationRailLayout(
            mainViewModel =mainViewModel,
            navController = navController,
            items = bottomNavigationItems,
            bottomBarState = bottomBarState
        )

    }
}