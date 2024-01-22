package com.msharialsayari.musrofaty.ui.screens.store_sms_list_Screen

import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import kotlinx.coroutines.flow.Flow

data class StoreSmsListUiState(
    var isLoading:Boolean = false,
    var smsFlow: Flow<PagingData<SmsModel>>? =null,
    var senders: List<SenderModel> = emptyList(),
    )