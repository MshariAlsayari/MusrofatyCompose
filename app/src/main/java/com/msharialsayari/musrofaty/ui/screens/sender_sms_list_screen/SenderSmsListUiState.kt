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
        var selectedTabIndex: Int = 0,
        var isLoading: Boolean = false,
        var navigateBack: Boolean = false,
        var isAllSmsPageLoading: Boolean = false,
        var isFavoriteSmsPageLoading: Boolean = false,
        var isSoftDeletedSmsPageLoading: Boolean = false,
        var isFinancialStatisticsSmsPageLoading: Boolean = false,
        var isCategoriesStatisticsSmsPageLoading: Boolean = false,
        var isRefreshing: Boolean = false,
        val sender: SenderModel? = null,
        var smsFlow: Flow<PagingData<SmsEntity>>? = null,
        var favoriteSmsFlow: Flow<PagingData<SmsEntity>>? = null,
        var softDeletedSmsFlow: Flow<PagingData<SmsEntity>>? = null,
        var allSmsFlow: Flow<List<SmsEntity>>? = null,
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