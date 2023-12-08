package com.msharialsayari.musrofaty.ui.screens.sms_screen

import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import kotlinx.coroutines.flow.Flow

data class SmsUiState (
        var sms : SmsModel? = null,
        var sender : SenderModel? = null,
        var storeAndCategoryModel: StoreAndCategoryModel? = null,
        var selectedCategory: SelectedItemModel? = null,
        var categories: Flow<List<CategoryEntity>>? = null,
    )