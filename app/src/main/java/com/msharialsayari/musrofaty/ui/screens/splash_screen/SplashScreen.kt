package com.msharialsayari.musrofaty.ui.screens.splash_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.TextComponent
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        TextComponent.HeaderText(
            text = "Splash Screen"
        )


        LaunchedEffect(Unit) {
            delay(2000)
            navController.navigate(BottomNavItem.Dashboard.screen_route) {
                popUpTo(Screen.Splash.route) {
                    inclusive = true
                }
            }
        }
    }
}