package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsContainer
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.EmptySmsCompose
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.PageLoading
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui_component.DividerComponent
import com.msharialsayari.musrofaty.ui_component.FinancialStatistics
import com.msharialsayari.musrofaty.ui_component.FinancialStatisticsModel

@Composable
fun FinancialStatisticsTab(viewModel: SenderSmsListViewModel){

    val uiState  by viewModel.uiState.collectAsState()



    when {
        uiState.financialTabLoading -> PageLoading()
        uiState.financialStatistics.isNotEmpty() -> BuildChartCompose(viewModel = viewModel)
        else -> EmptySmsCompose()
    }

}

@Composable
fun BuildChartCompose(
    viewModel: SenderSmsListViewModel,
){

    val context = LocalContext.current
    val uiState  by viewModel.uiState.collectAsState()
    val listState  = rememberLazyListState()
    val list = uiState.financialStatistics.values.toList()

    LazyColumn(
        modifier = Modifier,
        state = listState,
    ) {

        itemsIndexed(items = list )  {index ,item->
            FinancialStatistics(
                model = FinancialStatisticsModel(
                    filterOption = viewModel.getFilterTimeOption(),
                    currency = item.currency,
                    total = item.expenses.plus(item.income),
                    incomeTotal = item.income,
                    expensesTotal = item.expenses,
                    expensesSmsList = item.expensesSmsList,
                    incomesSmsList = item.incomeSmsList
                )
            ) { smsList, isExpenses ->
                val ids = smsList.map { it.id }
                val smsContainer = SmsContainer(ids)
                viewModel.navigateToSmsListScreen(
                    smsContainer,
                    categoryModel = null,
                    isCategoryRowClicked = false,
                    isExpensesSmsRowClicked = isExpenses,
                    context
                )
            }

            if(list.lastIndex != index){
                DividerComponent.HorizontalDividerComponent()
            }


        }




    }


}