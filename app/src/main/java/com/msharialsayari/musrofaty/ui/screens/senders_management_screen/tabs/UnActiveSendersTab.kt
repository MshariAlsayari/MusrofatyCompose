package com.msharialsayari.musrofaty.ui.screens.senders_management_screen.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.SendersListViewModel
import com.msharialsayari.musrofaty.ui.screens.senders_management_screen.SendersListCompose
import com.msharialsayari.musrofaty.ui.screens.senders_management_screen.SendersManagementViewModel

@Composable
fun UnActiveSendersTab(viewModel: SendersManagementViewModel,onNavigateToSenderDetails:(senderId:Int)->Unit){
    val uiState by viewModel.uiState.collectAsState()
    val list = uiState.unActiveSenders?.collectAsState(initial = emptyList())?.value ?: emptyList()
    SendersListCompose(viewModel = viewModel, list = SendersListViewModel.SendersUiState.wrapSendersToSenderComponentModelList(list), onNavigateToSenderDetails = onNavigateToSenderDetails, isActiveTab = false)

}