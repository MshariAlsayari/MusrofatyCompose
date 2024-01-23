package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryContainerStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.bottomsheets.SenderSmsListBottomSheetType
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import kotlinx.coroutines.flow.Flow

data class SenderSmsListUiState(
        val sender: SenderModel,
        var selectedSms: SmsModel? = null,
        var categories: Flow<List<CategoryEntity>>? = null,
        var selectedTabIndex: Int = 0,
        var isLoading: Boolean = false,
        var isRefreshing: Boolean = false,
        val bottomSheetType: SenderSmsListBottomSheetType? = null,
        val totalSms: Int = 0,

        //allSms tab
        var allSmsList :Flow<PagingData<SmsModel>>? =null,

        //favorite tab
        var favoriteSmsList :Flow<PagingData<SmsModel>>? =null,

        //soft deleted tab
        var softDeletedSmsList :Flow<PagingData<SmsModel>>? =null,

        //Financial tab
        var financialTabLoading: Boolean = false,
        var financialStatistics: Map<String, FinancialStatistics> = emptyMap(),

        //Categories tab
        var categoriesTabLoading: Boolean = false,
        var categoriesStatistics: List<CategoryContainerStatistics> = emptyList(),

        //filter times periods
        var selectedFilterTimeOption: SelectedItemModel? = null,

        //filer words
        var selectedFilter: SelectedItemModel? = null,
        var filters: List<FilterAdvancedModel> = emptyList(),


        //Date picker
        var startDate: Long = 0,
        var endDate: Long = 0,

        //Excel dialog
        var showGeneratingExcelFileDialog: Boolean = false
)