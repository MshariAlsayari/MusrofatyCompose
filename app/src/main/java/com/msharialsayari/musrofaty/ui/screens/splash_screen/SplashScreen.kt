package com.msharialsayari.musrofaty.ui.screens.splash_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val isLoading = remember { viewModel.isLoading }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if (isLoading.value) {
            ProgressBar.CircleProgressBar()
        }


        if (!isLoading.value) {
            navController.navigate(BottomNavItem.Dashboard.screen_route) {
                popUpTo(Screen.Splash.route) {
                    inclusive = true
                }
            }
        }

    }
}
