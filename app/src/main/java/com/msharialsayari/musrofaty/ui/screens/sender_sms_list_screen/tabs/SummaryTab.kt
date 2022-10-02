package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.tehras.charts.piechart.PieChartData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.PageLoading
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui_component.ChartComponent

@Composable
fun SummaryTab(senderId:Int){
    val viewModel: SenderSmsListViewModel =  hiltViewModel()
    val uiState                           by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit){ viewModel.getAllSmsBySenderId(senderId) }

    when{
        uiState.isTabLoading -> PageLoading()
        uiState.allSmsFlow != null -> BuildChartCompose(viewModel = viewModel, list = uiState.smsFlow?.collectAsLazyPagingItems()!! )
    }


}

@Composable
fun BuildChartCompose(viewModel: SenderSmsListViewModel, list: LazyPagingItems<SmsEntity>){
    val slices = list.itemSnapshotList.items
    ChartComponent.PieChartCompose(slices = listOf(PieChartData.Slice(0.5f, color = Color.Red), PieChartData.Slice(0.5f, color = Color.Green)))
}