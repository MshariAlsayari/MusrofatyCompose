package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel

data class CategorySmsListUIState (
    var isLoading:Boolean = false,
    var smsList :List<SmsModel> = emptyList(),
    var senders: List<SenderModel> = emptyList(),
    var category: CategoryModel = CategoryModel.getNoSelectedCategory(),
)