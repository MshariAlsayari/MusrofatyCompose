package com.msharialsayari.musrofaty.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.msharialsayari.musrofaty.MainViewModel
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.NavigationGraph


@Composable
fun NavigationRailLayout(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    items:List<BottomNavItem>,
    bottomBarState: MutableState<Boolean>
){
    Row(modifier = Modifier.fillMaxSize()) {
        SideNavigation(navController = navController, items = items, bottomBarState = bottomBarState)
        NavigationGraph(
            mainViewModel = mainViewModel,
            navController = navController,
            innerPadding = PaddingValues()
        )



    }
}