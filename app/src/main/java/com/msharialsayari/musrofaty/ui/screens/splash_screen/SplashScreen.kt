package com.msharialsayari.musrofaty.ui.screens.splash_screen

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.ProgressBar



@Composable
fun SplashScreen(onLoadingDone:()->Unit) {

    val context = LocalContext.current
    val viewModel: SplashViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val imageRes = if (MusrofatyTheme.isDarkMode)  R.drawable.ic_water_marker_dark_mode else R.drawable.ic_water_marker_light_mode

    LaunchedEffect(Unit) {
        viewModel.initInsertSmsJob(context)
        viewModel.initInsertStoresJob(context)
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

