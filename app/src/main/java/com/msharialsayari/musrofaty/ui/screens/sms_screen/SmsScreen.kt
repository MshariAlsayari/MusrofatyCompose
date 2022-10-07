package com.msharialsayari.musrofaty.ui.screens.sms_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.SmsActionType
import com.msharialsayari.musrofaty.ui_component.SmsComponent

@Composable
fun SmsScreen(smsId:String){
    val viewModel:SmsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit ){
        viewModel.getData(smsId)
    }

    when{
        uiState.isLoading -> ProgressCompose()
        uiState.sms != null -> PageCompose(viewModel,uiState.sms!!)
    }

}

@Composable
fun ProgressCompose(){
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        ProgressBar.CircleProgressBar()

    }
}

@Composable
fun PageCompose(viewModel: SmsViewModel, sms:SmsModel){
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        SmsComponent(
            model = viewModel.wrapSendersToSenderComponentModel(sms, context),
            onActionClicked = { model, action ->
                when (action) {
                    SmsActionType.FAVORITE -> viewModel.favoriteSms(
                        model.id,
                        model.isFavorite
                    )
                    SmsActionType.SHARE -> {}
                }
            })


    }
}

