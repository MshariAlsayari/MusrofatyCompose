package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.utils.ColorTemplate
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toCategoryStatisticsModel
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.EmptySmsCompose
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.PageLoading
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui_component.CategoriesStatistics


@Composable
fun CategoriesStatisticsTab(senderId:Int, onSmsClicked: (String) -> Unit){
    val viewModel: SenderSmsListViewModel =  hiltViewModel()
    val uiState                           by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit){
        viewModel.getCategoriesStatistics(senderId)
    }

    when{
        uiState.isCategoriesStatisticsSmsPageLoading  -> PageLoading()
        uiState.categoriesStatistics.isNotEmpty()     -> BuildCategoriesChartCompose(viewModel = viewModel, onSmsClicked = {
            onSmsClicked(it)
        })
        uiState.categoriesStatistics.isEmpty()        -> EmptySmsCompose()

    }


}

@Composable
fun BuildCategoriesChartCompose(
    viewModel: SenderSmsListViewModel,
    onSmsClicked:(String)->Unit
){

    val uiState  by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val colors: ArrayList<Int> = ArrayList()
    colors.addAll(ColorTemplate.VORDIPLOM_COLORS.toList())
    colors.addAll(ColorTemplate.JOYFUL_COLORS.toList())
    colors.addAll(ColorTemplate.COLORFUL_COLORS.toList())
    colors.addAll(ColorTemplate.LIBERTY_COLORS.toList())
    colors.addAll(ColorTemplate.PASTEL_COLORS.toList())
    colors.add(ColorTemplate.getHoloBlue())


    CategoriesStatistics(categories = uiState.categoriesStatistics.values.mapIndexed { index, it ->  it.toCategoryStatisticsModel(context, colors[index]) }, onSmsClicked = {
        onSmsClicked(it)
    })

}


