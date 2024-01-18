package com.msharialsayari.musrofaty.ui.screens.statistics_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@Composable
fun CategoriesChart(viewModel: StatisticsViewModel){

    val uiState by viewModel.uiState.collectAsState()
    val entries = uiState.listOfEntries


    Chart(
        chart = lineChart(),
        chartModelProducer = ChartEntryModelProducer(entries),
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(),
    )
}