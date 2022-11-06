package com.msharialsayari.musrofaty.ui.screens.splash_screen

import android.Manifest
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.jobs.InsertSmsJob
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
    val insertSmsRequest = OneTimeWorkRequestBuilder<InsertSmsJob>().build()
    val workManager = WorkManager.getInstance(context)
    val workInfo = workManager.getWorkInfosForUniqueWorkLiveData("Insert")
        .observeAsState()
        .value

    val insertInfo = remember(key1 = workInfo) {
        workInfo?.find { it.id == insertSmsRequest.id }
    }



    when(insertInfo?.state){
        WorkInfo.State.SUCCEEDED -> {
            LaunchedEffect(Unit) {
                onLoadingDone()
            }

        }
        else->{
            Log.i("Inserting status" , insertInfo?.state?.name ?: "null")
        }

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
            workManager.beginUniqueWork("Insert",ExistingWorkPolicy.KEEP, insertSmsRequest).enqueue()
            LaunchedEffect(Unit) {
                onLoadingDone()
            }
        }

    }

}
