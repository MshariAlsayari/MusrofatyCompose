package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer

object ChartComponent {

    @Composable
    fun PieChartCompose(modifier:Modifier = Modifier, slices:List<PieChartData.Slice>){
        PieChart(
        modifier     = modifier.fillMaxSize(),
        pieChartData = PieChartData(slices),
        animation    = simpleChartAnimation(),
        sliceDrawer  = SimpleSliceDrawer(sliceThickness = 100f),
        )
    }
}