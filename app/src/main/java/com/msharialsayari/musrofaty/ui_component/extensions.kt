package com.msharialsayari.musrofaty.ui_component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent

import androidx.compose.ui.graphics.toArgb
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.msharialsayari.musrofaty.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


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

