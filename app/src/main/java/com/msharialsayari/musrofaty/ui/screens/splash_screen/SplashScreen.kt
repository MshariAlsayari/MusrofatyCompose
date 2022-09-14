package com.msharialsayari.musrofaty.ui.screens.splash_screen

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.permission.PermissionStatus
import com.msharialsayari.musrofaty.ui.permission.singlePermission
import com.msharialsayari.musrofaty.ui_component.DialogComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.TextComponent


@Composable
fun SplashScreen(navController: NavHostController, settingPermission:()->Unit) {

    val result = singlePermission(permission = Manifest.permission.READ_SMS)
    when (result) {
        PermissionStatus.GRANTED -> {
            val viewModel: SplashViewModel = hiltViewModel()
            val isLoading = remember { viewModel.isLoading }
            val total = remember { viewModel.smsListSize }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {

                if (isLoading.value) {
                    ProgressBar.CircleProgressBar()
                }

                TextComponent.HeaderText(
                    text = "Total of Sms = ${total.value}"
                )


                if (!isLoading.value) {
                    navController.navigate(BottomNavItem.Dashboard.screen_route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }

            }
        }
        PermissionStatus.SHOULD_SHOW_DIALOG -> {
            DialogComponent.MusrofatyDialog(
                title = "Permission",
                message ="Read SMS Permission" ,
                positiveBtnText = "Go to Setting" ,
                negativeBtnText = "",
                onClickPositiveBtn = {
                    settingPermission()
                }
            )
        }
        else -> {}
    }

}
