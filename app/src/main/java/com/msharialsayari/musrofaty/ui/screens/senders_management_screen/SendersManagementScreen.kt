package com.msharialsayari.musrofaty.ui.screens.senders_management_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.ui_component.ProgressBar

@Composable
fun SendersManagementScreen(){
    val viewModel:SendersManagementViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    when{
        uiState.isLoading -> LoadingPageCompose()
        else -> PageCompose(viewModel = viewModel)
    }
}

@Composable
fun PageCompose(viewModel: SendersManagementViewModel){
    val uiState by viewModel.uiState.collectAsState()

}


@Composable
fun LoadingPageCompose(){
    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
            ){
        ProgressBar.CircleProgressBar()

    }

}