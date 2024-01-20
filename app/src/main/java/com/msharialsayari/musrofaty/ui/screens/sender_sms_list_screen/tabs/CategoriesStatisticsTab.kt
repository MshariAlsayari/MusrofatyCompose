package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.EmptySmsCompose
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.PageLoading
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui_component.categoriesStatistics.CategoriesStatistics


@Composable
fun CategoriesStatisticsTab(viewModel: SenderSmsListViewModel){
    val uiState  by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.selectedFilter, uiState.selectedFilterTimeOption){
       viewModel.getCategoriesStatistics()
    }

    when{
        uiState.categoriesTabLoading  -> PageLoading()
        uiState.categoriesStatistics.isNotEmpty()     -> BuildCategoriesChartCompose(viewModel = viewModel)
        else      -> EmptySmsCompose()

    }


}

@Composable
fun BuildCategoriesChartCompose(viewModel: SenderSmsListViewModel){

    val uiState  by viewModel.uiState.collectAsState()
    val listState  = rememberLazyListState()
    val list  = uiState.categoriesStatistics

    LazyColumn(
        modifier = Modifier,
        state = listState,
    ) {

        items(list) {
            CategoriesStatistics(item = it, onRowClicked = {
                viewModel.navigateToCategorySmsListScreen(it)
            })
        }

    }


}


