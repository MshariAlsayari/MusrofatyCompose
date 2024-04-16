package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.github.mikephil.charting.data.PieEntry
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsContainer
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListViewModel
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.EmptySmsCompose
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.PageLoading
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.ChartComponent
import com.msharialsayari.musrofaty.ui_component.DividerComponent
import com.msharialsayari.musrofaty.ui_component.FinancialStatistics
import com.msharialsayari.musrofaty.ui_component.FinancialStatisticsModel

@Composable
fun FinancialStatisticsTab(viewModel: CategorySmsListViewModel){

    val uiState  by viewModel.uiState.collectAsState()



    when {
        uiState.financialTabLoading -> PageLoading()
        uiState.financialStatistics.isNotEmpty() -> BuildChartCompose(viewModel = viewModel)
        else -> EmptySmsCompose()
    }

}

@Composable
fun BuildChartCompose(
    viewModel: CategorySmsListViewModel,
){

    val context = LocalContext.current
    val uiState  by viewModel.uiState.collectAsState()
    val listState  = rememberLazyListState()
    val list = uiState.financialStatistics

    LazyColumn(
        modifier = Modifier,
        state = listState,
    ) {

        itemsIndexed(items = list )  {index ,item->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ChartComponent.BarChartCompose(chart = item)

            }

            if(list.lastIndex != index){
                DividerComponent.HorizontalDividerComponent()
            }


        }




    }


}