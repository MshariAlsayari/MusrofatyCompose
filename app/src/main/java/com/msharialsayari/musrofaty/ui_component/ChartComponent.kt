package com.msharialsayari.musrofaty.ui_component

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoriesChart
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme


object ChartComponent {

    @Composable
    fun FinancialPieChartCompose(modifier:Modifier = Modifier, entries:ArrayList<PieEntry>, colors: ArrayList<Color>){
        Crossfade(targetState = entries, label = "") { pieChartData ->
            // on below line we are creating an
            // android view for pie chart.
            AndroidView(
                modifier = modifier.size(200.dp),
                update = { it.drawFinancialChart(pieChartData, colors) },
                factory = { context ->
                    PieChart(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                    }
                },
            )
        }
    }


    @Composable
    fun PieChartCompose(modifier:Modifier = Modifier, title:String, entries:ArrayList<PieEntry>,colors: ArrayList<Int>){
        val titleColor = MusrofatyTheme.colors.onBackground
        Crossfade(targetState = entries, label = "") { pieChartData ->
            // on below line we are creating an
            // android view for pie chart.
            AndroidView(
                modifier = modifier.size(200.dp),
                update = { it.drawChart(title, titleColor,pieChartData,colors) },
                factory = { context ->
                    PieChart(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                    }
                },
            )
        }
    }


    @Composable
    fun BarChartCompose(modifier:Modifier = Modifier, chart: CategoriesChart){
        Crossfade(targetState = chart, label = "") { chart ->
            // on below line we are creating an
            // android view for pie chart.
            AndroidView(
                modifier = modifier.size(200.dp),
                update = { it.drawCategoryChart(chart) },
                factory = { context ->
                    BarChart(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                    }
                },
            )
        }
    }
}
