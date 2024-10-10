package com.msharialsayari.musrofaty.ui.screens.filter_screen

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAmountModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ValidationModel
import com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets.FilterBottomSheetType


data class FilterUiState(
        var senderId: Int = -1,
        var filterId: Int = -1,
        var isCreateNewFilter:Boolean = false,
        var filterModel: FilterModel = FilterModel(
                id = 0,
                title = "",
                senderId = 0
        ),
        var filterWords: List<FilterWordModel> = emptyList(),
        val filterAmountModel: FilterAmountModel? = null,
        val bottomSheetType: FilterBottomSheetType? = null,
        var titleValidationModel: ValidationModel = ValidationModel(),
        var showConfirmationDialog:Boolean = false,
)