package com.msharialsayari.musrofaty.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.msharialsayari.musrofaty.MainActivity
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.NavigationGraph
import com.msharialsayari.musrofaty.utils.enums.ScreenType


@Composable
fun NavigationRailLayout(
    activity: MainActivity,
    navController: NavHostController,
    items:List<BottomNavItem>,
    bottomBarState: MutableState<Boolean>,
    screenType: ScreenType,
    onLanguageChanged: () -> Unit,
    onThemeChanged: () -> Unit
){
    Row(modifier = Modifier.fillMaxSize()) {
        SideNavigation(navController = navController, items = items, bottomBarState = bottomBarState)
        NavigationGraph(
            activity = activity,
            navController = navController,
            innerPadding = PaddingValues(),
            screenType=screenType,
            onLanguageChanged = onLanguageChanged,
            onThemeChanged=onThemeChanged
        )



    }
}