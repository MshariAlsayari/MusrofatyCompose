package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import kotlinx.coroutines.flow.Flow

data class SenderSmsListUiState(
        val sender: SenderModel,
        var selectedTabIndex: Int = 0,
        var isLoading: Boolean = false,
        var financialLoading: Boolean = false,
        var categoriesTabLoading: Boolean = false,
        var isRefreshing: Boolean = false,
        var selectedFilterTimeOption: SelectedItemModel? = null,
        var selectedFilter: SelectedItemModel? = null,
        var filters: List<FilterAdvancedModel> = emptyList(),
        var financialStatistics: Map<String, FinancialStatistics> = emptyMap(),
        var categoriesStatistics: Map<Int, CategoryStatistics> = emptyMap(),
        var startDate: Long = 0,
        var endDate: Long = 0,
        var showStartDatePicker: Boolean = false,
        var showEndDatePicker: Boolean = false,
        var showGeneratingExcelFileDialog: Boolean = false
)