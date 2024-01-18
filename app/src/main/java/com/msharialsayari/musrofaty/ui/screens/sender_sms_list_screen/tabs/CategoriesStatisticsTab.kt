package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.github.mikephil.charting.utils.ColorTemplate
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toCategoryStatisticsModel
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
    val context = LocalContext.current
    val colors: ArrayList<Int> = ArrayList()
    colors.addAll(ColorTemplate.VORDIPLOM_COLORS.toList())
    colors.addAll(ColorTemplate.JOYFUL_COLORS.toList())
    colors.addAll(ColorTemplate.COLORFUL_COLORS.toList())
    colors.addAll(ColorTemplate.LIBERTY_COLORS.toList())
    colors.addAll(ColorTemplate.PASTEL_COLORS.toList())
    colors.add(ColorTemplate.getHoloBlue())

    val categories = uiState.categoriesStatistics.values.mapIndexed { index, it ->
        it.toCategoryStatisticsModel(
            context,
            colors[index]
        )
    }

    LazyColumn(
        modifier = Modifier,
        state = listState,
    ) {

        item {
            CategoriesStatistics(categories = categories)
        }

    }


}


