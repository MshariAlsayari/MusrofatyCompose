package com.msharialsayari.musrofaty.ui.screens.statistics_screen

import com.msharialsayari.musrofaty.ui_component.SelectedItemModel

data class StatisticsUIState(
    val loading: Boolean = false,
    var selectedTimePeriod: SelectedItemModel? = null,
    var selectedCategory: SelectedItemModel? = null,
    var startDate: Long = 0,
    var endDate: Long = 0,
    var showStartDatePicker: Boolean = false,
    var showEndDatePicker: Boolean = false,
)