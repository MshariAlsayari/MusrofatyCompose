package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.LazySenderSms
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel


@Composable
fun FavoriteSmsTab(viewModel: SenderSmsListViewModel, onCategoryClicked:()->Unit){
    val uiState  by viewModel.uiState.collectAsState()
    val smsList = uiState.favoriteSmsList?.collectAsLazyPagingItems()
    if (smsList != null)
        LazySenderSms(viewModel = viewModel, list = smsList) {
            onCategoryClicked()
        }

}