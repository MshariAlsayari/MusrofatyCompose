package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.EmptySmsCompose
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.PageLoading
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.rememberAllSmsState
import com.msharialsayari.musrofaty.ui_component.FinancialStatistics
import com.msharialsayari.musrofaty.ui_component.FinancialStatisticsModel

@Composable
fun FinancialStatisticsTab(viewModel: SenderSmsListViewModel ){

    val uiState  by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.selectedFilter, uiState.selectedFilterTimeOption){
        viewModel.getFinancialStatistics()
    }


    when {
        uiState.financialLoading -> PageLoading()
        uiState.financialStatistics.isNotEmpty() -> BuildChartCompose(viewModel = viewModel)
        else -> EmptySmsCompose()
    }

}

@Composable
fun BuildChartCompose(
    viewModel: SenderSmsListViewModel,
){

    val uiState  by viewModel.uiState.collectAsState()
    val listState  = rememberLazyListState()

    LazyColumn(
        modifier = Modifier,
        state = listState,
    ) {

        items(items = uiState.financialStatistics.values.toList() , itemContent =  {
            FinancialStatistics(model = FinancialStatisticsModel(
                filterOption = viewModel.getFilterTimeOption(),
                currency = it.currency,
                total = it.expenses.plus(it.income),
                incomeTotal = it.income,
                expensesTotal = it.expenses,
            )
            )


        })




    }


}