package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen

import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.bottomsheets.CategorySmsListBottomSheetType
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import kotlinx.coroutines.flow.Flow

data class CategorySmsListUiState(
    val category: CategoryModel,
    var selectedTabIndex: Int = 0,
    var isRefreshing: Boolean = false,
    var isLoading: Boolean = false,
    val totalSms: Int = 0,
    var categories: Flow<List<CategoryEntity>>? = null,
    val bottomSheetType: CategorySmsListBottomSheetType? = null,
    var title:String = "",

    //allSms tab
    var allSmsList: Flow<PagingData<SmsModel>>? =null,

    //favorite tab
    var favoriteSmsList:Flow<PagingData<SmsModel>>? =null,

    //soft deleted tab
    var softDeletedSmsList:Flow<PagingData<SmsModel>>? =null,

    //Financial tab
    var financialTabLoading: Boolean = false,
    var financialStatistics: Map<String, FinancialStatistics> = emptyMap(),

    //filter times periods
    var selectedFilterTimeOption: SelectedItemModel? = null,

    //Date picker
    var startDate: Long = 0,
    var endDate: Long = 0,
)