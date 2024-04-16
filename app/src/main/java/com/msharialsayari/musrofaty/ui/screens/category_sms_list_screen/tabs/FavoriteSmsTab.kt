package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListViewModel
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.SmsList


@Composable
fun FavoriteSmsTab(viewModel: CategorySmsListViewModel){
    val uiState  by viewModel.uiState.collectAsState()
    val smsList = uiState.favoriteSmsList?.collectAsLazyPagingItems()
    if (smsList != null)
        SmsList(viewModel = viewModel, list = smsList)

}