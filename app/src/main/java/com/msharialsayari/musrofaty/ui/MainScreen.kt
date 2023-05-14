package com.msharialsayari.musrofaty.ui

import android.util.DisplayMetrics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.msharialsayari.musrofaty.MainActivity
import com.msharialsayari.musrofaty.navigation.BottomBarLayout
import com.msharialsayari.musrofaty.navigation.NavigationRailLayout
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.utils.enums.ScreenType
import com.msharialsayari.musrofaty.utils.getScreenTypeByWidth

@Composable
fun MainScreenView(
    activity: MainActivity,
    windowSizeClass: DisplayMetrics,
    onLanguageChanged: () -> Unit,
    onThemeChanged: () -> Unit
) {
    val context = LocalContext.current
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

    val screenType = remember { mutableStateOf(windowSizeClass.getScreenTypeByWidth())  }
    when(windowSizeClass.getScreenTypeByWidth()){
        ScreenType.Compact->
            BottomBarLayout(
                activity = activity,
                navController = navController,
                items=bottomNavigationItems,
                bottomBarState= bottomBarState,
                screenType = screenType.value,
                onLanguageChanged = onLanguageChanged,
                onThemeChanged = onThemeChanged)
        else->{
            NavigationRailLayout(
                activity = activity,
                navController = navController,
                items=bottomNavigationItems,
                bottomBarState= bottomBarState,
                screenType = screenType.value,
                onLanguageChanged = onLanguageChanged,
                onThemeChanged = onThemeChanged)
        }
    }


}