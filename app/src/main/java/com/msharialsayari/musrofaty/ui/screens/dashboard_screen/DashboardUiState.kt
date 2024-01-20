package com.msharialsayari.musrofaty.ui.screens.dashboard_screen

import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryContainerStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import kotlinx.coroutines.flow.Flow

data class DashboardUiState(
    var isLoading: Boolean = false,
    var isRefreshing: Boolean = false,
    var isCategoriesStatisticsSmsPageLoading: Boolean = false,
    var selectedFilterTimeOption: SelectedItemModel? = null,
    var startDate: Long = 0,
    var endDate: Long = 0,
    var showStartDatePicker: Boolean = false,
    var showEndDatePicker: Boolean = false,
    var showFilterTimeOptionDialog: Boolean = false,
    var isFinancialStatisticsSmsPageLoading: Boolean = false,
    var financialStatistics: Map<String, FinancialStatistics> = emptyMap(),
    var categoriesStatistics: List<CategoryContainerStatistics>  = emptyList(),
    var smsFlow: Flow<PagingData<SmsEntity>>? =null,
    var isSmsPageLoading: Boolean = false,
    var query:String="",
    var senders:List<SenderModel> = listOf()
)