package com.msharialsayari.musrofaty.ui.screens.splash_screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R

import com.msharialsayari.musrofaty.ui.theme.isLightTheme
import com.msharialsayari.musrofaty.ui_component.ButtonComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.utils.AppTheme
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.msharialsayari.requestpermissionlib.component.RequestPermissions
import com.msharialsayari.requestpermissionlib.model.DialogParams


@Composable
fun SplashScreen(onLoadingDone:()->Unit) {

    val context = LocalContext.current
    val viewModel: SplashViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val light = isLightTheme(appTheme = AppTheme.getThemById(SharedPreferenceManager.getTheme(context)))
    val imageRes = if (light)  R.drawable.ic_water_marker_light_mode else R.drawable.ic_water_marker_dark_mode
    val shouldAskPermission = remember { mutableStateOf(true) }



    if (shouldAskPermission.value){
        RequestPermissions(
            permissions = listOf(Manifest.permission.READ_SMS),
            rationalDialogParams = DialogParams(
                title = R.string.sms_permission_rational_dialog_title,
                message = R.string.sms_permission_rational_dialog_message,
            ),
            deniedDialogParams = DialogParams(
                title = R.string.sms_permission_denied_dialog_title,
                message = R.string.sms_permission_denied_dialog_message,
                positiveButtonText = R.string.permission_dialog_positive_button
            ),
            isGranted = {
                viewModel.initInsertSmsJob(context)
            },
            onDone = {
                shouldAskPermission.value = false
            }
        )
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(painter = painterResource(id = imageRes) , contentDescription = "")


        when {
            uiState.isLoading -> {
                ProgressBar.CircleProgressBar()
            }

            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && !shouldAskPermission.value ->{
                ButtonComponent.OutlineButton(
                    text = R.string.ask_sms_permission,
                    onClick = {
                        shouldAskPermission.value = true
                    }
                )

            }

            !uiState.isLoading && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED ->{
                LaunchedEffect(Unit) {
                    onLoadingDone()
                }
            }


        }

    }


}

@Composable
fun PageCompose(onLoadingDone:()->Unit){
    val context = LocalContext.current
    val viewModel: SplashViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val light = isLightTheme(appTheme = AppTheme.getThemById(SharedPreferenceManager.getTheme(context)))
    val imageRes = if (light)  R.drawable.ic_water_marker_light_mode else R.drawable.ic_water_marker_dark_mode

    LaunchedEffect(Unit) {
        viewModel.initInsertSmsJob(context)
    }


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
