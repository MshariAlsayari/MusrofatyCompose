package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel

@Composable
fun rememberPaginationAllSmsState(viewModel: SenderSmsListViewModel, isDeleted:Boolean?, isFavorite:Boolean?): LazyPagingItems<SmsModel> {
    return viewModel.observingPaginationAllSms(isDeleted,isFavorite).collectAsLazyPagingItems()
}