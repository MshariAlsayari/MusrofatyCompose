package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.LazySenderSms
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.PageLoading
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel


@Composable
fun AllSmsTab(senderId:Int, onSmsClicked:(String)->Unit){
    val viewModel: SenderSmsListViewModel =  hiltViewModel()
    val uiState                           by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit){ viewModel.getAllSms(senderId) }

    when{
        uiState.isAllSmsPageLoading -> PageLoading()
        uiState.smsFlow != null     -> LazySenderSms(viewModel = viewModel, list = uiState.smsFlow?.collectAsLazyPagingItems(), onSmsClicked = onSmsClicked )
    }


}