package com.msharialsayari.musrofaty.ui.screens.sms_list_screen

import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.ui.screens.sms_list_screen.bottomSheet.SmsListBottomSheetType
import com.msharialsayari.musrofaty.ui_component.SortedByAmount
import kotlinx.coroutines.flow.Flow

data class SmsListUIState (
    var smsList :List<SmsModel> = emptyList(),
    var categories: Flow<List<CategoryEntity>>? = null,
    var selectedSms: SmsModel? = null,
    var selectedSortedByAmount: SortedByAmount = SortedByAmount.HIGHEST,
    var bottomSheetType: SmsListBottomSheetType? = null
)