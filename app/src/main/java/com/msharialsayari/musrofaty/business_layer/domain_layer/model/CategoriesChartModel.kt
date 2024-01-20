package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.entry.FloatEntry
import java.time.LocalDate


data class CategoriesChartModel(
    val key:String,
    var data: Map<LocalDate, Float> = emptyMap(),
    var entries: List<FloatEntry> = emptyList(),
    var xItemCount:Int = 1,
    var yItemCount:Int = 6,
    var average:Float = 0f,
    var total:Float = 0f,
    var xValueFormatter : AxisValueFormatter<AxisPosition.Horizontal.Bottom> = DecimalFormatAxisValueFormatter(),
    var yValueFormatter : AxisValueFormatter<AxisPosition.Vertical.Start> = DecimalFormatAxisValueFormatter(pattern = "#;−#"),
    var xTitle:String = "",
    var yTitle:String = "",
)