package com.msharialsayari.musrofaty.ui.screens.senders_management_screen

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderEntity
import kotlinx.coroutines.flow.Flow

data class SendersManagementUiState(
    var isLoading:Boolean = false,
    var senders: Flow<List<SenderEntity>>? = null,
    )