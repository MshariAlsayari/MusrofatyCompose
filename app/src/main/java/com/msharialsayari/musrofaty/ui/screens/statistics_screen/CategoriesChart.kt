package com.msharialsayari.musrofaty.ui.screens.statistics_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer


private const val PERSISTENT_MARKER_X = 10f


@Composable
fun CategoriesChartContainer(modifier: Modifier = Modifier, viewModel: StatisticsViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val charts = uiState.charts


    when {
        charts.isEmpty() -> Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) { EmptyComponent.EmptyTextComponent() }

        else -> CategoriesChart(modifier, viewModel)
    }


}

@Composable
fun CategoriesChart(modifier: Modifier = Modifier, viewModel: StatisticsViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val charts = uiState.charts

    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {

        items(charts) {
            val marker = rememberMarker()
            val thresholdLine = rememberThresholdLine(it.average)
            Chart(
                chart = lineChart(
                    persistentMarkers = remember(marker) { mapOf(PERSISTENT_MARKER_X to marker) },
                    decorations = remember(thresholdLine) { listOf(thresholdLine) },
                ),
                chartModelProducer = ChartEntryModelProducer(it.entries),
                marker = marker,

                startAxis = rememberStartAxis(
                    itemPlacer = remember { AxisItemPlacer.Vertical.default(maxItemCount = it.yItemCount) },
                    valueFormatter = it.yValueFormatter,
                    titleComponent = axisLabelComponent(),
                    title = it.yTitle
                ),
                bottomAxis = rememberBottomAxis(
                    itemPlacer = remember { AxisItemPlacer.Horizontal.default(addExtremeLabelPadding = true) },
                    valueFormatter = it.xValueFormatter,
                    titleComponent = axisLabelComponent(),
                    title = it.xTitle
                ),
            )
        }


    }

}