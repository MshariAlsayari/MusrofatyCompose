package com.msharialsayari.musrofaty.navigation

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import com.msharialsayari.musrofaty.MainActivity
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.NavigationGraph
import com.msharialsayari.musrofaty.utils.enums.ScreenType

@Composable
fun BottomBarLayout(
    activity:MainActivity,
    navController: NavHostController,
    items:List<BottomNavItem>,
    bottomBarState: MutableState<Boolean>,
    onLanguageChanged: () -> Unit,
    onThemeChanged: () -> Unit){

    Scaffold(
        bottomBar = {
            BottomNavigation(
                navController = navController,
                items = items,
                bottomBarState = bottomBarState
            )
        },) { innerPadding ->
        NavigationGraph(
            activity = activity,
            navController = navController,
            innerPadding = innerPadding,
            onLanguageChanged = onLanguageChanged,
            onThemeChanged = onThemeChanged
        )
    }

}