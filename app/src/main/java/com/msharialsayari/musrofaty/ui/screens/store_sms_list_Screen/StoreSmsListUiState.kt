package com.msharialsayari.musrofaty.ui.screens.store_sms_list_Screen

import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import kotlinx.coroutines.flow.Flow

data class StoreSmsListUiState(
    var smsList: List<SmsModel> = emptyList(),
    var category: CategoryModel = CategoryModel.getCategory(),
    var categories: Flow<List<CategoryEntity>>? = null,
    var selectedSms: SmsModel? = null
    )