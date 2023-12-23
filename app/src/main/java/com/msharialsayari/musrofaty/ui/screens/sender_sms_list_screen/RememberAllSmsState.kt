package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity

@Composable
fun rememberAllSmsState(viewModel: SenderSmsListViewModel): State<List<SmsEntity>> {
    return viewModel.observingAllSms().collectAsState(emptyList())
}