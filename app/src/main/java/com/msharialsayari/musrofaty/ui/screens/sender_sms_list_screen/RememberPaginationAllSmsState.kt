package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity

@Composable
fun rememberPaginationAllSmsState(viewModel: SenderSmsListViewModel, isDeleted:Boolean?, isFavorite:Boolean?): LazyPagingItems<SmsEntity> {
    return viewModel.observingPaginationAllSms(isDeleted,isFavorite).collectAsLazyPagingItems()
}