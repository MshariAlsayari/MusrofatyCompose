package com.msharialsayari.musrofaty.ui.screens.store_sms_list_Screen

import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import kotlinx.coroutines.flow.Flow

data class StoreSmsListUiState(
    var isLoading:Boolean = false,
    var smsFlow: Flow<PagingData<SmsEntity>>? =null,
    )