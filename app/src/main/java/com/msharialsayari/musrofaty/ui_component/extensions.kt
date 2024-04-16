package com.msharialsayari.musrofaty.ui_component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.toArgb
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.msharialsayari.musrofaty.DayAxisValueFormatter
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoriesChart
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


fun PieChart.drawFinancialChart(entries: ArrayList<PieEntry>, colors: ArrayList<Color>){

    this.description.isEnabled = false
    this.dragDecelerationFrictionCoef = 0.95f
    this.isRotationEnabled = true
    this.isHighlightPerTapEnabled = true
    this.legend.isEnabled = false


    this.setUsePercentValues(true)
    this.animateY(1400, Easing.EaseInOutQuad);
    this.setExtraOffsets(5f, 10f, 5f, 5f);
    this.setTransparentCircleColor(Transparent.toArgb())
    this.setHoleColor(Transparent.toArgb())
    this.setTransparentCircleAlpha(0)
    this.holeRadius = 0f




    this.setEntryLabelColor(this.context.getColor(R.color.white))
    this.setEntryLabelTextSize(12f)


    val dataSet = PieDataSet(entries, "")

    dataSet.setDrawIcons(false)

    dataSet.sliceSpace = 1f
    dataSet.selectionShift = 5f


    dataSet.colors = colors.map { it.toArgb()}.toList()

    val data = PieData(dataSet)
    val valueFormatter = PercentFormatter()
    valueFormatter.mFormat = DecimalFormat("###,###,##0.0",DecimalFormatSymbols(Locale.ENGLISH))
    data.setValueFormatter(valueFormatter)
    data.setValueTextSize(11f)
    data.setValueTextColor(this.context.getColor(R.color.white))
    this.data = data
    this.highlightValues(null)
    this.invalidate()
}


fun PieChart.drawChart(title:String, colorTitle:Color, entries: ArrayList<PieEntry>, colors: ArrayList<Int>){

    this.description.isEnabled = false
    this.dragDecelerationFrictionCoef = 0.95f
    this.isRotationEnabled = true
    this.isHighlightPerTapEnabled = true
    this.legend.isEnabled = false


    this.setUsePercentValues(false)
    this.animateY(1400, Easing.EaseInOutQuad);
    this.setExtraOffsets(5f, 10f, 5f, 5f);
    this.setTransparentCircleColor(Transparent.toArgb())
    this.setHoleColor(Transparent.toArgb())
    this.setTransparentCircleAlpha(0)
    this.holeRadius = 50f
    this.centerText = title
    this.setCenterTextSize(14f)
    this.setDrawCenterText(true)
    this.setCenterTextColor(colorTitle.toArgb())




    this.setEntryLabelColor(Color.White.toArgb())
    this.setEntryLabelTextSize(12f)


    val dataSet = PieDataSet(entries, "")

    dataSet.setDrawIcons(false)

    dataSet.sliceSpace = 1f
    dataSet.selectionShift = 5f


    dataSet.colors = colors

    val data = PieData(dataSet)
    val valueFormatter = PercentFormatter()
    valueFormatter.mFormat = DecimalFormat("###,###,##0.0",DecimalFormatSymbols(Locale.ENGLISH))
    data.setValueFormatter(valueFormatter)
    data.setValueTextSize(11f)
    data.setValueTextColor(Color.Transparent.toArgb())
    this.data = data
    this.highlightValues(null)
    this.invalidate()
}

fun BarChart.drawCategoryChart(chart: CategoriesChart){

    setMaxVisibleValueCount(60)
    setPinchZoom(false)
    setDrawGridBackground(false)


    val xAxisFormatter: ValueFormatter = DayAxisValueFormatter(this)
    val xAxis: XAxis = xAxis
    xAxis.position = XAxisPosition.BOTTOM
    xAxis.setDrawGridLines(false)
    xAxis.granularity = 1f // only intervals of 1 day
    xAxis.labelCount = 7
    xAxis.valueFormatter = xAxisFormatter


    val leftAxis: YAxis = axisLeft
    leftAxis.setLabelCount(8, false)
    leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
    leftAxis.spaceTop = 15f
    leftAxis.axisMinimum = 0f
    leftAxis.mAxisMaximum = chart.max

    val l: Legend = legend
    l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
    l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
    l.orientation = Legend.LegendOrientation.HORIZONTAL
    l.setDrawInside(false)
    l.form = LegendForm.SQUARE
    l.formSize = 9f
    l.textSize = 11f
    l.xEntrySpace = 4f

    val set1= BarDataSet(chart.entries,"")

    val dataSets = ArrayList<IBarDataSet>()
    dataSets.add(set1)


    val charData = BarData(dataSets)
    charData.setValueTextSize(10f)
    charData.barWidth = 1f


    data = charData

}

