package com.msharialsayari.musrofaty.ui_component

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry


object ChartComponent {

    @Composable
    fun FinancialPieChartCompose(modifier:Modifier = Modifier, entries:ArrayList<PieEntry>, colors: ArrayList<Int>){
        Crossfade(targetState = entries) { pieChartData ->
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
    fun PieChartCompose(modifier:Modifier = Modifier, entries:ArrayList<PieEntry>,colors: ArrayList<Int>){
        Crossfade(targetState = entries) { pieChartData ->
            // on below line we are creating an
            // android view for pie chart.
            AndroidView(
                modifier = modifier.size(200.dp),
                update = { it.drawChart(pieChartData,colors) },
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
}
