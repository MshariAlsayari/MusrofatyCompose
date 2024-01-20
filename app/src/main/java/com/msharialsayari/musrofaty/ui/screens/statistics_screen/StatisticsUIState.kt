package com.msharialsayari.musrofaty.ui.screens.statistics_screen

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoriesChartModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.bottomsheets.BottomSheetType
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter

data class StatisticsUIState(
    val loading: Boolean = false,
    val list:List<SmsModel> = emptyList(),
    val bottomSheetType:BottomSheetType? = null,
    var selectedTimePeriod: SelectedItemModel? = null,
    var selectedCategory: SelectedItemModel? = null,
    var charts: List<CategoriesChartModel> = emptyList(),
    var startDate: Long = 0,
    var endDate: Long = 0,
)