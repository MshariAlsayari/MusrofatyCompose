package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.msharialsayari.musrofaty.ui_component.DividerComponent.HorizontalDividerComponent
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.SmsComponent


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SenderSmsListScreen(senderId: Int){
    val context = LocalContext.current
    val viewModel: SenderSmsListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    viewModel.getSenderModel(senderId)



    VerticalEasyList(
        list = SenderSmsListViewModel.SenderSmsListUiState.wrapSendersToSenderComponentModelList(uiState.sms, context),
        views = { SmsComponent(model = it) },
        dividerView = { HorizontalDividerComponent() },
        onItemClicked = {item, position ->  },
        isLoading = uiState.isLoading,
        loadingProgress = { ProgressBar.CircleProgressBar()},
        emptyView = { EmptyComponent.EmptyTextComponent()},
        onRefresh = {  }
    )

}