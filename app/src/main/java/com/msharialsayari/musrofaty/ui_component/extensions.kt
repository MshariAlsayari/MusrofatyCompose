package com.msharialsayari.musrofaty.ui_component

import android.graphics.Color
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


fun PieChart.drawFinancialChart(entries: ArrayList<PieEntry>, colors: ArrayList<Int>){

    this.description.isEnabled = false
    this.dragDecelerationFrictionCoef = 0.95f
    this.isRotationEnabled = true
    this.isHighlightPerTapEnabled = true
    this.legend.isEnabled = false


    this.setUsePercentValues(true)
    this.animateY(1400, Easing.EaseInOutQuad);
    this.setExtraOffsets(5f, 10f, 5f, 5f);
    this.setTransparentCircleColor(Color.TRANSPARENT)
    this.setHoleColor(Color.TRANSPARENT)
    this.setTransparentCircleAlpha(0)
    this.holeRadius = 0f




    this.setEntryLabelColor(this.context.getColor(R.color.white))
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
    data.setValueTextColor(this.context.getColor(R.color.white))
    this.data = data
    this.highlightValues(null)
    this.invalidate()
}


fun PieChart.drawChart(entries: ArrayList<PieEntry>, colors: ArrayList<Int>){

    this.description.isEnabled = false
    this.dragDecelerationFrictionCoef = 0.95f
    this.isRotationEnabled = true
    this.isHighlightPerTapEnabled = true
    this.legend.isEnabled = false


    this.setUsePercentValues(false)
    this.animateY(1400, Easing.EaseInOutQuad);
    this.setExtraOffsets(5f, 10f, 5f, 5f);
    this.setTransparentCircleColor(Color.TRANSPARENT)
    this.setHoleColor(Color.TRANSPARENT)
    this.setTransparentCircleAlpha(0)
    this.holeRadius = 0f




    this.setEntryLabelColor(Color.TRANSPARENT)
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
    data.setValueTextColor(Color.TRANSPARENT)
    this.data = data
    this.highlightValues(null)
    this.invalidate()
}

