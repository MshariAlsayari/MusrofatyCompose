package com.msharialsayari.musrofaty.ui.screens.splash_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.ProgressBar


@Composable
fun SplashScreen(navController: NavHostController) {

    val viewModel: SplashViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.collectAsState(initial = true)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if (isLoading) {
            ProgressBar.CircleProgressBar()
        }


        if (!isLoading) {
            navController.navigate(BottomNavItem.Dashboard.screen_route) {
                popUpTo(Screen.Splash.route) {
                    inclusive = true
                }
            }
        }

    }
}
