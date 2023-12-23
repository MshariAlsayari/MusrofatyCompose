package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs

import androidx.compose.runtime.Composable
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.LazySenderSms
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.rememberPaginationAllSmsState


@Composable
fun AllSmsTab(viewModel: SenderSmsListViewModel ){
    val allSmsState = rememberPaginationAllSmsState(viewModel = viewModel, isDeleted = null, isFavorite = null)
    LazySenderSms(viewModel = viewModel, list = allSmsState)
}