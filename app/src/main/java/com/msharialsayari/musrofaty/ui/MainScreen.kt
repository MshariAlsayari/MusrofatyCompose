package com.msharialsayari.musrofaty.ui

import android.icu.text.ListFormatter.Width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.msharialsayari.musrofaty.navigation.BottomNavigation
import com.msharialsayari.musrofaty.MainActivity
import com.msharialsayari.musrofaty.navigation.BottomBarLayout
import com.msharialsayari.musrofaty.navigation.NavigationRailLayout
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.NavigationGraph

@Composable
fun MainScreenView(
    activity: MainActivity,
    windowSizeClass: WindowSizeClass,
    onLanguageChanged: () -> Unit,
    onThemeChanged: () -> Unit
) {
    val context = LocalContext.current
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val navController = rememberNavController()
    val navHost = remember {
        movableContentOf<PaddingValues>{innerPadding->
            NavigationGraph(
                activity = activity,
                navController = navController,
                innerPadding = innerPadding,
                onLanguageChanged = onLanguageChanged,
                onThemeChanged = onThemeChanged
            )


        }
    }
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

    when(windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact->
            BottomBarLayout(
                activity = activity,
                navController = navController,
                items=bottomNavigationItems,
                bottomBarState= bottomBarState,
                onLanguageChanged = onLanguageChanged,
                onThemeChanged = onThemeChanged)
        else->{
            NavigationRailLayout(
                activity = activity,
                navController = navController,
                items=bottomNavigationItems,
                bottomBarState= bottomBarState,
                onLanguageChanged = onLanguageChanged,
                onThemeChanged = onThemeChanged)
        }
    }


}