package com.msharialsayari.musrofaty.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.msharialsayari.musrofaty.MainViewModel
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.NavigationGraph

@Composable
fun BottomBarLayout(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    items:List<BottomNavItem>,
    bottomBarState: MutableState<Boolean>) {

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        bottomBar = {
            BottomNavigation(
                navController = navController,
                items = items,
                bottomBarState = bottomBarState
            )
        },) { innerPadding ->
        NavigationGraph(
            mainViewModel = mainViewModel,
            navController = navController,
            innerPadding = innerPadding
        )
    }

}