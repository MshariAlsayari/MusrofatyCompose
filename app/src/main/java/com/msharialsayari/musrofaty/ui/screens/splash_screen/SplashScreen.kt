package com.msharialsayari.musrofaty.ui.screens.splash_screen

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.permission.PermissionStatus
import com.msharialsayari.musrofaty.ui.permission.singlePermission
import com.msharialsayari.musrofaty.ui.theme.isLightTheme
import com.msharialsayari.musrofaty.ui_component.DialogComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.utils.AppTheme
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager


@Composable
fun SplashScreen(settingPermission:()->Unit, onLoadingDone:()->Unit) {

    when (singlePermission(permission = Manifest.permission.READ_SMS)) {
        PermissionStatus.GRANTED -> {
            PageCompose(onLoadingDone = onLoadingDone)
        }
        PermissionStatus.SHOULD_SHOW_DIALOG -> {
            DialogComponent.MusrofatyDialog(
                message = stringResource(id = R.string.permission_dialog_message) ,
                positiveBtnText = stringResource(id = R.string.permission_dialog_positive_button) ,
                negativeBtnText = "",
                onClickPositiveBtn = {
                    settingPermission()
                }
            )
        }
        else -> {}
    }

}

@Composable
fun PageCompose(onLoadingDone:()->Unit){
    val context = LocalContext.current
    val viewModel: SplashViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val light = isLightTheme(appTheme = AppTheme.getThemById(SharedPreferenceManager.getTheme(context)))
    val imageRes = if (light)  R.drawable.ic_water_marker_light_mode else R.drawable.ic_water_marker_dark_mode

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Image(painter = painterResource(id = imageRes) , contentDescription = "")
        if (uiState.isLoading) {
            ProgressBar.CircleProgressBar()
        }

        if (!uiState.isLoading) {
            LaunchedEffect(Unit) {
                onLoadingDone()
            }
        }

    }

}
